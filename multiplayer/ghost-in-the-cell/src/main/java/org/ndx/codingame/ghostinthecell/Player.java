package org.ndx.codingame.ghostinthecell;
import java.util.Scanner;

import org.ndx.codingame.ghostinthecell.playground.Playfield;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
public class Player {
    public static void main(final String args[]) {
        final Scanner in = new Scanner(System.in);
        final Playfield playground = new Playfield();
        final int factoryCount = in.nextInt(); // the number of factories
        final int linkCount = in.nextInt(); // the number of links between factories
        for (int i = 0; i < linkCount; i++) {
            final int factory1 = in.nextInt();
            final int factory2 = in.nextInt();
            final int distance = in.nextInt();
            playground.connect(factory1, factory2, distance);
        }

        // game loop
        while (true) {
            final int entityCount = in.nextInt(); // the number of entities (e.g. factories and troops)
            for (int i = 0; i < entityCount; i++) {
                final int entityId = in.nextInt();
                final String entityType = in.next();
                final int arg1 = in.nextInt();
                final int arg2 = in.nextInt();
                final int arg3 = in.nextInt();
                final int arg4 = in.nextInt();
                final int arg5 = in.nextInt();
                switch(entityType) {
                case "FACTORY":
                	playground.setFactoryInfos(entityId, arg1, arg2, arg3);
                	break;
                case "TROOP":
                	playground.setTroop(arg2, arg3, arg1, arg4, arg5);
                	break;
                }
            }

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");
            System.err.println(playground.toDebugString());

            // Any valid action, such as "WAIT" or "MOVE source destination cyborgs"
            System.out.println(playground.compute());
            
            playground.cleanup();
        }
    }
}