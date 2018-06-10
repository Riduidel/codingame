package org.ndx.codingame.thaleshkt.playground;

import org.ndx.codingame.thaleshkt.actions.Move;
import org.ndx.codingame.thaleshkt.entities.Flag;
import org.ndx.codingame.thaleshkt.entities.UFO;
import org.ndx.codingame.thaleshkt.status.CanBoost;

public class Team {
	public Flag flag;
	public UFO first;
	public UFO second;
	public String compute(Playfield playfield) {
		Move firstMove = first.compute(playfield);
		Move secondMove = second.compute(playfield);
		playfield.status.get(CanBoost.class).first.update(firstMove);
		playfield.status.get(CanBoost.class).second.update(secondMove);
		return firstMove.toString()+"\n"+secondMove.toString();  
	}
}