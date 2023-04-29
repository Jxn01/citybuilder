package model.buildings.generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import controller.GameManager;
import model.Coordinate;
import model.Person;
import model.buildings.Building;
import model.enums.SaturationRate;
import util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Workplace.class, name = "workplace"),
        @JsonSubTypes.Type(value = ResidentialBuilding.class, name = "residential")
})

/**
 * This class represents a building that is generated by the game.
 */
public abstract class GeneratedBuilding extends Building {
    protected List<Person> people;
    protected SaturationRate saturationRate;
    protected int publicSafety;

    protected int maxCapacity;

    /**
     * Constructor of the generated building
     * @param coords is the coordinates of the building
     * @param firePossibility is the fire possibility of the building
     * @param isOnFire is the building on fire
     * @param people is the people in the building
     * @param saturationRate is the saturation rate of the building
     * @param publicSafety is the public safety of the building
     * @param maxCapacity is the maximum capacity of the building
     */
    @JsonCreator
    public GeneratedBuilding(@JsonProperty("coords") Coordinate coords, @JsonProperty("firePossibility") double firePossibility, @JsonProperty("isOnFire") boolean isOnFire, @JsonProperty("people") ArrayList<Person> people, @JsonProperty("saturationRate") SaturationRate saturationRate, @JsonProperty("publicSafety") int publicSafety) {
        super(coords, firePossibility, isOnFire);
        this.people = people;
        this.saturationRate = saturationRate;
        this.publicSafety = publicSafety;
        this.maxCapacity = GameManager.getLevelOneMaxCapacity();
    }

    /**
     * Add a person to the building
     * @param person is the person to add
     */
    public abstract void addPerson(Person person);

    /**
     * Remove a person from the building
     * @param person is the person to remove
     */
    public abstract void removePerson(Person person);

    /**
     * Updates the saturation rate of the building
     */
    public void updateSaturationRate() {
        if (people.size() == 0) {
            saturationRate = SaturationRate.EMPTY;
        } else if (people.size() < maxCapacity / 4) {
            saturationRate = SaturationRate.LOW;
        } else if (people.size() < maxCapacity / 2) {
            saturationRate = SaturationRate.MEDIUM;
        } else if (people.size() < maxCapacity * 3 / 4) {
            saturationRate = SaturationRate.HIGH;
        } else {
            saturationRate = SaturationRate.FULL;
        }
        Logger.log("Saturation rate of building at" + coords.toString() + " is now " + saturationRate);
    }

    /**
     * Setter for the max capacity of the building
     * @param maxCapacity is the maximum capacity of the building
     */
    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        Logger.log("Max capacity of building at " + coords.toString() + " is now " + maxCapacity);
    }

    /**
     * Get the public safety of the building
     * @return the public safety of the building
     */
    public int getPublicSafety() {
        return publicSafety;
    }

    /**
     * Set the public safety of the building
     * @param publicSafety is the public safety of the building
     */
    public void setPublicSafety(int publicSafety) {
        this.publicSafety = publicSafety;
        Logger.log("Public safety of building at " + coords.toString() + " is now " + publicSafety);
    }

    /**
     * Get the people in the building
     * @return the people in the building
     */
    public List<Person> getPeople() {
        return people;
    }

    /**
     * Get the saturation rate of the building
     * @return the saturation rate of the building
     */
    public SaturationRate getSaturationRate() {
        return saturationRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GeneratedBuilding that)) return false;
        if (!super.equals(o)) return false;
        return getPublicSafety() == that.getPublicSafety() && maxCapacity == that.maxCapacity && Objects.equals(getPeople(), that.getPeople()) && getSaturationRate() == that.getSaturationRate();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getPeople(), getSaturationRate(), getPublicSafety(), maxCapacity);
    }
}


