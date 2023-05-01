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

/**
 * This class represents a forest.
 */
public class Forest extends RangedBuilding {
    private int growTime;
    private int growStage;

    /**
     * Constructor of the forest
     *
     * @param coords is the coordinates of the forest
     */
    public Forest(Coordinate coords) {
        super(coords, GameManager.getFirePossibility(), false, GameManager.getForestBuildCost(), GameManager.getForestMaintenanceCost(), GameManager.getForestRange());

        this.growTime = GameManager.getForestGrowthTime();
        this.growStage = 0;

        Logger.log("Forest created at " + coords.toString());
    }

    @JsonCreator
    public Forest(@JsonProperty("coords") Coordinate coords, @JsonProperty("firePossibility") double firePossibility, @JsonProperty("onFire") boolean onFire, @JsonProperty("buildCost") int buildCost, @JsonProperty("maintenanceCost") int maintenanceCost, @JsonProperty("range") int range, @JsonProperty("growTime") int growTime, @JsonProperty("growStage") int growStage) {
        super(coords, firePossibility, onFire, buildCost, maintenanceCost, range);
        this.growTime = growTime;
        this.growStage = growStage;
    }

    /**
     * Grow the forest
     */
    public void grow() {
        if (growStage < growTime) {
            growStage++;
            Logger.log("Forest grew to " + growStage + " years old");
        } else {
            Logger.log("Forest is fully grown");
        }
    }

    @JsonIgnore
    @Override
    public String getStatistics() {
        String statistics = "Forest statistics:\n";
        statistics += "Grow time: " + growTime + " years \n";
        statistics += "Forest age: " + growStage + " years \n";
        statistics += "Range: " + range + "\n";
        statistics += "Build cost: " + buildCost + "\n";
        statistics += "Maintenance cost: " + maintenanceCost + "\n";
        return statistics;
    }

    /**
     * Get the growth time of the forest
     *
     * @return the growth time of the forest
     */
    public int getGrowTime() {
        return growTime;
    }

    /**
     * Set the growth time of the forest
     *
     * @param growTime is the growth time of the forest
     */
    public void setGrowTime(int growTime) {
        this.growTime = growTime;
    }

    /**
     * Get the growth stage of the forest
     *
     * @return the growth stage of the forest
     */
    public int getGrowStage() {
        return growStage;
    }

    /**
     * Set the growth stage of the forest
     *
     * @param growStage is the growth stage of the forest
     */
    public void setGrowStage(int growStage) {
        this.growStage = growStage;
    }

    public boolean isGrown(){
        return growStage >= growTime;
    }

    @Override
    public void setOnFire() {
        Logger.log("Forest is on fire at " + coords.toString());
        onFire = true;
    }

    @Override
    public void extinguish() {
        Logger.log("Forest is extinguished at " + coords.toString());
        onFire = false;
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

        peopleInBuildingsWithinRange.forEach(p -> p.addEffect(Effect.FOREST));
    }

    private int calculateDistance(Coordinate c1, Coordinate c2) {
        return Math.abs(c1.getX() - c2.getX()) + Math.abs(c1.getY() - c2.getY());
    }

    @Override
    public String toString() {
        return "Forest{" +
                "growTime=" + growTime +
                ", growStage=" + growStage +
                ", range=" + range +
                ", buildCost=" + buildCost +
                ", maintenanceCost=" + maintenanceCost +
                ", coords=" + coords +
                ", firePossibility=" + firePossibility +
                ", isOnFire=" + onFire +
                '}';
    }
}
