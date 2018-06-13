package org.ndx.codingame.thaleshkt;

/**
 * Elements that should be customized for better behaviour
 * @author nicolas-delsaux
 *
 */
public interface MetaParameters {
	public static final int FLAG_OFFSET = 0;
	public static final int COLLISION_HORIZON = 10;
	public static final int AFTER_HORIZON = COLLISION_HORIZON+COLLISION_HORIZON/2;
	public static final int ORTHOGONAL_POINTS = Constants.UFO_RADIUS*3; 

}
