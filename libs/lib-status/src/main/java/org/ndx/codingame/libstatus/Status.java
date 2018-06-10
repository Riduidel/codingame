package org.ndx.codingame.libstatus;

import java.util.HashMap;
import java.util.Map;

import org.ndx.codingame.gaming.tounittest.ConstructableInUnitTest;

/**
 * Ne pas oublier que cette classe peut (et en fait doit) être étendue
 * @author nicolas-delsaux
 *
 */
public class Status implements ConstructableInUnitTest {
	private final Map<Class<? extends StatusElement>, StatusElement> elements = new HashMap<>();
	
	public void set(final StatusElement s) {
		elements.put(s.getClass(), s);
	}
	
	public <Type extends StatusElement> Type get(final Class<Type> type) {
		return type.cast(elements.get(type));
	}
	
	public void advanceOneTurn() {
		for(final StatusElement e : elements.values()) {
			e.advanceOneTurn();
		}
	}

	public boolean containsKey(Object key) {
		return elements.containsKey(key);
	}

	public void setIfNeeded(final StatusElement element) {
		if(!containsKey(element.getClass())) {
			set(element);
		}
	}

	@Override
	public StringBuilder toUnitTestConstructor(String multilinePrefix) {
		StringBuilder returned = new StringBuilder(multilinePrefix);
		returned.append(getClass().getSimpleName()).append(" status = new ").append(getClass().getSimpleName()).append("();\n");
		for(final StatusElement e : elements.values()) {
			returned.append(multilinePrefix)
				.append("status.set(").append(e.toUnitTestConstructor(multilinePrefix)).append(");\n");
		}
		return returned;
	}
}
