package org.ndx.codingame.thaleshkt.playground;

import org.ndx.codingame.gaming.tounittest.ToUnitTestFiller;
import org.ndx.codingame.gaming.tounittest.ToUnitTestHelpers;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.thaleshkt.Constants;
import org.ndx.codingame.thaleshkt.status.ThalesStatus;

public class Playfield implements ToUnitTestFiller {
	public final Team my = new Team();
	public final Team adversary = new Team();
	public final ThalesStatus status;
	public Playfield(ThalesStatus status) {
		this.status = status;
	}
	public String compute() {
		return my.compute(this);
	}
	public Side findMySide() {
		return my.flag.position.center.x<Constants.WIDTH/2 ? Side.LEFT : Side.RIGHT;
	}
	public StringBuilder build(final String effectiveCommand) {
		final StringBuilder returned = new StringBuilder();
		returned.append(status.toUnitTestConstructor(ToUnitTestHelpers.CONTENT_PREFIX));
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("Playfield p = new Playfield();\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("p.my.first = ").append(my.first.toUnitTestConstructor("")).append(";\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("p.my.second = ").append(my.second.toUnitTestConstructor("")).append(";\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append(my.flag.toUnitTestConstructor("p.my.flag = "));
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("p.enemy.first = ").append(adversary.first.toUnitTestConstructor("")).append(";\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("p.enemy.second = ").append(adversary.second.toUnitTestConstructor("")).append(";\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append(adversary.flag.toUnitTestConstructor("p.enemy.flag = "));
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX)
		.append("assertThat(p.compute()).isNotEqualTo(\"")
		.append(effectiveCommand.replace(
				"\n",
				"\\n\"+\n"+ToUnitTestHelpers.CONTENT_PREFIX+"\""))
		.append("\");\n");
		return returned;
	}
	
	public ContinuousPoint getPositionOfFlag(Participant p) {
		Team ofParticipant = p.getParticipant(this);
		Team ofAdversary = p.getAdversary(this);
		if(ofParticipant.flag.position.center.x>=0) {
			return ofParticipant.flag.position.center;
		} else {
			if(ofAdversary.first.hasFlag) {
				return ofAdversary.first.position.center;
			} else {
				return ofAdversary.second.position.center;
			}
		}
	}
}
