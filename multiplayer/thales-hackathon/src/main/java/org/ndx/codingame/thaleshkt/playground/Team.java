package org.ndx.codingame.thaleshkt.playground;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

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
	public Collection<Move> compute(Playfield playfield) {
		Collection<Move> moves;
		if(first.hasFlag) {
			moves = Arrays.asList(first.computeComeback(playfield),
					second.computeDefense(playfield));
		} else if (second.hasFlag) {
			moves = Arrays.asList(first.computeDefense(playfield),
					second.computeComeback(playfield));
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
			moves = shortestMove.get(key);
		}
		// Don't forget to update boost status accordingly
		return moves;
	}
}