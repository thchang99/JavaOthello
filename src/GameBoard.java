import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Stack;

public class GameBoard implements Serializable {
    private int board[][];
    private boolean pass = false;
    final int[][] dir = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {-1, -1}, {1, -1}, {-1, 1}};
    private Stack<Go> availableMoves;

    public GameBoard() {
        board = new int[8][8];
        board[3][3] = 1;
        board[4][4] = 1;
        board[3][4] = -1;
        board[4][3] = -1;
        availableMoves = moves(-1);
    }

    public Stack<Go> getAvailableMoves() {
        return availableMoves;
    }
    public void setAvailableMoves(int side) {
        availableMoves = moves(side);
    }

    public int[][] getBoard() {
        return board;
    }

    public void turn(int x, int y, int side) {
        board[x][y] = side;
    }

    public Stack<Go> set(int x, int y, int side) {
        Stack<Go> coords = new Stack<>();
        for (Go move : availableMoves) {
            if (move.x == x && move.y == y) {
                int i = x;
                int j = y;
                i += move.dx;
                j += move.dy;
                do {
                    coords.push(new Go(i, j, 0, 0));
                    i += move.dx;
                    j += move.dy;
                } while (board[i][j] == side * -1);
            }
        }

        return coords;
    }

    private Stack<Go> moves(int side) {
        Stack<Go> st = new Stack<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (board[i][j] == 0) {
                    for (int[] dir : this.dir) {
                        try {
                            int x = i + dir[0];
                            int y = j + dir[1];
                            if (board[x][y] == side * -1) {
                                while (board[x][y] == side * -1) {
                                    x += dir[0];
                                    y += dir[1];
                                    if (board[x][y] == side) {
                                        st.push(new Go(i, j, dir[0], dir[1]));
                                    }
                                }
                            }
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
        return st;
    }




    /*
     *  0 0 0 0 0 0 0 0
     *  0 0 0 0 0 0 0 0
     *  0 0 0 -1 1 0 0 0
     *  0 0 0 1 -1 0 0 0
     *  0 0 0 0 0 0 0 0
     *  0 0 0 0 0 0 0 0
     *
     * */

    /*
      if is '0'
    * if surrounded by zeros is no
    * if detect number opposite number on direction
    *   go in direction until it finds opposite number
    *   if zero stop
    *
     */
}