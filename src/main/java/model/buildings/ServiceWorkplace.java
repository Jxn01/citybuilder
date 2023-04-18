package model.buildings;

import model.Coordinate;
import model.Person;

import java.awt.*;
import java.util.ArrayList;

public class ServiceWorkplace extends Workplace {

    /**
     * Constructor of the service workplace
     * @param texture is the texture of the service workplace
     * @param coords is the coordinates of the service workplace
     * @param firePossibility is the fire possibility of the service workplace
     * @param isOnFire is the service workplace on fire
     * @param people is the people in the service workplace
     * @param saturationRate is the saturation rate of the service workplace
     * @param publicSafety is the public safety of the service workplace
     */
    public ServiceWorkplace(Image texture, Coordinate coords, double firePossibility, boolean isOnFire, ArrayList<Person> people, SaturationRate saturationRate, int publicSafety) {
        super(texture, coords, firePossibility, isOnFire, people, saturationRate, publicSafety);
    }

    @Override
    public String getStatistics() {
        System.out.println("Get statistics");
        return "1";
    }

    @Override
    public void setTexture() {
        System.out.println("Set texture");
    }
}
