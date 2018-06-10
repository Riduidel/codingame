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
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append("Playfield p = new Playfield(status);\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append(my.first.toUnitTestConstructor("p.my.first = ")).append(";\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append(my.second.toUnitTestConstructor("p.my.second = ")).append(";\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append(my.flag.toUnitTestConstructor("p.my.flag = ")).append(";\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append(adversary.first.toUnitTestConstructor("p.adversary.first = ")).append(";\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append(adversary.second.toUnitTestConstructor("p.adversary.second = ")).append(";\n");
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX).append(adversary.flag.toUnitTestConstructor("p.adversary.flag = ")).append(";\n");
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
