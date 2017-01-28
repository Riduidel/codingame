package org.ndx.codingame.gaming;

import java.util.Collection;

public interface ToUnitTest {
	public static StringBuilder declareCollection(final Collection<?> collection, final Class<?> declaredInterfaceType, final Class<?> declaredContent, final String name) {
		final StringBuilder returned = new StringBuilder();
		returned.append(declaredInterfaceType.getSimpleName()).append("<").append(declaredContent.getName()).append("> ").append(name);
		returned.append("=");
		returned.append("new ").append(collection.getClass().getSimpleName()).append("<>();");
		return returned;
	}
	
	public static StringBuilder declaredFilledCollection(final String prefix, final Collection<? extends ToUnitTest> collection, final Class<?> declaredInterfaceType, final Class<?> declaredContent, final String name) {
		final StringBuilder returned = declareCollection(collection, declaredInterfaceType, declaredContent, name);
		returned.append("\n");
		final String nextPrefix = prefix+"\t";
		for(final ToUnitTest t : collection) {
			returned.append(nextPrefix).append(name).append(".add(").append(t.toUnitTestConstructor(nextPrefix)).append(");\n");
		}
		return returned;
	}

	String METHOD_PREFIX = "\t\t\t";
	String CONTENT_PREFIX = METHOD_PREFIX+"\t";
	
	public abstract StringBuilder toUnitTestConstructor(String multilinePrefix);

}
