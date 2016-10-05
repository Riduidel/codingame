package org.ndx.codingame.the_accountant;

import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Shoot enemies before they collect all the incriminating data!
 * The closer you are to an enemy, the more damage you do but don't get too close or you'll get killed.
 **/
public class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);

        // game loop
        while (true) {
            int x = in.nextInt();
            int y = in.nextInt();
            Agent agent = new Agent(x, y);
            int dataCount = in.nextInt();
            Collection<DataPoint> data = new ArrayList<>();
            for (int i = 0; i < dataCount; i++) {
                int dataId = in.nextInt();
                int dataX = in.nextInt();
                int dataY = in.nextInt();
                data.add(new DataPoint(dataId, dataX, dataY));
            }
            int enemyCount = in.nextInt();
            Collection<Enemy> enemies= new ArrayList<>();
            for (int i = 0; i < enemyCount; i++) {
                int enemyId = in.nextInt();
                int enemyX = in.nextInt();
                int enemyY = in.nextInt();
                int enemyLife = in.nextInt();
                enemies.add(new Enemy(enemyId, enemyX, enemyY, enemyLife));
            }

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");
            agent.protectPointsFromEnemies(data, enemies);

            System.out.println("MOVE 8000 4500"); // MOVE x y or SHOOT id
        }
    }
}