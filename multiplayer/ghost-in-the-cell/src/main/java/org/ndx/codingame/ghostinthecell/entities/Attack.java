package org.ndx.codingame.ghostinthecell.entities;

public class Attack {
	public static final Resolver<Attack> DEFAULT = new Resolver<Attack>() {

		@Override
		public Attack resolvedAttack(final int owner, final int count) {
			return new Attack(owner, count);
		}
		
	};
	
	public final int owner;
	private int count;
	public Attack(final int owner, final int count) {
		super();
		this.owner = owner;
		this.count = count;
	}
	public Attack resolve(final Attack attack) {
		return resolve(attack, DEFAULT);
	}
	public <Type extends Attack> Type resolve(final Attack attack, final Resolver<Type> resolver) {
		int count = this.count;
		int owner = this.owner;
		if(attack.owner==owner) {
			count+=attack.count;
		} else {
			count-=attack.count;
			if(count<0) {
				owner = attack.owner;
				count = Math.abs(count);
			}
		}
		return resolver.resolvedAttack(owner, count);
	}
	public boolean isMine() {
		return owner>0;
	}

	public boolean isEnemy() {
		return owner<0;
	}
	public int getCount() {
		return count;
	}
	public void setCount(final int count) {
		this.count = count;
	}
}
