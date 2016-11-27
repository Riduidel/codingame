package org.ndx.codingame.gaming;

import java.util.Collection;

public class ToUnitTest {
	public static StringBuilder declareCollection(Collection<?> collection, Class<?> declaredInterfaceType, Class<?> declaredContent, String name) {
		StringBuilder returned = new StringBuilder();
		returned.append(declaredInterfaceType.getSimpleName()).append("<").append(declaredContent.getName()).append("> ").append(name);
		returned.append("=");
		returned.append("new ").append(collection.getClass().getSimpleName()).append("<>();");
		return returned;
	}
}
