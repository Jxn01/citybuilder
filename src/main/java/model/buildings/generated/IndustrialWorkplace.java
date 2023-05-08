package model.buildings.generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import controller.GameManager;
import model.Coordinate;
import model.Person;
import model.buildings.interfaces.FunctionalBuilding;
import model.buildings.playerbuilt.Forest;
import model.enums.Effect;
import model.enums.SaturationRate;
import model.field.Field;
import model.field.PlayableField;
import util.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class IndustrialWorkplace extends Workplace implements FunctionalBuilding {

    /**
     * Constructor of the industrial workplace
     *
     * @param coords is the coordinates of the industrial workplace
     */
    public IndustrialWorkplace(Coordinate coords) {
        super(coords, 0.0, false, null, 0);

        people = new ArrayList<>();
        publicSafety = 100;

        Logger.log("Industrial workplace created at " + coords.toString());
    }

    @JsonCreator
    public IndustrialWorkplace(@JsonProperty("coords") Coordinate coords, @JsonProperty("firePossibility") double firePossibility, @JsonProperty("isOnFire") boolean isOnFire, @JsonProperty("people") ArrayList<Person> people, @JsonProperty("saturationRate") SaturationRate saturationRate, @JsonProperty("publicSafety") int publicSafety) {
        super(coords, firePossibility, isOnFire, people, publicSafety);
    }

    @JsonIgnore
    @Override
    public String getStatistics() {
        String statistics = "Industrial workplace statistics:\n";
        statistics += "Number of people working here: " + people.size() + "\n";
        statistics += "Public safety: " + publicSafety + "\n";
        statistics += "HP: " + hp + "/" + GameManager.getBuildingMaxHP() + "\n";
        return statistics;
    }

    @Override
    public void setOnFire() {
        Logger.log("Industrial workplace at " + coords.toString() + " is on fire!");
        onFire = true;
    }

    @Override
    public void extinguish() {
        Logger.log("Industrial workplace at " + coords.toString() + " is extinguished!");
        onFire = false;
    }

    @Override
    public void addPerson(Person person) throws RuntimeException {
        if (people == null) {
            people = new ArrayList<>();
        }
        if (people.size() == maxCapacity) {
            Logger.log("Can't add person " + person.getName() + " to industrial workplace building at " + coords.toString() + " because maximum capacity reached!");
            throw new RuntimeException("Maximum capacity reached! Can't add new person!");
        } else {
            Logger.log("Person " + person.getName() + " added to industrial workplace at " + coords.toString());

            people.add(person);
            person.setWorkplace(this);
        }
    }

    @Override
    public void removePerson(Person person) {
        Logger.log("Person " + person.getName() + " removed from industrial workplace at " + coords.toString());

        people.remove(person);
        person.setWorkplace(null);
    }

    /**
     * The effect of the industrial workplace
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
                .filter(f -> calculateDistance(f.getCoords(), coords) <= GameManager.getIndustrialRange())
                .filter(f -> pathBetween(coords, f.getCoords())
                        .stream()
                        .map(c -> fields[c.getX()][c.getY()])
                        .map(c -> (PlayableField) c)
                        .map(PlayableField::getBuilding)
                        .filter(b -> b instanceof Forest)
                        .map(b -> (Forest) b)
                        .noneMatch(Forest::isGrown))
                .map(GeneratedBuilding::getPeople)
                .collect(ArrayList::new, ArrayList::addAll, ArrayList::addAll);

        peopleInBuildingsWithinRange.forEach(p -> p.addEffect(Effect.INDUSTRIAL_NEARBY));
    }

    /**
     * Calculates the distance between two coordinates
     *
     * @param c1 the first coordinate
     * @param c2 the second coordinate
     * @return the distance between the two coordinates
     */
    private int calculateDistance(Coordinate c1, Coordinate c2) {
        return Math.abs(c1.getX() - c2.getX()) + Math.abs(c1.getY() - c2.getY());
    }

    /**
     * Calculates the path between two coordinates
     *
     * @param c1 the first coordinate
     * @param c2 the second coordinate
     * @return the path between the two coordinates
     */
    private List<Coordinate> pathBetween(Coordinate c1, Coordinate c2) {
        int x1 = c1.getX();
        int y1 = c1.getY();
        int x2 = c2.getX();
        int y2 = c2.getY();

        List<Coordinate> xPath = new ArrayList<>(IntStream.range(x1, x2)
                .mapToObj(x -> new Coordinate(x, y1))
                .toList());

        List<Coordinate> yPath = IntStream.range(y1, y2)
                .mapToObj(y -> new Coordinate(x2, y))
                .toList();

        xPath.addAll(yPath);

        return xPath;
    }
}
