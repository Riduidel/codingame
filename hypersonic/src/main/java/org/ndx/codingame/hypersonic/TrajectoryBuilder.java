package org.ndx.codingame.hypersonic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.ndx.codingame.gaming.Delay;

public class TrajectoryBuilder {
	public static final Random random = new Random();
	public final Playground source;
	public final Delay delay;
	public final EvolvableConstants constants;
	public int count;
	public TrajectoryBuilder(Playground source, Delay delay, EvolvableConstants constants) {
		super();
		this.source = source;
		this.delay = delay;
		this.constants = constants;
	}
	public Trajectory findBest(Gamer me) {
		count = 0;
		Trajectory returned = null, tested;
		while(!delay.isElapsed(constants.DELAY_CREATE_TRAJECTORIES) 
				&& count<=constants.COUNT_ENOUGH_TRAJECTORIES) {
			tested = build(me, count);
			if(returned==null) {
				returned = tested;
			} else {
				if(returned.score<tested.score)
					returned = tested;
			}
			count++;
		}
		System.err.println(String.format("Tested %d trajectories", count));
		return returned;
	}
	public Trajectory build(Gamer me, int count) {
		Trajectory returned = new Trajectory();
		Entity current = me;
		Playground playground = source;
		int length = 0;
		int speedBonus = constants.HORIZON;
		while(!delay.isElapsed(constants.DELAY_CREATE_TRAJECTORIES)
				&& length<EvolvableConstants.HORIZON) {
			Step next = createStep(current, me, playground, returned.steps.size(), count);
			if(next==null) {
				break;
			}
			returned.add(next, speedBonus);
			current = next;
			playground = next.playground;
			length++;
			speedBonus--;
		}
		return returned;
	}
	public Step createStep(Entity current, Gamer me, Playground playground, int time, int count) {
		// We test positions on default next playground, whcih gives a good idea of what will happen
		Playground test = playground.next();
		int[][] availableDirections = test.getAvailableDirectionsAt(current);
		int[] directions = null;
		if(availableDirections.length==0) {
			// Too bad, I'm dead !
			directions = new int[] {current.x, current.y};
		} else {
			int randomDirection = random.nextInt(availableDirections.length);
			directions = availableDirections[randomDirection]; 
		}
		List<Action> availableActions = new ArrayList<>(2);
		availableActions.add(Action.MOVE);
		// Now we have our directions, check what actions are available
		if(me.bombs>0) {
			// OK, gamer can bomb. But should he bomb ?
			// We assume yes
			availableActions.add(Action.BOMB);
		}
		int randomAction = random.nextInt(availableActions.size());
		Action nextAction = availableActions.get(randomAction);
		return playground.getStep(nextAction, directions[0], directions[1], me, current, constants);
	}
}