package model.buildings.playerbuilt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import controller.GameManager;
import model.Coordinate;
import util.Logger;

@JsonTypeName("road")
public class Road extends PlayerBuilding {

    /**
     * Constructor of the road
     *
     * @param coords is the coordinates of the road
     */
    public Road(Coordinate coords) {
        super(coords, 0.0, false, GameManager.getRoadBuildCost(), GameManager.getRoadMaintenanceCost());

        Logger.log("Road created at " + coords.toString());
    }

    @JsonCreator
    public Road(@JsonProperty("coords") Coordinate coords, @JsonProperty("firePossibility") double firePossibility, @JsonProperty("isOnFire") boolean isOnFire, @JsonProperty("buildCost") int buildCost, @JsonProperty("maintenanceCost") int maintenanceCost) {
        super(coords, firePossibility, isOnFire, buildCost, maintenanceCost);
    }

    @JsonIgnore
    @Override
    public String getStatistics() {
        String statistics = "Road statistics:\n";
        statistics += "Build cost: " + buildCost + "\n";
        statistics += "Maintenance cost: " + maintenanceCost + "\n";
        return statistics;
    }

    // TODO: Road can't be on fire
    @Override
    public void setOnFire() {
        Logger.log("Road is on fire at " + coords.toString());
    }

    @Override
    public void extinguish() {
        //Road can't be on fire
    }
}
