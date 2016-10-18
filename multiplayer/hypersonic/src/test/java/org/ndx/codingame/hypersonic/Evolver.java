package org.ndx.codingame.hypersonic;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.ndx.codingame.gaming.Delay;
import org.ndx.codingame.hypersonic.EvolvableConstants;
import org.ndx.codingame.hypersonic.Gamer;
import org.ndx.codingame.hypersonic.Playground;
import org.ndx.codingame.hypersonic.Trajectory;

public class Evolver {
	public static final int WIDTH = 13;
	public static final int HEIGHT = 11;
	public static final int INITIAL_BOMB = 1;
	public static final int INITIAL_RANGE = 3;
	public static final int NUMBER_OF_SOLUTIONS = 100;
	public static final int CONVERGENCE_LIMIT = 10;
	public static final Random random = new Random();
	private static final int SOLUTION_RANGE = 100;
	public static enum Status {
		ALIVE, DEAD
	}

	private static class EvolvableConstantsInfos implements Comparable<EvolvableConstantsInfos> {
		public final EvolvableConstants constants;
		/**
		 * Number of turns survived
		 */
		public long score;

		public EvolvableConstantsInfos(EvolvableConstants constants) {
			super();
			this.constants = constants;
		}

		@Override
		public int compareTo(EvolvableConstantsInfos arg0) {
			int returned = (int) Math.signum(score-arg0.score);
			if(returned==0) {
				returned = (int) Math.signum(hashCode()-arg0.hashCode());
			}
			if(returned==0) {
				for(Field f : constants.getClass().getFields()) {
					if(returned==0) {
						if(!Modifier.isStatic(f.getModifiers())) {
							if(Integer.class.equals(f.getType())) {
								try {
									returned = (int) Math.signum(
											((Integer) f.get(constants))-
											((Integer) f.get(arg0.constants)));
								} catch(Throwable t) {
									
								}
							}
						}
					}
				}
			}
			return 0;
		}

		public EvolvableConstantsInfos hybridate(EvolvableConstantsInfos second) throws Throwable {
			EvolvableConstants c = new EvolvableConstants();
			for(Field f : c.getClass().getFields()) {
				if(!Modifier.isStatic(f.getModifiers())) {
					if(Integer.class.equals(f.getType())) {
						f.set(c, 
								(((Integer) f.get(constants)) +
								((Integer) f.get(second.constants)))
								/2
								);
					}
				}
			}
			return new EvolvableConstantsInfos(c);
		}
	}
	private static void open(Class<?> evolvableClass) {
		for(Field f : evolvableClass.getFields()) {
			if(!Modifier.isStatic(f.getModifiers())) {
				f.setAccessible(true);
			}
		}
	}
	private static EvolvableConstantsInfos createRandomSolution() throws Throwable{
		EvolvableConstants solution =new EvolvableConstants();
		for(Field f : solution.getClass().getFields()) {
			if(!Modifier.isStatic(f.getModifiers())) {
				if(Integer.class.equals(f.getType())) {
					f.set(solution, random.nextInt(SOLUTION_RANGE)-SOLUTION_RANGE/2);
				}
			}
		}
		return new EvolvableConstantsInfos(solution);
	}
	private static boolean areConvergent(EvolvableConstantsInfos first,
			EvolvableConstantsInfos second) throws Throwable {
		return areConvergent(first.constants, second.constants);
	}
	private static boolean areConvergent(EvolvableConstants first,
			EvolvableConstants second) throws Throwable {
		int difference = getConvergenceLevel(first, second);
		return difference<CONVERGENCE_LIMIT;
	}
	private static int getConvergenceLevel(EvolvableConstantsInfos first,
			EvolvableConstantsInfos second) throws Throwable {
		return getConvergenceLevel(first.constants, second.constants);
	}
	private static int getConvergenceLevel(EvolvableConstants first, EvolvableConstants second)
			throws IllegalAccessException {
		int difference = 0;
		for(Field f : EvolvableConstants.class.getFields()) {
			if(!Modifier.isStatic(f.getModifiers())) {
				if(Integer.class.equals(f.getType())) {
					difference+=Math.abs(((Integer)f.get(first))-((Integer)f.get(second))); 
				}
			}
		}
		return difference;
	}
	public static void main(String[] args) throws Throwable {
		int count = 0;
		// make all fields accessible, then generate solutions at random
		open(EvolvableConstants.class);
		List<EvolvableConstantsInfos> constants = new ArrayList<>(NUMBER_OF_SOLUTIONS);
		for(int index = 0; index<NUMBER_OF_SOLUTIONS; index++) {
			constants.add(createRandomSolution());
		}
		// Now iterate until the first solutions are all with range
		while(!areConvergent(constants.get(0), constants.get(1))) {
			constants = runTournament(constants); count++;
			if(constants.size()>1)
				System.err.println(String.format("Tournament %d has convergence level of %d", count,
					getConvergenceLevel(constants.get(0), constants.get(1))));
		}
		showBestSolutions(constants);
		System.err.println(String.format("Convergence (difference of fields between two first values) is %d", 
				getConvergenceLevel(constants.get(0), constants.get(1))));
	}
	/**
	 * Run a tournament by playing each set of constants, then ordering them based upon their success loss rate
	 * @param constants
	 * @return
	 * @throws Throwable 
	 */
	private static List<EvolvableConstantsInfos> runTournament(List<EvolvableConstantsInfos> constants) throws Throwable {
		List<EvolvableConstantsInfos> infos = new ArrayList<>();
		// First, create the games
		for (EvolvableConstantsInfos evolvableConstantsInfos : constants) {
			evolvableConstantsInfos.score = score(evolvableConstantsInfos);
			if(evolvableConstantsInfos.score>0)
				infos.add(evolvableConstantsInfos);
		}
		// And finaly remove the bad player and mix the good ones
		Collections.sort(infos);
		Collections.reverse(infos);
		List<EvolvableConstantsInfos> returned = hybridate(infos);
		return returned;
	}
	private static void showBestSolutions(List<EvolvableConstantsInfos> infos) throws Throwable {
		StringBuilder returned = new StringBuilder();
		for(Field f : EvolvableConstants.class.getFields()) {
			if(!Modifier.isStatic(f.getModifiers())) {
				if(Integer.class.equals(f.getType())) {
					for (int index = 0; index < 1; index++) {
						EvolvableConstantsInfos solution = infos.get(index);
						returned.append(String.format("\t\tpublic Integer %s = %d;", f.getName(), f.get(solution.constants)));
					}
					returned.append("\n");
				}
			}
		}
		for (int index = 0; index < 2; index++) {
			EvolvableConstantsInfos solution = infos.get(index);
			returned.append(String.format("%25sscore = %d30;", "", solution.score));
		}
		returned.append("\n");
		System.err.println(returned);
	}
	private static List<EvolvableConstantsInfos> hybridate(List<EvolvableConstantsInfos> returned) throws Throwable {
		// Keep the first half
		returned = new ArrayList<>(returned.subList(0, returned.size()/2));
		List<EvolvableConstantsInfos> toAdd = new ArrayList<>();
		Iterator<EvolvableConstantsInfos> iterator = returned.iterator();
		while(iterator.hasNext()) {
			EvolvableConstantsInfos first = iterator.next();
			if(iterator.hasNext()) {
				EvolvableConstantsInfos second = iterator.next();
				EvolvableConstantsInfos child = first.hybridate(second);
				toAdd.add(child);
			}
		}
		returned.addAll(toAdd);
		while(returned.size()<NUMBER_OF_SOLUTIONS) {
			returned.add(createRandomSolution());
		}
		return returned;
	}
	
	public static int score(EvolvableConstantsInfos infos) {
		Delay delay = new TestDelay(1000000);
//		Delay delay = new Delay();
		Playground tested = PlayerTest.read(Arrays.asList(
			"....00.0.....",
			".X.X0X0X.X.X.",
			"000000.0.0000",
			"0X.X0X.X.X.X0",
			"0.00..0..00.0",
			".X0X0X0X.X0X.",
			"0.00..0..00.0",
			"0X.X0X.X.X.X0",
			"000000.0.0000",
			".X.X0X0X.X.X.",
			"....00.0....."
			));
		Gamer me = new Gamer(0, 0, 0, 1, 3);
		tested.readGameEntities(
			new Gamer(1, 12, 10, 1, 3)
			);
		Trajectory stupidOne = createStupidTrajectory(infos, delay, tested, me);
		Trajectory dumbOne = createDumbTrajectory(infos, delay, tested, me);
		Trajectory suicideOne = createSuicideTrajectory(infos, delay, tested, me);
		Trajectory smart = createSmartTrajectory(infos, delay, tested, me);
		return smart.score*10+dumbOne.score-stupidOne.score*10-suicideOne.score*1000;
	}
	
	
	public static enum Trajectories {
		SMART, DUMB, STUPID, SUICIDE
	}
	
	private static Trajectory createSuicideTrajectory(EvolvableConstantsInfos infos, Delay delay,
			Playground tested, Gamer me) {
		return PlayerTest.TrajectoryTest.builder(tested, delay, infos.constants)
				.move(1, 0)
				.bomb(0, 0)
				.move(0, 1)
				.move(0, 1)
				.move(0, 0)
				.move(0, 0)
				.move(0, 0)
				.move(0, 0)
				.move(0, 0)
				.move(0, 0)
				.move(0, 0)
				.build(me, 0);
	}
	private static Trajectory createSmartTrajectory(EvolvableConstantsInfos infos, Delay delay,
			Playground tested, Gamer me) {
		return PlayerTest.TrajectoryTest.builder(tested, delay, infos.constants)
				.move(1, 0)
				.bomb(0, 0)
				.move(0, 1)
				.move(0, 1)
				.move(0, 1)
				.move(0, 1)
				.move(0, 1)
				.move(0, 1)
				.move(0, 1)
				.move(0, 1)
				.build(me, 0);
	}
	private static Trajectory createStupidTrajectory(EvolvableConstantsInfos infos, Delay delay,
			Playground tested, Gamer me) {
		return PlayerTest.TrajectoryTest.builder(tested, delay, infos.constants)
				.bomb(0, 0)
				.move(0, 0)
				.move(0, 0)
				.move(0, 0)
				.move(0, 0)
				.move(0, 0)
				.move(0, 0)
				.move(0, 0)
				.move(0, 0)
				.move(0, 0)
				.build(me, 0);
	}
	private static Trajectory createDumbTrajectory(EvolvableConstantsInfos infos, Delay delay,
			Playground tested, Gamer me) {
		return PlayerTest.TrajectoryTest.builder(tested, delay, infos.constants)
				.move(0, 0)
				.move(0, 0)
				.move(0, 0)
				.move(0, 0)
				.move(0, 0)
				.move(0, 0)
				.move(0, 0)
				.move(0, 0)
				.move(0, 0)
				.move(0, 0)
				.build(me, 0);
	}
}
