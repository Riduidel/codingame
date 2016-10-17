package org.ndx.codingame.simpleclass;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSelectInfo;
import org.apache.commons.vfs2.FileSelector;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemManager;
import org.apache.commons.vfs2.VFS;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.logging.Log;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;

/**
 * Find all the classes, both in local files, and in sources classified
 * dependencies
 * 
 * @author ndelsaux
 *
 */
public class ClassesFinder {
	private FileSystemManager fsManager = null;
	
	{
		try {
			fsManager = VFS.getManager();
		} catch (FileSystemException e) {
			throw new RuntimeException("VFS didn't start !", e);
		}
	}

	private Log log;

	public ClassesFinder(Log log) {
		this.log = log;
	}

	private Map<String, CompilationUnit> parseSources(Collection<File> sourceFiles) throws ParseException, IOException {
		Map<String, CompilationUnit> units = new HashMap<>();
		for (File f : sourceFiles) {
			CompilationUnit parsed = ParserUtils.parse(f);
			units.put(ParserUtils.getPublicClassFullName(parsed), parsed);
		}
		return units;
	}

	private CompilationUnit parse(InputStream s) throws ParseException {
		return ParserUtils.augment(JavaParser.parse(s));
	}

	private Map<String, CompilationUnit> parseDependencies(Set<Artifact> artifacts) {
		Map<String, CompilationUnit> units = new HashMap<>();
		for (Artifact	artifact : artifacts) {
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
						log.debug(String.format("Found %s java files in %s", javaFiles.length, artifact));
						for(FileObject f : javaFiles) {
							try {
								try(InputStream s = f.getContent().getInputStream()) {
									CompilationUnit loadedClass = parse(s);
									units.put(ParserUtils.getPublicClassFullName(loadedClass), loadedClass);
								}
							} catch(Exception e) {
								log.debug(String.format("Unable to read class from %s", f.getURL().toString()), e);
							}
						}
					} catch (FileSystemException e) {
						log.warn("Unable to open JAR "+path, e);
					}
				}
			}
		}
		return units;
	}
	
	public Map<String, CompilationUnit> findAll(Collection<File> sourceFiles, Set<Artifact> set) throws ParseException, IOException {
		Map<String, CompilationUnit> classes = new TreeMap<>();
		classes.putAll(parseSources(sourceFiles));
		classes.putAll(parseDependencies(set));
		return classes;
	}

}
