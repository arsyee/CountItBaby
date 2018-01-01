package hu.fallen.countitbaby.helpers;

public class Dim {
    private int x;
    private int y;

    public Dim(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int X() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }

    public int Y() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public int hashCode() {
        // mostly used to identify coordinates on screen
        // this way hashCode is different for or tipical coordinates
        return 10*1000*x + y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Dim)) return false;
        Dim other = (Dim) obj;
        return x == other.x && y == other.y;
    }

    public Object tag;
}
