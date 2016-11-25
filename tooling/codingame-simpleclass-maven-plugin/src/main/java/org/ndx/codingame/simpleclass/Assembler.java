package org.ndx.codingame.simpleclass;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSelectInfo;
import org.apache.commons.vfs2.FileSelector;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.Comment;

@Mojo(name = "assemble",
		requiresDependencyResolution=ResolutionScope.RUNTIME,
		defaultPhase=LifecyclePhase.PREPARE_PACKAGE)
public class Assembler extends AbstractMojo {

	@Parameter(defaultValue="${project.build.directory}/codingame/Player.java", property="codingame.path")
	private File output;

	@Parameter(defaultValue="${project}", required=true, readonly=true)
	public MavenProject project;
	@Parameter(name="playerClass", property="playerClass")
	public File playerClass;
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			getLog().info(String.format("Should generate a Player in root package from Player class %s declared in a package", playerClass));
			Collection<File> sourceFiles = findJavaSources();
			if(sourceFiles.isEmpty()) {
				getLog().warn("There is no player class to extend. Ending");
			}
			getLog().info(String.format("Source files are %s", sourceFiles));
			Map<String, CompilationUnit> classes = new ClassesFinder(getLog())
					.findAll(sourceFiles, project.getArtifacts()); 
			getLog().info(String.format("Extend local Player class (found at %s) with all local classes and classes found in sources", playerClass));
			CompilationUnit generated = new PlayerBuilder(classes.keySet()).build(classes, ParserUtils.getPublicClassFullName(playerClass));
			addBuildDateTo(generated);
			FileUtils.write(output, generated.toString());
		} catch(Exception e) {
			throw new MojoExecutionException("Unable to create compressed Player class", e);
		}
	}
	
	/**
	 * Find the Java source files. Notice the {@link #playerClass} may be set if null before this call
	 * (yup, this is bad side effect)
	 * @return
	 */
	private Collection<File> findJavaSources() {
		Collection<File> returned = new ArrayList<>();
		for(String folder : project.getCompileSourceRoots()) {
			if(!folder.contains("target")) {
				File file = new File(folder);
				Collection<File> classes = FileUtils.listFiles(file, 
						FileFilterUtils.suffixFileFilter(".java"), 
						FileFilterUtils.trueFileFilter());
				if(playerClass==null) {
					Collection<File> playerClasses = FileUtils.listFiles(file,
							FileFilterUtils.or(FileFilterUtils.nameFileFilter("Player.java"),
									FileFilterUtils.nameFileFilter("Solution.java")), 
							FileFilterUtils.trueFileFilter());
					if(playerClasses.size()==1) {
						playerClass = playerClasses.iterator().next();
					} else if(playerClasses.size()<1){
						throw new UnsupportedOperationException("No player class was found in project ! Aborting.");
					} else if(playerClasses.size()>1){
						throw new UnsupportedOperationException(
								String.format("Seems like there is more than one candidate class for embedding all the content. "
								+ "\nWhich one to choose between %s ?"
								+ "\nYou'll have to choose by setting the <playerClass>???</playerClass> configuration property", playerClasses));
					}
				}
				returned.addAll(classes);
			}
		}
		return returned;
	}

	private void addBuildDateTo(CompilationUnit playerUnit) {
		Instant instant = Instant.now().truncatedTo( ChronoUnit.MILLIS );
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime zdt = instant.atZone( zoneId );
		String output = zdt.toString();
		playerUnit.setComment(new BlockComment(
						String.format("\nProudly built by %s on %s"
								+ "\n@see https://github.com/Riduidel/codingame/tree/master/tooling/codingame-simpleclass-maven-plugin\n", 
								getClass().getName(), 
								output)));
	}

}
