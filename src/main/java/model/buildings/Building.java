package model.buildings;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import model.Coordinate;
import model.buildings.generated.GeneratedBuilding;
import model.buildings.interfaces.Flammable;
import model.buildings.playerbuilt.PlayerBuilding;
import util.Logger;

import com.github.javafaker.Faker;

import java.util.Objects;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PlayerBuilding.class, name = "playerbuilt"),
        @JsonSubTypes.Type(value = GeneratedBuilding.class, name = "generated")
})

/**
 * This class represents a building.
 */
public abstract class Building implements Flammable {
    protected String address = Faker.instance().address().streetAddressNumber();
    protected Coordinate coords;
    protected double firePossibility;
    protected boolean onFire;

    /**
     * Constructor of the building
     * @param coords is the coordinates of the building
     * @param firePossibility is the fire possibility of the building
     * @param onFire is the building on fire
     */
    public Building(Coordinate coords, double firePossibility, boolean onFire) {
        this.coords = coords;
        this.firePossibility = firePossibility;
        this.onFire = onFire;
    }

    @JsonCreator
    public Building(@JsonProperty("address") String address, @JsonProperty("coords") Coordinate coords, @JsonProperty("firePossibility") double firePossibility, @JsonProperty("onFire") boolean onFire) {
        this.address = address;
        this.coords = coords;
        this.firePossibility = firePossibility;
        this.onFire = onFire;
    }

    /**
     * Get the statistics of the building
     * @return the statistics of the building
     */
    public abstract String getStatistics();

    /**
     * Get the coordinates of the building
     * @return the coordinates of the building
     */
    public Coordinate getCoords() {
        return coords;
    }

    /**
     * Set the coordinates of the building
     * @param coords is the coordinates of the building
     */
    public void setCoords(Coordinate coords) {
        this.coords = coords;
    }

    /**
     * Get the fire possibility of the building
     * @return the fire possibility of the building
     */
    public double getFirePossibility() {
        return firePossibility;
    }

    /**
     * Set the fire possibility of the building
     * @param firePossibility is the fire possibility of the building
     */
    public void setFirePossibility(double firePossibility) {
        Logger.log("Fire possibility set to " + firePossibility + " at " + coords.toString());
        this.firePossibility = firePossibility;
    }

    /**
     * Get the fire status of the building
     * @return the fire status of the building
     */
    public boolean isOnFire() {
        return onFire;
    }

    /**
     * Get the address of the building
     * @return the address of the building
     */
    public String getAddress() {
        return address;
    }

    /**
     * Set the address of the building
     * @param address is the address of the building
     */
    public void setAddress(String address) {
        Logger.log("Setting address to " + address + " at " + coords.toString());
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Building building)) return false;
        return Double.compare(building.getFirePossibility(), getFirePossibility()) == 0 && isOnFire() == building.isOnFire() && Objects.equals(getAddress(), building.getAddress()) && Objects.equals(getCoords(), building.getCoords());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAddress(), getCoords(), getFirePossibility(), isOnFire());
    }
}
