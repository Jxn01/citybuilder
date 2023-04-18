package model;

/**
 * This class represents a coordinate
 */
public class Coordinate {
    private int x;
    private int y;

    /**
     * Constructor of the coordinate
     * @param x is the x coordinate
     * @param y is the y coordinate
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the x coordinate
     * @return the x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Set the x coordinate
     * @param x is the x coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Get the y coordinate
     * @return the y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Set the y coordinate
     * @param y is the y coordinate
     */
    public void setY(int y) {
        this.y = y;
    }
}
