package model.buildings.generated;

import model.Coordinate;
import model.Person;
import model.enums.SaturationRate;
import util.Logger;
import util.ResourceLoader;

import java.io.IOException;
import java.util.ArrayList;

public class IndustrialWorkplace extends Workplace {

    /**
     * Constructor of the industrial workplace
     * @param coords is the coordinates of the industrial workplace
     */
    public IndustrialWorkplace(Coordinate coords) {
        super(coords, 0.0, false, null, null, 0, 0);

        people = new ArrayList<>();
        saturationRate = SaturationRate.EMPTY;
        publicSafety = 100;
        maxCapacity = 20;

        Logger.log("Industrial workplace created at " + coords.toString());
    }

    @Override
    public String getStatistics() {
        String statistics = "Industrial workplace statistics:\n";
        statistics += "Number of people working here: " + people.size() + "\n";
        statistics += "Public safety: " + publicSafety + "\n";
        statistics += "Saturation rate: " + saturationRate + "\n";
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
        if(people.size() == maxCapacity) {
            Logger.log("Can't add person " + person.getName() + " to industrial workplace building at " + coords.toString() + " because maximum capacity reached!");
            throw new RuntimeException("Maximum capacity reached! Can't add new person!");
        } else {
            Logger.log("Person " + person.getName() + " added to industrial workplace at " + coords.toString());

            people.add(person);
            person.setWorkplace(this);

            updateSaturationRate();
        }
    }

    @Override
    public void removePerson(Person person) {
        Logger.log("Person " + person.getName() + " removed from industrial workplace at " + coords.toString());

        people.remove(person);
        person.setWorkplace(null);

        updateSaturationRate();
    }
}
