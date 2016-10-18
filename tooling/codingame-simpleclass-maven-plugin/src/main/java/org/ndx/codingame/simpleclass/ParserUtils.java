package org.ndx.codingame.simpleclass;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

public class ParserUtils {

	public static String getPublicClassFullName(CompilationUnit playerUnit) {
		TypeDeclaration playerClass = ParserUtils.getPublicClassIn(playerUnit);
		PackageDeclaration packageObject = playerUnit.getPackage();
		if (packageObject == null) {
			return playerClass.getName();
		} else {
			String playerClassName = packageObject.getPackageName() + "." + playerClass.getName();
			return playerClassName;
		}
	}

	public static TypeDeclaration getPublicClassIn(CompilationUnit playerUnit) {
		TypeDeclaration playerClass = (ClassOrInterfaceDeclaration) playerUnit.getTypes().stream()
				.findFirst()
				.filter(t -> Modifier.isPublic(t.getModifiers())).orElseThrow(() -> new RuntimeException(
						"Each java source should contain a public class, or else it's impossible for me to assemble them"));
		return playerClass;
	}

	public static CompilationUnit parse(File f) throws ParseException, IOException {
		return ParserUtils.augment(JavaParser.parse(f));
	}

	/**
	 * Add line number comments for easier source mapping
	 * 
	 * @param parse
	 * @return
	 */
	public static CompilationUnit augment(CompilationUnit parse) {
		return parse;
	}

	public static String getPublicClassFullName(File playerClass) throws ParseException, IOException {
		return getPublicClassFullName(parse(playerClass));
	}

}
