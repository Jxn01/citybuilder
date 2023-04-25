package model.field;

import model.Coordinate;

import java.awt.*;
import java.util.Random;

import static util.ResourceLoader.loadImage;

/**
 * This class represents a field on the map
 */
public abstract class Field {
    protected Image texture;
    protected final Coordinate coord;

    /**
     * Constructor of the field.
     * @param coord The coordinate of the field.
     */
    public Field(Coordinate coord) {
        this.coord = coord;
        Random rand = new Random();
        int randomNum = rand.nextInt(3) + 1;
        try { switch(randomNum) {
                case 1 -> texture = loadImage("GRASS_1.png");
                case 2 -> texture = loadImage("GRASS_2.png");
                case 3 -> texture = loadImage("GRASS_3.png"); }
        } catch(Exception exc){ exc.printStackTrace(); }
    }

    /**
     * This method returns the coordinate of the field.
     * @return The coordinate of the field.
     */
    public Coordinate getCoord() {
        return coord;
    }

    /**
     * This method returns the texture of the field.
     * @return The texture of the field.
     */
    public Image getTexture() {
        return texture;
    }

    /**
     * This method sets the texture of the field.
     * @param texture The texture of the field.
     */
    public void setTexture(Image texture) {
        this.texture = texture;
    }
}
