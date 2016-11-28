package org.ndx.codingame.fantastic;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.ndx.codingame.fantastic.entities.Bludger;
import org.ndx.codingame.fantastic.entities.Entity;
import org.ndx.codingame.fantastic.entities.EntityVisitor;
import org.ndx.codingame.fantastic.entities.Snaffle;
import org.ndx.codingame.fantastic.entities.Wizard;
import org.ndx.codingame.fantastic.status.Status;
import org.ndx.codingame.gaming.ToUnitTest;
import org.ndx.codingame.lib2d.Geometry;
import org.ndx.codingame.lib2d.Segment;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;

public class Playground {
	public static final int WIDTH = 16001;
	public static final int HEIGHT = 7501;
	public static final double GOAL_SIZE = 4000;
	
	public static final List<ContinuousPoint> poles = Arrays.asList(
			Geometry.at(0, HEIGHT/2-GOAL_SIZE/2),
			Geometry.at(0, HEIGHT/2+GOAL_SIZE/2),
			Geometry.at(WIDTH, HEIGHT/2-GOAL_SIZE/2),
			Geometry.at(WIDTH, HEIGHT/2+GOAL_SIZE/2)
			);
	
	public static final List<Segment> goals = Arrays.asList(
		Geometry.from(poles.get(0)).segmentTo(poles.get(1)),
		Geometry.from(poles.get(2)).segmentTo(poles.get(3))
	);
	
	private static EntityVisitor<String> ELEMENT_WRITER = new EntityVisitor<String>() {
		
		@Override
		public String visitWizard(Wizard wizard) {
			return String.format("new Wizard(%d, %d, %d, %d, %d, %d, %s, %s)", 
					wizard.id,
					(int) wizard.direction.first.x,
					(int) wizard.direction.first.y,
					(int) wizard.direction.second.x,
					(int) wizard.direction.second.y,
					wizard.teamId,
					wizard.holdingSnaffle,
					wizard.isAttacking());
		}
		
		@Override
		public String visitSnaffle(Snaffle snaffle) {
			return String.format("new Snaffle(%d, %d, %d, %d, %d)", 
					snaffle.id,
					(int) snaffle.direction.first.x,
					(int) snaffle.direction.first.y,
					(int) snaffle.direction.second.x,
					(int) snaffle.direction.second.y);
		}
		
		@Override
		public String visitBludger(Bludger bludger) {
			return String.format("new Bludger(%d, %d, %d, %d, %d)", 
					bludger.id,
					(int) bludger.direction.first.x,
					(int) bludger.direction.first.y,
					(int) bludger.direction.second.x,
					(int) bludger.direction.second.y);
		}
	};

	public static String toUnitTestString(Status status, List<Entity> playing, List<Wizard> myTeam) {
		String METHOD_PREFIX = "\t\t\t"; 
		String CONTENT_PREFIX = METHOD_PREFIX+"\t";
		final StringBuilder returned = new StringBuilder();
		returned.append(METHOD_PREFIX).append("@Test public void can_find_actions_in_")
			.append(System.currentTimeMillis()).append("() {\n");
		returned.append(CONTENT_PREFIX).append("Status status = new Status();\n");
		returned.append(CONTENT_PREFIX).append("\t").append("status.setTeam(").append(status.getTeam()).append(");\n");
		returned.append(CONTENT_PREFIX).append("\t").append("status.setMagic(").append(status.getMagic()).append(");\n");
		returned.append(CONTENT_PREFIX).append(ToUnitTest.declareCollection(playing, List.class, Entity.class, "playing")).append("\n");
		for (Entity entity : playing) {
			String adding = entity.accept(ELEMENT_WRITER);
			returned.append(CONTENT_PREFIX).append("\t").append("playing.add(").append(adding).append(");\n");
		}
		returned.append(CONTENT_PREFIX).append(ToUnitTest.declareCollection(myTeam, List.class, Wizard.class, "myTeam")).append("\n");
		for (Entity entity : myTeam) {
			String adding = entity.accept(ELEMENT_WRITER);
			returned.append(CONTENT_PREFIX).append("\t").append("myTeam.add(").append(adding).append(");\n");
		}
		returned.append(CONTENT_PREFIX).append("// TODO Write that test !\n");
		returned.append(CONTENT_PREFIX).append("for(Wizard my : myTeam) {\n");
		returned.append(CONTENT_PREFIX).append("\t").append("my.play(status, playing, myTeam);\n");
		returned.append(CONTENT_PREFIX).append("}\n");
		returned.append(METHOD_PREFIX).append("}\n\n");
		return returned.toString();
	}
}
