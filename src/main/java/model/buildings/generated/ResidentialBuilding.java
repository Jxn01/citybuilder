package model.buildings.generated;

import model.Coordinate;
import model.Person;
import model.enums.SaturationRate;
import util.ResourceLoader;

import java.io.IOException;
import java.util.ArrayList;

public class ResidentialBuilding extends GeneratedBuilding {

    /**
     * Constructor of the residential building
     * @param coords is the coordinates of the residential building
     */
    public ResidentialBuilding(Coordinate coords) {
        super(null, coords, 0.0, false, null, null, 0, 0);

        try{
            texture = ResourceLoader.loadImage("livingZone.png");
        }catch (IOException e) {
            e.printStackTrace();
            texture = null;
        }

        firePossibility = 0.1;
        people = new ArrayList<>();
        saturationRate = SaturationRate.EMPTY;
        publicSafety = 100;
        maxCapacity = 20;
        System.out.println("Residential building created at " + coords.toString());
    }

    public void addPerson(Person person) throws RuntimeException{
        if(people.size() == maxCapacity){
            System.out.println("Can't add person " + person.getName() + " to residential building at " + coords.toString() + " because maximum capacity reached!");
            throw new RuntimeException("Maximum capacity reached! Can't add new person!");
        }
        people.add(person);
        person.setHome(this);
        updateSaturationRate();
        System.out.println("Person " + person.getName() + " added to residential building at " + coords.toString());
    }

    public void removePerson(Person person){
        people.remove(person);
        person.setHome(null);
        updateSaturationRate();
        System.out.println("Person " + person.getName() + " removed from residential building at " + coords.toString());
    }

    @Override
    public String getStatistics() {
        String statistics = "Residential building statistics:\n";
        statistics += "Maximum capacity: " + maxCapacity + "\n";
        statistics += "Number of people living here: " + people.size() + "\n";
        statistics += "Public safety: " + publicSafety + "\n";
        statistics += "Saturation rate: " + saturationRate + "\n";
        System.out.println(statistics);
        return statistics;
    }

    @Override
    public void setOnFire() {
        onFire = true;
        System.out.println("Residential building set on fire at " + coords.toString());
    }

    @Override
    public void extinguish() {
        onFire = false;
        System.out.println("Residential building extinguished at " + coords.toString());
    }
}
