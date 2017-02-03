package org.ndx.codingame.simpleclass;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;

public class ParserUtils {

	public static String getPublicClassFullName(final CompilationUnit playerUnit) {
		final TypeDeclaration playerClass = ParserUtils.getPublicClassIn(playerUnit);
		final PackageDeclaration packageObject = playerUnit.getPackage();
		if (packageObject == null) {
			return playerClass.getName();
		} else {
			final String playerClassName = packageObject.getPackageName() + "." + playerClass.getName();
			return playerClassName;
		}
	}

	public static TypeDeclaration getPublicClassIn(final CompilationUnit playerUnit) {
		final TypeDeclaration playerClass = playerUnit.getTypes().stream()
				.findFirst()
				.filter(t -> 
					Modifier.isPublic(t.getModifiers()))
				.orElseThrow(() -> new RuntimeException(
						"Each java source should contain a public class, or else it's impossible for me to assemble them"));
		return playerClass;
	}

	public static CompilationUnit parse(final File f) throws ParseException, IOException {
		return ParserUtils.augment(JavaParser.parse(f));
	}

	/**
	 * Add line number comments for easier source mapping
	 * 
	 * @param parse
	 * @return
	 */
	public static CompilationUnit augment(final CompilationUnit parse) {
		return parse;
	}

	public static String getPublicClassFullName(final File playerClass) throws ParseException, IOException {
		return getPublicClassFullName(parse(playerClass));
	}

}
