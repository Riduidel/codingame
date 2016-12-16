package org.ndx.codingame.coders_strike_back.actions;

import java.util.Collection;

import org.ndx.codingame.lib2d.shapes.Line;

public interface Trajectory {

	String build(boolean canBoost, boolean canShield, Collection<Line> enemyDirections);
}
