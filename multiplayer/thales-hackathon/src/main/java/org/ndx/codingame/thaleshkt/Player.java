package org.ndx.codingame.thaleshkt;

import java.util.Scanner;

import org.ndx.codingame.gaming.tounittest.ToUnitTestStringBuilder;
import org.ndx.codingame.thaleshkt.entities.Flag;
import org.ndx.codingame.thaleshkt.entities.Gamer;
import org.ndx.codingame.thaleshkt.entities.UFO;
import org.ndx.codingame.thaleshkt.playground.Participant;
import org.ndx.codingame.thaleshkt.playground.Playfield;
import org.ndx.codingame.thaleshkt.playground.Side;
import org.ndx.codingame.thaleshkt.status.CanBoost;
import org.ndx.codingame.thaleshkt.status.MySide;
import org.ndx.codingame.thaleshkt.status.ThalesStatus;

/**
 * Auto-generated code below aims at helping you parse the standard input
 * according to the problem statement.
 **/
public class Player {

	public static void main(String args[]) {
		Scanner in = new Scanner(System.in);

		ThalesStatus status = new ThalesStatus(); 
		status.set(new CanBoost());
		// game loop
		while (true) {
			Playfield play = new Playfield(status);
			// Position de notre drapeau
			play.my.flag = new Flag(in.nextInt(), in.nextInt());
			// Position du drapeau adversaire
			play.adversary.flag = new Flag(in.nextInt(), in.nextInt());
			if(!status.containsKey(MySide.class)) {
				status.set(new MySide(play.findMySide()));
			}
			// Position de MES bonhommes
			play.my.first = new UFO(
					Participant.MY,
					Gamer.FIRST,
					// Position
					in.nextInt(), in.nextInt(), 
					// Vitesse
					in.nextInt(), in.nextInt(), 
					// Est-ce que j'ai le drapeau ?
					in.nextInt());
			play.my.second = new UFO(
					Participant.MY,
					Gamer.SECOND,
					// Position
					in.nextInt(), in.nextInt(), 
					// Vitesse
					in.nextInt(), in.nextInt(), 
					// Est-ce que j'ai le drapeau ?
					in.nextInt());
			// Position des bonhommes enemis
			play.adversary.first = new UFO(
					Participant.ADVERSARY,
					Gamer.FIRST,
					// Position
					in.nextInt(), in.nextInt(), 
					// Vitesse
					in.nextInt(), in.nextInt(), 
					// Est-ce que j'ai le drapeau ?
					in.nextInt());
			play.adversary.second = new UFO(
					Participant.ADVERSARY,
					Gamer.SECOND,
					// Position
					in.nextInt(), in.nextInt(), 
					// Vitesse
					in.nextInt(), in.nextInt(), 
					// Est-ce que j'ai le drapeau ?
					in.nextInt());
			String effectiveCommand = play.compute();
			System.err.println(ToUnitTestStringBuilder.canComputeAt(play, effectiveCommand));
			System.out.println(effectiveCommand);
		}
	}
}