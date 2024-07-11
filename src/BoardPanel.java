import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.Stack;

public class BoardPanel extends JPanel {
    GoPanel[][] panels;
    OthelloClientUI ui;

    public BoardPanel(OthelloClientUI ui) {
        this.ui = ui;
        setSize(new Dimension(560, 560));
        setBackground(Color.BLACK);
        setLayout(new GridLayout(8, 8));
        panels = new GoPanel[8][8];
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                panels[i][j] = new GoPanel(i, j, this);
                add(panels[i][j]);
            }
        }
        reset();
    }

    public void reset() {
        setVisible(false);
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                panels[i][j].place(0);
            }
        }
        panels[4][3].place(-1);
        panels[3][4].place(-1);
        panels[3][3].place(1);
        panels[4][4].place(1);
        setVisible(true);
    }

    public void clicked(int x, int y) {
        ui.sendSet(x, y);
    }

    public void placed(int x, int y, int side) {
        panels[x][y].place(side);
    }

    public void flip(Stack<Go> move) {
        for (Go go : move) {
            try {
                Thread.sleep(110);
                panels[go.x][go.y].flip();
            } catch (InterruptedException e) {
            }
        }
    }

    public void setAvailable(Stack<Go> available, boolean what) {
        for (Go go : available) {
            panels[go.x][go.y].setAvailable(what);
        }
    }

//    public static void main(String[] args) {
//        JFrame frame = new JFrame();
//        frame.setSize(600,560);
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        BoardPanel panel = new BoardPanel();
//        frame.setLayout(null);
//        panel.setBounds(50,50,560,560);
//        frame.add(panel);
//        frame.setVisible(true);
//    }
}
