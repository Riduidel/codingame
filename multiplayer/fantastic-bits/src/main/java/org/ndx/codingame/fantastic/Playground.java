package org.ndx.codingame.fantastic;

import java.util.Arrays;
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
	
	public static final ContinuousPoint A = new ContinuousPoint(0, 0),
			B = new ContinuousPoint(WIDTH, 0),
			C = new ContinuousPoint(0, HEIGHT),
			D = new ContinuousPoint(WIDTH, HEIGHT);
	public static final Segment TOP = new Segment(A, B);
	public static final Segment BOTTOM = new Segment(C, D);
	
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
		public String visitWizard(final Wizard entity) {
			return String.format("new Wizard(%d, %d, %d, %d, %d, %d, %s, %s)", 
					entity.id,
					(int) entity.position.x,
					(int) entity.position.y,
					(int) entity.speed.x,
					(int) entity.speed.y,
					entity.teamId,
					entity.holdingSnaffle,
					entity.isAttacking());
		}
		
		@Override
		public String visitSnaffle(final Snaffle entity) {
			return String.format("new Snaffle(%d, %d, %d, %d, %d)", 
					entity.id,
					(int) entity.position.x,
					(int) entity.position.y,
					(int) entity.speed.x,
					(int) entity.speed.y
					);
		}
		
		@Override
		public String visitBludger(final Bludger entity) {
			return String.format("new Bludger(%d, %d, %d, %d, %d)", 
					entity.id,
					(int) entity.position.x,
					(int) entity.position.y,
					(int) entity.speed.x,
					(int) entity.speed.y
					);
		}
	};

	public static String toUnitTestString(final Status status, final List<Entity> playing, final List<Wizard> myTeam) {
		final String METHOD_PREFIX = "\t\t\t"; 
		final String CONTENT_PREFIX = METHOD_PREFIX+"\t";
		final StringBuilder returned = new StringBuilder();
		returned.append(METHOD_PREFIX).append("@Test public void can_find_actions_in_")
			.append(System.currentTimeMillis()).append("() {\n");
		returned.append(CONTENT_PREFIX).append("Status status = new Status();\n");
		returned.append(CONTENT_PREFIX).append("\t").append("status.setTeam(").append(status.getTeam()).append(");\n");
		returned.append(CONTENT_PREFIX).append("\t").append("status.setMagic(").append(status.getMagic()).append(");\n");
		returned.append(CONTENT_PREFIX).append(ToUnitTest.declareCollection(playing, List.class, Entity.class, "playing")).append("\n");
		for (final Entity entity : playing) {
			final String adding = entity.accept(ELEMENT_WRITER);
			returned.append(CONTENT_PREFIX).append("\t").append("playing.add(").append(adding).append(");\n");
		}
		returned.append(CONTENT_PREFIX).append(ToUnitTest.declareCollection(myTeam, List.class, Wizard.class, "myTeam")).append("\n");
		for (final Entity entity : myTeam) {
			final String adding = entity.accept(ELEMENT_WRITER);
			returned.append(CONTENT_PREFIX).append("\t").append("myTeam.add(").append(adding).append(");\n");
		}
		returned.append(CONTENT_PREFIX).append("final Entities entitiesStore = new Entities(status, playing, myTeam, status.get(TeamStatus.class).getAttacked(), status.get(TeamStatus.class).getDefended());\n");
		returned.append(CONTENT_PREFIX).append("final List<String> actions = entitiesStore.computeActionsToString();\n");
		returned.append(CONTENT_PREFIX).append("assertThat(actions).hasSize(2);\n");
		returned.append(METHOD_PREFIX).append("}\n");
		return returned.toString();
	}
}
