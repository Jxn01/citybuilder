package model.buildings.playerbuilt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import model.Coordinate;
import model.buildings.interfaces.FunctionalBuilding;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PoliceStation.class, name = "police"),
        @JsonSubTypes.Type(value = Stadium.class, name = "stadium"),
        @JsonSubTypes.Type(value = FireDepartment.class, name = "firedepartment"),
        @JsonSubTypes.Type(value = Forest.class, name = "forest")
})

public abstract class RangedBuilding extends PlayerBuilding implements FunctionalBuilding {
    protected int range;

    /**
     * Constructor of the ranged building
     *
     * @param coords          is the coordinates of the ranged building
     * @param firePossibility is the fire possibility of the ranged building
     * @param isOnFire        is the ranged building on fire
     * @param buildCost       is the build cost of the ranged building
     * @param maintenanceCost is the maintenance cost of the ranged building
     * @param range           is the range of the ranged building
     */

    @JsonCreator
    public RangedBuilding(@JsonProperty("coords") Coordinate coords, @JsonProperty("firePossibility") double firePossibility, @JsonProperty("isOnFire") boolean isOnFire, @JsonProperty("buildCost") int buildCost, @JsonProperty("maintenanceCost") int maintenanceCost, @JsonProperty("range") int range) {
        super(coords, firePossibility, isOnFire, buildCost, maintenanceCost);

        this.range = range;
    }

    /**
     * Get the range of the ranged building
     *
     * @return the range of the ranged building
     */
    public int getRange() {
        return range;
    }

    /**
     * Set the range of the ranged building
     *
     * @param range is the range of the ranged building
     */
    public void setRange(int range) {
        this.range = range;
    }
}
