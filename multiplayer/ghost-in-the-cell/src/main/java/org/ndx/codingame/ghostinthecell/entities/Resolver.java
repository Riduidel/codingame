package org.ndx.codingame.ghostinthecell.entities;

public interface Resolver<Type extends Attack> {
	public Type resolvedAttack(int owner, int count);
}