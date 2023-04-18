package model.buildings;

import model.Coordinate;
import model.Person;

import java.awt.*;
import java.util.ArrayList;

public class IndustrialWorkplace extends Workplace {

    /**
     * Constructor of the industrial workplace
     * @param texture is the texture of the industrial workplace
     * @param coords is the coordinates of the industrial workplace
     * @param firePossibility is the fire possibility of the industrial workplace
     * @param isOnFire is the industrial workplace on fire
     * @param people is the people in the industrial workplace
     * @param saturationRate is the saturation rate of the industrial workplace
     * @param publicSafety is the public safety of the industrial workplace
     */
    public IndustrialWorkplace(Image texture, Coordinate coords, double firePossibility, boolean isOnFire, ArrayList<Person> people, SaturationRate saturationRate, int publicSafety) {
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
