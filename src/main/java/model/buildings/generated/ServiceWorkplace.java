package model.buildings.generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import controller.GameManager;
import model.Coordinate;
import model.Person;
import model.enums.SaturationRate;
import util.Logger;

import java.util.ArrayList;

public class ServiceWorkplace extends Workplace {

    /**
     * Constructor of the service workplace
     *
     * @param coords is the coordinates of the service workplace
     */
    public ServiceWorkplace(Coordinate coords) {
        super(coords, 0.0, false, null, 0);

        people = new ArrayList<>();
        publicSafety = 100;

        Logger.log("Service workplace created at " + coords.toString());
    }

    @JsonCreator
    public ServiceWorkplace(@JsonProperty("coords") Coordinate coords, @JsonProperty("firePossibility") double firePossibility, @JsonProperty("isOnFire") boolean isOnFire, @JsonProperty("people") ArrayList<Person> people, @JsonProperty("saturationRate") SaturationRate saturationRate, @JsonProperty("publicSafety") int publicSafety) {
        super(coords, firePossibility, isOnFire, people, publicSafety);
    }

    @JsonIgnore
    @Override
    public String getStatistics() {
        String statistics = "Service workplace statistics:\n";
        statistics += "Maximum capacity: " + maxCapacity + "\n";
        statistics += "Number of people working here: " + people.size() + "\n";
        statistics += "Public safety: " + publicSafety + "\n";
        statistics += "HP: " + hp + "/" + GameManager.getBuildingMaxHP() + "\n";
        return statistics;
    }

    @Override
    public void setOnFire() {
        Logger.log("Service workplace at " + coords.toString() + " is on fire!");
        onFire = true;
    }

    @Override
    public void addPerson(Person person) throws RuntimeException {
        if (people == null) {
            people = new ArrayList<>();
        }
        if (people.size() == maxCapacity) {
            Logger.log("Can't add person " + person.getName() + " to service workplace at " + coords.toString() + " because maximum capacity reached!");
            throw new RuntimeException("Maximum capacity reached! Can't add new person!");
        } else {
            Logger.log("Person " + person.getName() + " added to service workplace at " + coords.toString());

            people.add(person);

            person.setWorkplace(this);
        }
    }

    @Override
    public void removePerson(Person person) {
        Logger.log("Person " + person.getName() + " removed from service workplace at " + coords.toString());

        people.remove(person);
        person.setWorkplace(null);
    }
}
