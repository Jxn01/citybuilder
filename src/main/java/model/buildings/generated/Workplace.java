package model.buildings.generated;

import model.Coordinate;
import model.Person;
import model.enums.SaturationRate;

import java.awt.*;
import java.util.ArrayList;

public abstract class Workplace extends GeneratedBuilding {
    /**
     * Constructor of the workplace
     * @param texture is the texture of the workplace
     * @param coords is the coordinates of the workplace
     * @param firePossibility is the fire possibility of the workplace
     * @param isOnFire is the workplace on fire
     * @param people is the people in the workplace
     * @param saturationRate is the saturation rate of the workplace
     * @param publicSafety is the public safety of the workplace
     */
    public Workplace(Image texture, Coordinate coords, double firePossibility, boolean isOnFire, ArrayList<Person> people, SaturationRate saturationRate, int publicSafety, int maxCapacity) {
        super(texture, coords, firePossibility, isOnFire, people, saturationRate, publicSafety, maxCapacity);
    }
}
