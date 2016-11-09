package org.ndx.codingame.coders_strike_back;

import java.util.Collection;

import org.ndx.codingame.lib2d.Line;

public interface Trajectory {

	String build(boolean canBoost, boolean canShield, Collection<Line> enemyDirections);
}
