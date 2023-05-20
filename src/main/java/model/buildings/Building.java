package model.buildings;

import com.fasterxml.jackson.annotation.*;
import com.github.javafaker.Faker;
import controller.GameManager;
import model.Coordinate;
import model.buildings.generated.GeneratedBuilding;
import model.buildings.interfaces.Flammable;
import model.buildings.playerbuilt.PlayerBuilding;
import model.buildings.playerbuilt.Stadium;
import model.field.PlayableField;
import util.Logger;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.random.RandomGenerator;

/**
 * This class represents a building.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = PlayerBuilding.class, name = "playerbuilt"),
        @JsonSubTypes.Type(value = GeneratedBuilding.class, name = "generated")
})
public abstract class Building implements Flammable {
    protected String address = Faker.instance().address().streetAddressNumber();
    protected Coordinate coords;
    protected double firePossibility;
    protected boolean onFire;
    protected int hp = 10;

    /**
     * Constructor of the building
     *
     * @param coords          is the coordinates of the building
     * @param firePossibility is the fire possibility of the building
     * @param onFire          is the building on fire
     */
    public Building(Coordinate coords, double firePossibility, boolean onFire) {
        this.coords = coords;
        this.firePossibility = firePossibility;
        this.onFire = onFire;
        this.hp = GameManager.getBuildingMaxHP();
    }

    @JsonCreator
    public Building(@JsonProperty("address") String address, @JsonProperty("coords") Coordinate coords, @JsonProperty("firePossibility") double firePossibility, @JsonProperty("onFire") boolean onFire, @JsonProperty("hp") int hp) {
        this.address = address;
        this.coords = coords;
        this.firePossibility = firePossibility;
        this.onFire = onFire;
        this.hp = hp;
    }

    public ArrayList<Building> getNeighbours() {
        ArrayList<Building> neighbours = new ArrayList<>();
        GameManager.getGameData().getPlayableFieldsWithBuildings()
                .stream()
                .map(PlayableField::getBuilding)
                .filter(b -> b.getDistance(this.getCoords()) <= 1)
                .forEach(neighbours::add);
        return neighbours;
    }

    public int getDistance(Coordinate c) {
        return Math.abs(this.getX() - c.getX()) + Math.abs(this.getY() - c.getY());
    }

    public abstract String getStatistics();
    public Coordinate getCoords() {
        return coords;
    }
    public void setCoords(Coordinate coords) {
        this.coords = coords;
    }
    public double getFirePossibility() {
        return firePossibility;
    }
    public void setFirePossibility(double firePossibility) {
        //Logger.log("Fire possibility set to " + firePossibility + " at " + coords.toString()); spammy
        this.firePossibility = firePossibility;
    }
    public boolean isOnFire() {
        return onFire;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        Logger.log("Setting address to " + address + " at " + coords.toString());
        this.address = address;
    }
    public int getHp() {
        return hp;
    }
    public void setHp(int hp) {
        this.hp = hp;
    }
    @JsonIgnore
    public int getX() {
        return coords.getX();
    }
    @JsonIgnore
    public int getY() {
        return coords.getY();
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

    public abstract int getMaintenanceCost();

    public abstract int getBuildCost();

    @Override
    public void setOnFire() {
        Logger.log("Building is on fire at " + coords.toString());
        onFire = true;
    }

    @Override
    public void extinguish() {
        Logger.log("Building is extinguished at " + coords.toString());
        onFire = false;
    }

    @Override
    public void burnTick() {
        Logger.log("Building is burning at " + coords.toString());

        if(this instanceof Stadium){
            if(new Random().nextInt(4) == 1) {
                hp--;
            }
        } else {
            hp--;
        }

        if(hp <= 0) {
            onFire = false;
            hp = 0;
            burnDown();
        }
    }
//
    @Override
    public void repair(){
        if(hp < GameManager.getBuildingMaxHP()){
            hp++;
        }
    }
}
