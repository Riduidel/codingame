package org.ndx.codingame.gaming.tounittest;

import java.util.Collection;

public class ToUnitTestHelpers {

	public static final String METHOD_PREFIX = "\t\t\t";
	public static final String CONTENT_PREFIX = METHOD_PREFIX+"\t";

	public ToUnitTestHelpers() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Declare a simple collection of elements
	 * @param effectiveValue effective collection which will be used
	 * @param container declared type of collection
	 * @param content declared type of elements in collection. Pass Object class to have no type defined
	 * @param name name of collection in output string
	 * @return a line allowing declaration of collection in used test.
	 */
	public static StringBuilder declareElement(final Collection<?> effectiveValue, final Class<?> container, final Class<?> content, final Class<?> instanciatedType, final String name) {
		final StringBuilder returned = new StringBuilder();
		returned.append(container.getSimpleName());
		if(!Object.class.equals(content)) {
			returned.append("<").append(content.getName()).append(">");
		}
		returned.append(" ").append(name);
		returned.append("=");
		returned.append("new ").append(instanciatedType.getSimpleName());
		if(!Object.class.equals(content)) {
			returned.append("<>");
		}
		returned.append("();");
		return returned;
	}

	/**
	 * Declare a collection and add all elements given in
	 * @param prefix prefix for correct alignment
	 * @param value source value used to initialize container
	 * @param container container type which will be used in test
	 * @param content content type declared in collection
	 * @param name name of collection in test
	 * @return a string containing the full initialization of that container
	 */
	public static StringBuilder declaredFilledContainer(final String prefix, final Collection<? extends ConstructableInUnitTest> value, final Class<?> container, final Class<?> content, final String name) {
		return declaredFilledContainer(prefix, value, container, content, value.getClass(), name, "add");
	}

	/**
	 * Declare a collection and add all elements given in with the given add method
	 * @param prefix prefix for correct alignment
	 * @param value source value used to initialize container
	 * @param container container type which will be used in test
	 * @param content content type declared in collection
	 * @param name name of collection in test
	 * @param addMethodName name of the method used to add elements. They are added one by one.
	 * @return a string containing the full initialization of that container
	 */
	public static StringBuilder declaredFilledContainer(final String prefix, final Collection<? extends ConstructableInUnitTest> value, final Class<?> container, final Class<?> content, final Class<?> containerType, final String name, final String addMethodName) {
		final StringBuilder returned = new StringBuilder();
		returned.append(prefix).append(declareElement(value, container, content, containerType, name));
		returned.append("\n");
		final String nextPrefix = prefix+"\t";
		for(final ConstructableInUnitTest t : value) {
			returned.append(nextPrefix).append(name).append(".").append(addMethodName).append("(").append(t.toUnitTestConstructor(nextPrefix)).append(");\n");
		}
		return returned;
	}

}
