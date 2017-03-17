package org.ndx.codingame.codevszombies.playground;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedMap;
import java.util.stream.Collectors;

import org.ndx.codingame.codevszombies.entities.Ash;
import org.ndx.codingame.codevszombies.entities.Entity;
import org.ndx.codingame.codevszombies.entities.Human;
import org.ndx.codingame.codevszombies.entities.Zombie;
import org.ndx.codingame.gaming.tounittest.ToUnitTestHelpers;
import org.ndx.codingame.gaming.tounittest.ToUnitTestStringBuilder;
import org.ndx.codingame.lib2d.Geometry;
import org.ndx.codingame.lib2d.continuous.ContinuousPoint;
import org.ndx.codingame.lib2d.shapes.Segment;
import org.ndx.codingame.lib2d.shapes.Vector;

public class Playfield {
	public static final int WIDTH = 16000;
	public static final int HEIGHT = 9000;
	private Collection<Human> humans = new ArrayList<>();
	private Collection<Zombie> zombies = new ArrayList<>();
	private Ash ash;
	
	public Playfield() {
		
	}

	public Playfield(final List<Human> humans, final List<Zombie> zombies, final Ash ash) {
		this.humans = humans;
		this.zombies = zombies;
		this.ash = ash;
	}

	public void add(final Ash ash) {
		this.ash = ash;
	}

	public void add(final Human entity) {
		humans.add(entity);
	}

	public void add(final Zombie entity) {
		zombies.add(entity);
	}

	public String toUnitTestString() {
		return new ToUnitTestStringBuilder("can_find_move").build(this::fillTest);
	}
	
	public StringBuilder fillTest() {
		final StringBuilder returned = new StringBuilder();
		final String PLAYFIELD = "playfield";
		final List<Entity> entities = new ArrayList<>(humans);
		entities.addAll(zombies);
		returned.append(ToUnitTestHelpers.declaredFilledContainer(ToUnitTestHelpers.CONTENT_PREFIX, 
				entities, 
				Playfield.class, 
				Object.class, 
				Playfield.class, PLAYFIELD, "add"));
		returned.append(ToUnitTestHelpers.CONTENT_PREFIX)
			.append(PLAYFIELD).append(".add(")
			.append(ash.toUnitTestConstructor(ToUnitTestHelpers.CONTENT_PREFIX)).append(");\n");
		return returned;
	}

	public String compute() {
		return toCommandString(ash.findBestMoveOn(this));
	}

	private String toCommandString(final ContinuousPoint point) {
		return String.format("%d %d", (int) point.x, (int) point.y);
	}

	public Collection<Human> getHumans() {
		return humans;
	}

	public Collection<Zombie> getZombies() {
		return zombies;
	}

	public ContinuousPoint center() {
		return new ContinuousPoint(WIDTH/2, HEIGHT/2);
	}

	public List<Playfield> predictAtLeast(final int turns) {
		final List<Playfield> returned = new ArrayList<>();
		returned.add(this);
		for (int index = 0; index < turns; index++) {
			returned.add(returned.get(index).advanceOneTurn());
		}
		return returned;
	}

	private Playfield advanceOneTurn() {
		final List<Human> nextHumans = humans.stream()
				.map((human)-> human.advanceOneTurn())
				.collect(Collectors.toList())
						;
		final List<Zombie> nextZombies = zombies.stream()
			.map((zombie) -> advanceZombie(zombie, nextHumans))
			.collect(Collectors.toList());
		return new Playfield(nextHumans, nextZombies, ash);
	}

	public Zombie advanceZombie(final Zombie toAdvance, final Collection<Human> targets) {
		final SortedMap<Double, Human> sortedTargets = toAdvance.byDistanceTo(targets);
		final List<Double> distances = new ArrayList<>(sortedTargets.keySet());
		distances.stream()
			.filter((d) -> d<Zombie.SPEED)
			.forEach((d) -> targets.remove(sortedTargets.remove(d)));
		if(sortedTargets.isEmpty()) {
			return new Zombie(toAdvance.getId(), new Vector(toAdvance.getPosition(), toAdvance.getPosition()));
		} else {
			final Double targetDistance = sortedTargets.firstKey();
			final Human realTarget = sortedTargets.get(targetDistance);
			final Segment segmentToTarget = Geometry.from(toAdvance.getPosition()).segmentTo(realTarget.getPosition());
			final ContinuousPoint firstPoint = segmentToTarget.pointAtDistance(Zombie.SPEED, segmentToTarget.first);
			final ContinuousPoint secondPoint = segmentToTarget.pointAtDistance(
					Math.min(Zombie.SPEED*2, toAdvance.getPosition().distance2To(realTarget.getPosition())), 
					segmentToTarget.first);
			return new Zombie(toAdvance.getId(), new Vector(firstPoint, secondPoint));
		}
	}

	@Override
	public String toString() {
		return String.format("Playfield [\n===========humans===========\n%s\n===========zombies===========\n%s]", 
				humans.stream().map((h)->h.toString()).collect(Collectors.joining("\n")), 
				zombies.stream().map((h)->h.toString()).collect(Collectors.joining("\n")));
	}
}
