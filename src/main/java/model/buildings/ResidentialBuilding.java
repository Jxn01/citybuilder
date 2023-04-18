package model.buildings;

import model.Coordinate;
import model.Person;

import java.awt.*;
import java.util.ArrayList;

public class ResidentialBuilding extends GeneratedBuilding {

    /**
     * Constructor of the residential building
     * @param texture is the texture of the residential building
     * @param coords is the coordinates of the residential building
     * @param firePossibility is the fire possibility of the residential building
     * @param isOnFire is the residential building on fire
     * @param people is the people in the residential building
     * @param saturationRate is the saturation rate of the residential building
     * @param publicSafety is the public safety of the residential building
     */
    public ResidentialBuilding(Image texture, Coordinate coords, double firePossibility, boolean isOnFire, ArrayList<Person> people, SaturationRate saturationRate, int publicSafety) {
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
