package model.buildings.playerbuilt;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import controller.GameManager;
import model.Coordinate;
import model.Person;
import model.buildings.generated.GeneratedBuilding;
import model.enums.Effect;
import model.field.Field;
import model.field.PlayableField;
import org.jetbrains.annotations.NotNull;
import util.Logger;

import java.util.ArrayList;
import java.util.Arrays;

public class PoliceStation extends RangedBuilding {

    /**
     * Constructor of the police station
     *
     * @param coords is the coordinates of the police station
     */
    public PoliceStation(@NotNull Coordinate coords) {
        super(coords, GameManager.getFirePossibility(), false, GameManager.getPoliceBuildCost(), GameManager.getPoliceMaintenanceCost(), GameManager.getPoliceRange());
        Logger.log("Police station created at " + coords.toString());
    }

    @JsonCreator
    public PoliceStation(@JsonProperty("coords") Coordinate coords, @JsonProperty("firePossibility") double firePossibility, @JsonProperty("onFire") boolean onFire, @JsonProperty("buildCost") int buildCost, @JsonProperty("maintenanceCost") int maintenanceCost, @JsonProperty("range") int range) {
        super(coords, firePossibility, onFire, buildCost, maintenanceCost, range);
    }

    @JsonIgnore
    @Override
    public @NotNull String getStatistics() {
        String statistics = "Police station statistics:\n";
        statistics += "Range: " + range + "\n";
        statistics += "Build cost: " + buildCost + "\n";
        statistics += "Maintenance cost: " + maintenanceCost + "\n";
        statistics += "HP: " + hp + "/" + GameManager.getBuildingMaxHP() + "\n";
        return statistics;
    }

    /**
     * The effect of the Police Station.
     */
    @Override
    public void effect() {
        Field[][] fields = GameManager.getFields();
        ArrayList<Person> peopleInBuildingsWithinRange = Arrays.stream(fields)
                .flatMap(Arrays::stream)
                .filter(f -> f instanceof PlayableField)
                .map(f -> (PlayableField) f)
                .filter(f -> f.getBuilding() instanceof GeneratedBuilding)
                .map(f -> (GeneratedBuilding) f.getBuilding())
                .filter(f -> calculateDistance(f.getCoords(), coords) <= range)
                .map(GeneratedBuilding::getPeople)
                .collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);

        peopleInBuildingsWithinRange.forEach(p -> p.addEffect(Effect.POLICE_STATION));
    }

    /**
     * Calculate the distance between two coordinates
     *
     * @param c1 is the first coordinate
     * @param c2 is the second coordinate
     * @return the distance between the two coordinates
     */
    private int calculateDistance(@NotNull Coordinate c1, @NotNull Coordinate c2) {
        return Math.abs(c1.getX() - c2.getX()) + Math.abs(c1.getY() - c2.getY());
    }
}