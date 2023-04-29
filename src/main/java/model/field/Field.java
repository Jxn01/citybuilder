package model.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import model.Coordinate;
import view.enums.Tile;

import java.util.Random;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Field field)) return false;

        if (getCoord() != null ? !getCoord().equals(field.getCoord()) : field.getCoord() != null) return false;
        return getTile() == field.getTile();
    }

    @Override
    public int hashCode() {
        int result = getCoord() != null ? getCoord().hashCode() : 0;
        result = 31 * result + (getTile() != null ? getTile().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Field{" +
                "coord=" + coord +
                ", tile=" + tile +
                '}';
    }
}
