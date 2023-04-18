package model.buildings.generated;

import model.Coordinate;
import model.Person;
import model.enums.SaturationRate;
import util.ResourceLoader;

import java.io.IOException;
import java.util.ArrayList;

public class ServiceWorkplace extends Workplace {

    /**
     * Constructor of the service workplace
     * @param coords is the coordinates of the service workplace
     */
    public ServiceWorkplace(Coordinate coords) {
        super(null, coords, 0.0, false, null, null, 0, 0);

        try{
            texture = ResourceLoader.loadImage("serviceZone.png");
        }catch (IOException e) {
            e.printStackTrace();
            texture = null;
        }

        people = new ArrayList<>();
        saturationRate = SaturationRate.EMPTY;
        publicSafety = 100;
        maxCapacity = 20;
        System.out.println("Service workplace created at " + coords.toString());
    }

    @Override
    public String getStatistics() {
        String statistics = "Industrial workplace statistics:\n";
        statistics += "Maximum capacity: " + maxCapacity + "\n";
        statistics += "Number of people working here: " + people.size() + "\n";
        statistics += "Public safety: " + publicSafety + "\n";
        statistics += "Saturation rate: " + saturationRate + "\n";
        System.out.println(statistics);
        return statistics;
    }

    @Override
    public void setOnFire() {
        onFire = true;
        System.out.println("Service workplace at " + coords.toString() + " is on fire!");
    }

    @Override
    public void extinguish() {
        onFire = false;
        System.out.println("Service workplace at " + coords.toString() + " is extinguished!");
    }

    @Override
    public void addPerson(Person person) throws RuntimeException{
        if(people.size() == maxCapacity){
            System.out.println("Can't add person " + person.getName() + " to service workplace at " + coords.toString() + " because maximum capacity reached!");
            throw new RuntimeException("Maximum capacity reached! Can't add new person!");
        }
        people.add(person);
        person.setWorkplace(this);
        updateSaturationRate();
        System.out.println("Person " + person.getName() + " added to service workplace at " + coords.toString());
    }

    @Override
    public void removePerson(Person person) {
        people.remove(person);
        person.setWorkplace(null);
        updateSaturationRate();
        System.out.println("Person " + person.getName() + " removed from service workplace at " + coords.toString());
    }
}
