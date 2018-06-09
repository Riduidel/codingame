package org.ndx.codingame.thaleshkt.playground;

import org.ndx.codingame.thaleshkt.entities.Flag;
import org.ndx.codingame.thaleshkt.entities.UFO;

public class Team {
	public Flag flag;
	public UFO first;
	public UFO second;
	public String compute(Playfield playfield) {
		return first.compute(playfield)+"\n"+second.compute(playfield);  
	}
}