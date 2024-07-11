import java.io.Serializable;

public class Go implements Serializable {
    int x = 0;
    int y = 0;
    int dx = 0;
    int dy = 0;

    public Go(int x, int y, int dx, int dy) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public String toString()
    {
        return "(" + x + "," + y + "," + dx + "," + dy + ")";
    }
}
