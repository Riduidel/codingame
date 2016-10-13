package org.ndx.codingame.simpleclass;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
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
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.GenericVisitor;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

@Mojo(name = "assemble",
		requiresDependencyResolution=ResolutionScope.RUNTIME,
		defaultPhase=LifecyclePhase.PREPARE_PACKAGE)
public class Assembler extends AbstractMojo {

	private FileSystemManager fsManager = null;
	
	{
		try {
			fsManager = VFS.getManager();
		} catch (FileSystemException e) {
			throw new RuntimeException("VFS didn't start !", e);
		}
	}

	@Parameter(defaultValue="${project.build.directory}/codingame/Player.java", property="codingame.path")
	private File output;

	@Parameter(defaultValue="${project}", required=true, readonly=true)
	public MavenProject project;
	@Parameter(name="playerClass", property="playerClass")
	public File playerClass;
	
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		try {
			getLog().info("Should generate a Player in root package from Player class declared in a package");
			Collection<File> sourceFiles = findJavaSources();
			if(sourceFiles.isEmpty()) {
				getLog().warn("There is no player class to extend. Ending");
			}
			getLog().info(String.format("Source files are %s", sourceFiles));
			Map<String, CompilationUnit> classes = new HashMap<>();
			classes.putAll(parseSources(sourceFiles));
			getLog().info("Parse all dependencies of type source");
			classes.putAll(parseDependencies());
			getLog().info(String.format("Extend local Player class (found at %s) with all local classes and classes found in sources", playerClass));
			createExtendedPlayerClassUsing(playerClass, output, classes);
		} catch(Exception e) {
			throw new MojoExecutionException("Unable to create compressed Player class", e);
		}
	}
	
	private Map<String, CompilationUnit> parseDependencies() {
		Map<String, CompilationUnit> units = new HashMap<>();
		for (Artifact	artifact : project.getArtifacts()) {
			if("sources".equals(artifact.getClassifier())) {
				File artifactFile = artifact.getFile();
				String name = artifactFile.getName();
				String path = artifactFile.getAbsolutePath();
				if(name.endsWith("jar")) {
					try {
						FileObject artifactObject = fsManager.resolveFile(String.format("jar:%s", path));
						FileObject[] javaFiles = artifactObject.findFiles(new FileSelector() {
							
							@Override
							public boolean traverseDescendents(FileSelectInfo fileInfo) throws Exception {
								return true;
							}
							
							@Override
							public boolean includeFile(FileSelectInfo fileInfo) throws Exception {
								return fileInfo.getFile().getName().getExtension().equals("java");
							}
						});
						getLog().debug(String.format("Found %s java files in %s", javaFiles.length, artifact));
						for(FileObject f : javaFiles) {
							try {
								try(InputStream s = f.getContent().getInputStream()) {
									CompilationUnit loadedClass = parse(s);
									units.put(getPublicClassFullName(loadedClass), loadedClass);
								}
							} catch(Exception e) {
								getLog().debug(String.format("Unable to read class from %s", f.getURL().toString()), e);
							}
						}
					} catch (FileSystemException e) {
						getLog().warn("Unable to open JAR "+path, e);
					}
				}
			}
		}
		return units;
	}

	private Map<String, CompilationUnit> parseSources(Collection<File> sourceFiles) throws ParseException, IOException {
		Map<String, CompilationUnit> units = new HashMap<>();
		for(File f : sourceFiles) {
			CompilationUnit parsed = parse(f);
			units.put(getPublicClassFullName(parsed), parsed);
		}
		return units;
	}

	private CompilationUnit parse(InputStream s) throws ParseException {
		return augment(JavaParser.parse(s));
	}

	/**
	 * Add line number comments for easier source mapping
	 * @param parse
	 * @return
	 */
	private CompilationUnit augment(CompilationUnit parse) {
		return parse;
	}

	private CompilationUnit parse(File f) throws ParseException, IOException {
		return augment(JavaParser.parse(f));
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
							FileFilterUtils.nameFileFilter("Player.java"), 
							FileFilterUtils.trueFileFilter());
					if(playerClasses.size()==1) {
						playerClass = playerClasses.iterator().next();
					}
				}
				returned.addAll(classes);
			}
		}
		return returned;
	}

	private void createExtendedPlayerClassUsing(File input, File output, Map<String, CompilationUnit> classes) throws Exception {
		CompilationUnit playerUnit = parse(input);
		String playerClassName = getPublicClassFullName(playerUnit);
		// Before all, remove package declaration
		playerUnit.setPackage(null);
		// And mark the class as non public, otherwise Codingame won't accept it
		ClassOrInterfaceDeclaration playerClass = getPublicClassIn(playerUnit);
		playerClass.setModifiers(0);
		output.getParentFile().mkdirs();
		if(output.exists())
			output.delete();
		ClassOrInterfaceDeclaration player = null;
		for(TypeDeclaration declaration : playerUnit.getTypes()) {
			if(declaration.getName().endsWith("Player")) {
				if (declaration instanceof ClassOrInterfaceDeclaration) {
					player = (ClassOrInterfaceDeclaration) declaration;
				}
			}
		}
		Collection<String> importsToRemove = extendPlayerClassUsing(classes, playerUnit, playerClassName, player);
		cleanupImports(playerUnit, importsToRemove);
		FileUtils.write(output, playerUnit.toString());
	}

	private void cleanupImports(CompilationUnit playerUnit, Collection<String> importsToRemove) {
		Collection<ImportDeclaration> declarationsToRemove = new ArrayList<>();
		for(ImportDeclaration d : playerUnit.getImports()) {
			String importName = d.getName().toString();
			if(d.isStatic()) {
				importName = importName.substring(0, importName.lastIndexOf('.'));
			}
			if(importsToRemove.contains(importName)) {
				declarationsToRemove.add(d);
			}
		}
		playerUnit.getImports().removeAll(declarationsToRemove);
	}

	private Collection<String> extendPlayerClassUsing(Map<String, CompilationUnit> classes, CompilationUnit playerUnit,
			String playerClassName, ClassOrInterfaceDeclaration player) {
		Collection<String> importsToRemove = new ArrayList<>();
		// And now, for each compilation unit that is not the input file, add class as static class
		// and not yet imported imports
		for(Map.Entry<String, CompilationUnit> entry : classes.entrySet()) {
			String className = entry.getKey();
			if(!playerClassName.equals(className)) {
				extendPlayerClassUsing(playerUnit, player, className, entry.getValue());
				importsToRemove.add(className);
			}
		}
		return importsToRemove;
	}

	private String getPublicClassFullName(CompilationUnit playerUnit) {
		ClassOrInterfaceDeclaration playerClass = getPublicClassIn(playerUnit);
		PackageDeclaration packageObject = playerUnit.getPackage();
		if(packageObject==null) {
			return playerClass.getName();
		} else {
			String playerClassName  = packageObject.getPackageName()+"."+playerClass.getName();
			return playerClassName;
		}
	}

	private ClassOrInterfaceDeclaration getPublicClassIn(CompilationUnit playerUnit) {
		ClassOrInterfaceDeclaration playerClass = (ClassOrInterfaceDeclaration) playerUnit.getTypes().stream()
				.findFirst()
				.filter(t -> Modifier.isPublic(t.getModifiers()))
				.orElseThrow(() -> new RuntimeException("Each java source should contain a public class, or else it's impossible for me to assemble them"));
		return playerClass;
	}
	private void extendPlayerClassUsing(CompilationUnit playerCompilationUnit, ClassOrInterfaceDeclaration player, String addedClassQualifiedName, CompilationUnit addedClass) {
		// Now remove import of that class and its inner classes
		// Then added required import declaration
		for(ImportDeclaration declaration : addedClass.getImports()) {
			if(!playerCompilationUnit.getImports().contains(declaration)) {
				playerCompilationUnit.getImports().add(declaration);
			}
		}
		
		
		for(TypeDeclaration declaration : addedClass.getTypes()) {
			boolean abstractClass = Modifier.isAbstract(declaration.getModifiers());
			declaration.setModifiers(0);
			if(abstractClass)
				declaration.setModifiers(Modifier.ABSTRACT);
			playerCompilationUnit.getTypes().add(declaration);
		}
	}
}
