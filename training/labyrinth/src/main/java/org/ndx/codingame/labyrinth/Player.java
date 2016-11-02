package org.ndx.codingame.labyrinth;

import java.util.Scanner;

import org.ndx.codingame.lib2d.discrete.Direction;

public class Player {

    public static void main(String args[]) {
        Scanner in = new Scanner(System.in);
        int R = in.nextInt(); // number of rows.
        int C = in.nextInt(); // number of columns.
        int A = in.nextInt(); // number of rounds between the time the alarm countdown is activated and the time the alarm goes off.

        PlayField playground = new PlayField(C, R);
        Agent kirk = new Agent(A);
        Direction move = null;
        // game loop
        while (true) {
            int KR = in.nextInt(); // row where Kirk is located.
            int KC = in.nextInt(); // column where Kirk is located.
            kirk.putOn(KC, KR);
            playground.clear();
            for (int i = 0; i < R; i++) {
                String ROW = in.next(); // C of the characters in '#.TC?' (i.e. one line of the ASCII maze).
                playground.setRow(i, ROW);
                System.err.println(ROW);
            }
            System.err.println("agent status is "+kirk);
            kirk.scan(playground, move);

            move = kirk.moveOn(playground);
            System.out.println(move.name); // Kirk's next move (UP DOWN LEFT or RIGHT).
        }
    }
}