import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;

public class GoPanel extends JLabel implements MouseListener {
    private int x, y;
    private int side;
    private boolean available;
    private BoardPanel board;
    private double scale;

    public GoPanel(int x, int y, BoardPanel board) {
        addMouseListener(this);
        this.board = board;
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(70, 70));
        this.x = x;
        this.y = y;
        side = 0;
        scale = 1;
        available = false;
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(49, 145, 56));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.fillRect(2, 2, getWidth() - 4, getHeight() - 4);
        AffineTransform at = g2d.getTransform();
        g2d.translate(35, 35);
        g2d.scale(scale, 1);

        if (available) {
            g2d.setColor(Color.LIGHT_GRAY);
            g2d.fillRect(-5, -5, 10, 10);
        }

        if (side == -1) {
            g2d.setColor(new Color(28, 28, 28));
            g2d.fillOval(-29, -29, 58, 58);
            g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.setColor(new Color(40, 40, 40));
            g2d.drawArc(-25, -25, 50, 50, 110, 60);
            g2d.setColor(Color.BLACK);
            g2d.drawArc(-25, -25, 50, 50, -10, -60);
        }

        if (side == 1) {
            g2d.setColor(new Color(252, 247, 230));
            g2d.fillOval(-29, -29, 58, 58);
            g2d.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2d.setColor(Color.WHITE);
            g2d.drawArc(-25, -25, 50, 50, 110, 60);
            g2d.setColor(new Color(237, 232, 216));
            g2d.drawArc(-25, -25, 50, 50, 110, 60);

        }
        g2d.setTransform(at);
    }

    public void setAvailable(boolean b)
    {
        available = b;
        repaint();
    }


    public void place(int side) {
        this.side = side;
        available = false;
        repaint();
    }

    public void flip() {

        Thread thread = new Thread() {
            public void run() {
                try {
                    while (scale > .0) {
                        scale -= .1;
                        repaint();
                        sleep(10);
                    }
                    place(side * -1);
                    while (scale < 1) {
                        scale += .1;
                        repaint();
                        sleep(10);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (available)
            board.clicked(x, y);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
