package model.field;

import com.fasterxml.jackson.annotation.*;
import model.Coordinate;
import view.enums.Tile;

import java.awt.*;
import java.util.Random;

import static util.ResourceLoader.loadImage;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BorderField.class, name = "border"),
        @JsonSubTypes.Type(value = PlayableField.class, name = "playable")
})

/**
 * This class represents a field on the map
 */
public abstract class Field {
    protected final Coordinate coord;
    protected Tile tile;

    /**
     * Constructor of the field.
     * @param coord The coordinate of the field.
     */
    public Field(Coordinate coord) {
        this.coord = coord;
        Random rand = new Random();
        int randomNum = rand.nextInt(3) + 1;
        switch(randomNum) {
            case 1 -> tile = Tile.GRASS_1;
            case 2 -> tile = Tile.GRASS_2;
            case 3 -> tile = Tile.GRASS_3;
        }
    }

    @JsonCreator
    public Field(@JsonProperty("coord") Coordinate coord, @JsonProperty("tile") Tile tile) {
        this.coord = coord;
        this.tile = tile;
    }

    /**
     * Resets the tile of the field
     */
    protected void resetTile(){
        Random rand = new Random();
        int randomNum = rand.nextInt(3) + 1;
        switch(randomNum) {
            case 1 -> tile = Tile.GRASS_1;
            case 2 -> tile = Tile.GRASS_2;
            case 3 -> tile = Tile.GRASS_3;
        }
    }

    /**
     * This method returns the coordinate of the field.
     * @return The coordinate of the field.
     */
    public Coordinate getCoord() {
        return coord;
    }

    /**
     * This method returns the texture tile of the field.
     * @return The texture tile of the field.
     */
    public Tile getTile() {
        return tile;
    }

    /**
     * This method sets the texture tile of the field.
     * @param tile The texture tile of the field.
     */
    public void setTile(Tile tile) {
        this.tile = tile;
    }
}
