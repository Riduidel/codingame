package org.ndx.codingame.fall2026;

import java.util.*;

import org.ndx.codingame.fall2026.entities.House;
import org.ndx.codingame.fall2026.playground.Playfield;

import java.io.*;
import java.math.*;

public class Player {

    public static void main(String args[]) {
    	Playfield playfield = new Playfield();
        Scanner in = new Scanner(System.in);
        int houseCount = in.nextInt();
        for (int i = 0; i < houseCount; i++) {
            int houseId = in.nextInt();
            int houseX = in.nextInt();
            int houseY = in.nextInt();
            playfield.add(new House(houseX, houseY, houseId));
        }
        // game loop
        while (true) {
        	playfield = playfield.advance();
            int myScore = in.nextInt();
            int opponentScore = in.nextInt();
            for (int i = 0; i < houseCount; i++) {
                int houseId = in.nextInt();
                int myUnits = in.nextInt();
                int opponentUnits = in.nextInt();
                playfield.install(houseId, myUnits, opponentUnits);
            }
            int pathCount = in.nextInt();
            for (int i = 0; i < pathCount; i++) {
                int house1 = in.nextInt();
                int house2 = in.nextInt();
                playfield.registerConnectedHouses(house1, house2);
            }
            int triangleCount = in.nextInt();
            for (int i = 0; i < triangleCount; i++) {
                int house1 = in.nextInt();
                int house2 = in.nextInt();
                int house3 = in.nextInt();
                int owner = in.nextInt();
                boolean meCanCapture = in.nextInt() != 0;
                boolean opponentCanCapture = in.nextInt() != 0;
                playfield.addTriangle(house1, house2, house3, owner, meCanCapture, opponentCanCapture);
            }
            int linkableCount = in.nextInt();
            for (int i = 0; i < linkableCount; i++) {
                int house1 = in.nextInt();
                int house2 = in.nextInt();
                playfield.registerConnectableHouses(house1, house2);
            }

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            System.out.println("MOVE 3 17 1");
        }
    }
}