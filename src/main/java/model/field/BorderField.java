package model.field;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import model.Coordinate;
import view.enums.Tile;

import static util.ResourceLoader.*;
import java.io.IOException;

/**
 * This class represents a border field on the map
 */
@JsonTypeName("border")
public class BorderField extends Field {

    /**
     * Constructor of the border field.
     * @param coord The coordinate of the border field.
     */
    @JsonCreator
    public BorderField(@JsonProperty("coord") Coordinate coord) {
        super(coord);
        this.tile = Tile.ROCKS;
    }
}
