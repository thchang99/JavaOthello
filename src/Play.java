import java.util.Arrays;
import java.util.Scanner;
import java.util.Stack;

public class Play {
    GameBoard gameBoard;
    int myTurn;
    int turn;

    public Play(int myTurn) {
        gameBoard = new GameBoard();
        turn = -1;
    }

    public int getTurn() {
        return turn;
    }

    public Stack<Go> getAvailableMoves() {
        return gameBoard.getAvailableMoves();
    }

    public void skip() {
        turn *= -1;
        gameBoard.setAvailableMoves(turn);
    }

    public int[] getScore() {
        int[] score = new int[2];
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                if (gameBoard.getBoard()[i][j] == 1) {
                    score[1]++;
                } else if (gameBoard.getBoard()[i][j] == -1) {
                    score[0]++;
                }
            }
        return score;
    }

    public Stack<Go> playon(int x, int y) {
        Stack<Go> move;
        Go setter = new Go(0, 0, 0, 0);
//        Stack<Go> st = gameBoard.getAvailableMoves();
//        System.out.println(Arrays.deepToString(st.toArray()));
        setter.x = x;
        setter.y = y;
        move = gameBoard.set(setter.x, setter.y, turn);
//        if (move.isEmpty()) {
//            System.out.println("Illegal move!");
//            return;
//        }
        gameBoard.turn(setter.x, setter.y, turn);
        for (Go coords : move) {
            gameBoard.turn(coords.x, coords.y, turn);
        }
        turn *= -1;
        gameBoard.setAvailableMoves(turn);
        return move;
    }

}
