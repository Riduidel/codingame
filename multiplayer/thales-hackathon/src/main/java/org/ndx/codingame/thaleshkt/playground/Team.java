package org.ndx.codingame.thaleshkt.playground;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.ndx.codingame.thaleshkt.actions.Move;
import org.ndx.codingame.thaleshkt.actions.MoveType;
import org.ndx.codingame.thaleshkt.entities.Flag;
import org.ndx.codingame.thaleshkt.entities.UFO;

public class Team {
	public Flag flag;
	public UFO first;
	public UFO second;
	private Participant participant;
	public Team(Participant p) {
		this.participant = p;
	}
	public String compute(Playfield playfield) {
		Move firstMove;
		Move secondMove;
		if(first.hasFlag) {
			firstMove = first.computeComeback(playfield);
			secondMove = second.computeDefense(playfield);
		} else if (second.hasFlag) {
			firstMove = first.computeDefense(playfield);
			secondMove = second.computeComeback(playfield);
		} else {
			Map<MoveType, Move> firstMovesMap = first.computeMoves(playfield);
			Map<MoveType, Move> secondMovesMap = second.computeMoves(playfield);
			SortedMap<Double, List<Move>> shortestMove = new TreeMap<>();
			List<List<MoveType>> crossed = Arrays.asList(
					Arrays.asList(MoveType.ATTACK, MoveType.DEFENSE),
					Arrays.asList(MoveType.DEFENSE, MoveType.ATTACK)
					);
			for(List<MoveType> actions : crossed) {
				Move f = firstMovesMap.get(actions.get(0));
				Move s = secondMovesMap.get(actions.get(1));
				double totalDistance = f.moving.position.center.distance2To(f.destination) +
						s.moving.position.center.distance2To(s.destination);
				shortestMove.put(totalDistance, Arrays.asList(f, s));
			}
			Double key = shortestMove.firstKey();
			List<Move> selected = shortestMove.get(key);
			firstMove = selected.get(0);
			secondMove = selected.get(1);
		}
		// Don't forget to update boost status accordingly
		return firstMove.toString()+"\n"+secondMove.toString();  
	}
}