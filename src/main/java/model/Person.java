package model;

import java.util.ArrayList;
import com.github.javafaker.Faker;
import model.buildings.generated.ResidentialBuilding;
import model.buildings.generated.Workplace;
import model.enums.Effect;
import util.Logger;
import static model.GameData.budget;

/**
 * This class represents a person in the game
 */
public class Person {
    private int age;
    private String name;
    private ArrayList<Effect> effects;
    private ResidentialBuilding home;
    private Workplace workplace;
    boolean founder;

    /**
     * Constructor for Person
     */
    public Person() {
        //random between 18 and 65
        this.age = (int) (Math.random() * 47 + 18);
        this.name = Faker.instance().name().fullName();
        this.effects = new ArrayList<>();

        Logger.log("Person created: " + name + " (" + age + ")");
    }

    /**
     * Constructor for Person
     * @param founder if the person is a founder
     */
    public Person(boolean founder) {
        this();
        this.founder = founder;
    }

    /**
     * Calculates the decease chance of the person
     * @return the decease chance of the person
     */
    public double calculateDeceaseChance() {
        double deceaseChance;
        if(age < 65) {
            deceaseChance = 0.0;
        } else if(age < 70) {
            deceaseChance = 0.2;
        } else if(age < 80) {
            deceaseChance = 0.5;
        } else if(age < 90) {
            deceaseChance = 0.8;
        } else {
            deceaseChance = 1.0;
        }
        Logger.log("Decease chance of " + name + " is " + deceaseChance);
        return deceaseChance;
    }

    /**
     * Calculates the move away chance of the person
     * @return the move away chance of the person
     */
    public double calculateMoveAwayChance() {
        double moveAwayChance;
        if(founder) {
            moveAwayChance = 0.0;
        } else {
            int satisfaction = calculateSatisfaction();
            if(satisfaction < 20) {
                moveAwayChance = 1.0;
            } else if(satisfaction < 40) {
                moveAwayChance = 0.5;
            } else if(satisfaction < 60) {
                moveAwayChance = 0.2;
            } else if(satisfaction < 80) {
                moveAwayChance = 0.1;
            } else {
                moveAwayChance = 0.0;
            }
        }
        Logger.log("Move away chance of " + name + " is " + moveAwayChance);
        return moveAwayChance;
    }

    /**
     * This method calculates the distance to work of the person
     * @return the distance to work of the person
     */
    public int calculateDistanceToWork() {
        int distance = 0;
        //todo: calculate distance based on graph

        Logger.log("Distance to work of " + name + " is " + distance);
        return distance;
    }

    /**
     * This method calculates the satisfaction of the person
     * @return the satisfaction of the person
     */
    public int calculateSatisfaction() {
        //calculate satisfaction based on effects
        int satisfaction = 60;

        for(Effect effect : effects) {
            satisfaction += effect.getValue();
        }

        Logger.log("Satisfaction of " + name + " is " + satisfaction);
        return satisfaction;
    }

    /**
     * This method adds an effect to the person
     * @param effect the effect to add
     */
    public void addEffect(Effect effect) {
        effects.add(effect);
        Logger.log("Effect " + effect + " added to " + name);
    }

    /**
     * This method removes an effect from the person
     * @param effect the effect to remove
     */
    public void removeEffect(Effect effect) {
        effects.remove(effect);
        Logger.log("Effect " + effect + " removed from " + name);
    }

    /**
     * This method checks if the person is retired
     * @return true if the person is retired
     */
    public boolean isRetired() {
        return age > 65;
    }

    /**
     * This method "kills" the person
     */
    public void decease() {
        Logger.log(name + " has died");
        this.name = "Deceased";
    }

    /**
     * This method moves the person away
     */
    public void moveAway() {
        Logger.log(name + " has moved away");
        this.name = "Moved away";
    }

    /**
     * This method pays the taxes of the person
     */
    public void payTaxes() {
        Logger.log(name + " has paid taxes");
        budget += 1000;
        //exact amount to be determined
    }

    /**
     * This method pays the pension of the person
     */
    public void getsPension() {
        Logger.log(name + " has received pension");
        budget -= 1000;
        //exact amount to be determined
    }

    /**
     * Getter for the age
     * @return the age of the person
     */
    public int getAge() {
        return age;
    }

    /**
     * Setter for the age
     * @param age the age of the person
     */
    public void setAge(int age) {
        Logger.log("New age of " + name + " is " + age);
        this.age = age;
    }

    /**
     * Getter for the name
     * @return the name of the person
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the name
     * @param name the name of the person
     */
    public void setName(String name) {
        Logger.log("New name of " + this.name + " is " + name);
        this.name = name;
    }

    /**
     * Getter for the effects
     * @return the effects of the person
     */
    public ArrayList<Effect> getEffects() {
        return effects;
    }

    /**
     * Setter for the effects
     * @param effects the effects of the person
     */
    public void setEffects(ArrayList<Effect> effects) {
        Logger.log("New effects of " + name + " are " + effects.toString());
        this.effects = effects;
    }

    /**
     * Setter for the home
     * @param residentialBuilding the home of the person
     */
    public void setHome(ResidentialBuilding residentialBuilding) {
        this.home = residentialBuilding;
        if(residentialBuilding != null) {
            Logger.log("New home of " + name + " is" + residentialBuilding);
        } else {
            Logger.log("New home of " + name + " is null");
        }

    }

    /**
     * Getter for the home
     * @return the home of the person
     */
    public ResidentialBuilding getHome() {
        return home;
    }

    /**
     * Setter for the workplace
     * @param workplace the workplace of the person
     */
    public void setWorkplace(Workplace workplace) {
        this.workplace = workplace;
        if(workplace != null) {
            Logger.log("New home of " + name + " is" + workplace);
        } else {
            Logger.log("New home of " + name + " is null");
        }
    }

    /**
     * Getter for the workplace
     * @return the workplace of the person
     */
    public Workplace getWorkplace() {
        return workplace;
    }

    @Override
    public String toString() {
        return "Person{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", effects=" + effects +
                ", home=" + home +
                ", workplace=" + workplace +
                ", founder=" + founder +
                '}';
    }
}
