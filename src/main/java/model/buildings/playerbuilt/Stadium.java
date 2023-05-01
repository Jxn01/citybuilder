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
import util.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Stadium extends RangedBuilding {

    private Coordinate topRightCoords;
    private Coordinate bottomLeftCoords;
    private Coordinate bottomRightCoords;

    /**
     * Constructor of the stadium
     *
     * @param topLeftCoords     is the top left coordinates of the stadium
     * @param topRightCoords    is the top right coordinates of the stadium
     * @param bottomLeftCoords  is the bottom left coordinates of the stadium
     * @param bottomRightCoords is the bottom right coordinates of the stadium
     */
    public Stadium(Coordinate topLeftCoords, Coordinate topRightCoords, Coordinate bottomLeftCoords, Coordinate bottomRightCoords) {
        super(topLeftCoords, GameManager.getFirePossibility(), false, GameManager.getStadiumBuildCost(), GameManager.getStadiumMaintenanceCost(), GameManager.getStadiumRange());

        this.topRightCoords = topRightCoords;
        this.bottomLeftCoords = bottomLeftCoords;
        this.bottomRightCoords = bottomRightCoords;

        Logger.log("Stadium created at " + coords.toString());
    }

    @JsonCreator
    public Stadium(@JsonProperty("coords") Coordinate coords, @JsonProperty("firePossibility") double firePossibility, @JsonProperty("onFire") boolean onFire, @JsonProperty("buildCost") int buildCost, @JsonProperty("maintenanceCost") int maintenanceCost, @JsonProperty("range") int range) {
        super(coords, firePossibility, onFire, buildCost, maintenanceCost, range);
    }

    /**
     * Get the top right coordinates of the stadium
     *
     * @return the top right coordinates of the stadium
     */
    public Coordinate getTopRightCoords() {
        return topRightCoords;
    }

    /**
     * Set the top right coordinates of the stadium
     *
     * @param topRightCoords is the top right coordinates of the stadium
     */
    public void setTopRightCoords(Coordinate topRightCoords) {
        this.topRightCoords = topRightCoords;
    }

    /**
     * Get the bottom left coordinates of the stadium
     *
     * @return the bottom left coordinates of the stadium
     */
    public Coordinate getBottomLeftCoords() {
        return bottomLeftCoords;
    }

    /**
     * Set the bottom left coordinates of the stadium
     *
     * @param bottomLeftCoords is the bottom left coordinates of the stadium
     */
    public void setBottomLeftCoords(Coordinate bottomLeftCoords) {
        this.bottomLeftCoords = bottomLeftCoords;
    }

    /**
     * Get the bottom right coordinates of the stadium
     *
     * @return the bottom right coordinates of the stadium
     */
    public Coordinate getBottomRightCoords() {
        return bottomRightCoords;
    }

    /**
     * Set the bottom right coordinates of the stadium
     *
     * @param bottomRightCoords is the bottom right coordinates of the stadium
     */
    public void setBottomRightCoords(Coordinate bottomRightCoords) {
        this.bottomRightCoords = bottomRightCoords;
    }

    @JsonIgnore
    @Override
    public String getStatistics() {
        String statistics = "Stadium statistics:\n";
        statistics += "Range: " + range + "\n";
        statistics += "Build cost: " + buildCost + "\n";
        statistics += "Maintenance cost: " + maintenanceCost + "\n";
        return statistics;
    }

    @Override
    public void setOnFire() {
        onFire = true;
        Logger.log("Stadium is on fire at " + coords.toString());
    }

    @Override
    public void extinguish() {
        onFire = false;
        Logger.log("Stadium is extinguished at " + coords.toString());
    }

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

        peopleInBuildingsWithinRange.forEach(p -> p.addEffect(Effect.STADIUM));
    }

    private int calculateDistance(Coordinate c1, Coordinate c2) {
        return Math.abs(c1.getX() - c2.getX()) + Math.abs(c1.getY() - c2.getY());
    }

}
