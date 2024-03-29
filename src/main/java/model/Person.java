package model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.javafaker.Faker;
import controller.GameManager;
import model.buildings.generated.ResidentialBuilding;
import model.buildings.generated.Workplace;
import model.enums.Effect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import util.Logger;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class represents a person in the game
 */
public class Person {
    boolean founder;
    private int age;
    private String name;
    private @Nullable Set<Effect> effects;
    private @Nullable ResidentialBuilding home;
    private @Nullable Workplace workplace;
    private ArrayList<Integer> payedTaxes;

    /**
     * Constructor for Person
     */
    public Person() {
        //random between 18 and 65
        this.age = (int) (Math.random() * 47 + 18);
        this.name = Faker.instance().name().fullName();
        this.effects = new TreeSet<>();
        this.payedTaxes = new ArrayList<>();

        //Logger.log("Person created: " + name + " (" + age + ")"); spammy + laggy
    }

    @JsonCreator
    public Person(@JsonProperty("age") int age, @JsonProperty("name") String name, @JsonProperty("effects") TreeSet<Effect> effects, @JsonProperty("founder") boolean founder, @JsonProperty("payedTaxes") ArrayList<Integer> payedTaxes) {
        this.age = age;
        this.name = name;
        this.effects = effects;
        this.home = null;
        this.workplace = null;
        this.founder = founder;
        this.payedTaxes = payedTaxes;
    }

    public ArrayList<Integer> getPayedTaxes() {
        return payedTaxes;
    }

    public void setPayedTaxes(ArrayList<Integer> payedTaxes) {
        this.payedTaxes = payedTaxes;
    }

    public void addToPayedTaxes(int tax) {
        this.payedTaxes.add(tax);
    }

    /**
     * Constructor for Person
     *
     * @param founder if the person is a founder
     */
    public Person(boolean founder) {
        this();
        this.founder = founder;
    }

    /**
     * Calculates the decease chance of the person
     *
     * @return the decease chance of the person
     */
    public double calculateDeceaseChance() {
        double deceaseChance;
        if (age < 65) {
            deceaseChance = 0.0;
        } else if (age < 70) {
            deceaseChance = 0.2;
        } else if (age < 80) {
            deceaseChance = 0.5;
        } else if (age < 90) {
            deceaseChance = 0.8;
        } else {
            deceaseChance = 1.0;
        }
        Logger.log("Decease chance of " + name + " is " + deceaseChance);
        return deceaseChance;
    }

    /**
     * Calculates the move away chance of the person
     *
     * @return the move away chance of the person
     */
    public double calculateMoveAwayChance() {
        double moveAwayChance;
        if (founder) {
            moveAwayChance = 0.0;
        } else {
            int satisfaction = calculateSatisfaction();
            if (satisfaction <= 20) {
                moveAwayChance = 1.0;
            } else if (satisfaction <= 40) {
                moveAwayChance = 0.5;
            } else if (satisfaction < 60) {
                moveAwayChance = 0.2;
            } else if (satisfaction < 80) {
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
     *
     * @return the distance to work of the person
     */
    public int calculateDistanceToWork() {
        if (Objects.isNull(home) || Objects.isNull(workplace)) {
            return 0;
        }

        int distance = GameManager.findShortestPathLength(home.getCoords(), workplace.getCoords());

        Logger.log("Distance to work of " + name + " is " + distance);
        return distance;
    }

    /**
     * This method calculates the satisfaction of the person
     *
     * @return the satisfaction of the person
     */
    public int calculateSatisfaction() {
        //calculate satisfaction based on effects
        int satisfaction = 50;

        if (Objects.isNull(effects))
            return satisfaction;

        for (Effect effect : effects) {
            satisfaction += effect.getValue();
        }

        //Logger.log("Satisfaction of " + name + " is " + satisfaction); spammy
        return satisfaction;
    }

    /**
     * This method adds an effect to the person
     *
     * @param effect the effect to add
     */
    public void addEffect(Effect effect) {
        if (effects != null) {
            effects.add(effect);
        }

        //Logger.log("Effect " + effect + " added to " + name);
    }

    /**
     * This method removes an effect from the person
     *
     * @param effect the effect to remove
     */
    public void removeEffect(Effect effect) {
        if (effects != null)
            effects.remove(effect);
        //Logger.log("Effect " + effect + " removed from " + name);
    }

    public void retire() {
        founder = true;
        fire();
    }

    /**
     * This method checks if the person is retired
     *
     * @return true if the person is retired
     */
    @JsonIgnore
    public boolean isRetired() {
        return age > 65;
    }

    /**
     * This method "kills" the person
     */
    public void decease() {
        Logger.log(name + " has died");
        this.name = "Deceased";
        this.effects = null;
        if (home != null)
            home.removePerson(this);
        if (workplace != null)
            workplace.removePerson(this);
        this.home = null;
        this.workplace = null;
    }

    /**
     * This method evicts the person
     */
    public void evict() {
        Logger.log(name + " has been evicted");
        workplace.removePerson(this);
        home.removePerson(this);
        this.home = null;
        this.workplace = null;
    }

    /**
     * This method fires the person
     */
    public void fire() {
        Logger.log(name + " has been fired");
        workplace.removePerson(this);
        this.workplace = null;
    }

    /**
     * This method moves the person away
     */
    public void moveAway() {
        Logger.log(name + " has moved away");
        this.name = "Moved away";
        if (home != null)
            home.removePerson(this);
        if (workplace != null)
            workplace.removePerson(this);
        this.home = null;
        this.workplace = null;
    }

    /**
     * Getter for the age
     *
     * @return the age of the person
     */
    public int getAge() {
        return age;
    }

    /**
     * Setter for the age
     *
     * @param age the age of the person
     */
    public void setAge(int age) {
        Logger.log("New age of " + name + " is " + age);
        this.age = age;
    }

    /**
     * Getter for the name
     *
     * @return the name of the person
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for the name
     *
     * @param name the name of the person
     */
    public void setName(String name) {
        Logger.log("New name of " + this.name + " is " + name);
        this.name = name;
    }

    /**
     * Getter for the effects
     *
     * @return the effects of the person
     */
    public Set<Effect> getEffects() {
        return effects;
    }

    /**
     * Setter for the effects
     *
     * @param effects the effects of the person
     */
    public void setEffects(@NotNull Set<Effect> effects) {
        Logger.log("New effects of " + name + " are " + effects.toString());
        this.effects = effects;
    }

    /**
     * Getter for the home
     *
     * @return the home of the person
     */
    @JsonIgnore
    public ResidentialBuilding getHome() {
        return home;
    }

    /**
     * Setter for the home
     *
     * @param residentialBuilding the home of the person
     */
    public void setHome(@Nullable ResidentialBuilding residentialBuilding) {
        this.home = residentialBuilding;
        if (residentialBuilding != null) {
            Logger.log("New home of " + name + " is" + residentialBuilding);
        } else {
            Logger.log("New home of " + name + " is null");
        }
    }

    /**
     * Getter for the workplace
     *
     * @return the workplace of the person
     */
    @JsonIgnore
    public Workplace getWorkplace() {
        return workplace;
    }

    /**
     * Setter for the workplace
     *
     * @param workplace the workplace of the person
     */
    public void setWorkplace(@Nullable Workplace workplace) {
        this.workplace = workplace;
        if (workplace != null) {
            Logger.log("New workplace of " + name + " is" + workplace);
            if (calculateDistanceToWork() > 10) {
                addEffect(Effect.WORK_DISTANCE);
            }

        } else {
            Logger.log("New workplace of " + name + " is null");
        }
    }

    /**
     * Getter for the founder
     *
     * @return true if the person is a founder
     */
    public boolean isFounder() {
        return founder;
    }

    /**
     * Setter for the founder
     *
     * @param founder true if the person is a founder
     */
    public void setFounder(boolean founder) {
        this.founder = founder;
    }

    @Override
    public @NotNull String toString() {
        return "Person{" +
                "age=" + age +
                ", name='" + name + '\'' +
                ", effects=" + effects +
                ", home=" + home +
                ", workplace=" + workplace +
                ", founder=" + founder +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person person)) return false;
        return getAge() == person.getAge() && founder == person.founder && Objects.equals(getName(), person.getName()) && Objects.equals(getEffects(), person.getEffects()) && Objects.equals(getHome(), person.getHome()) && Objects.equals(getWorkplace(), person.getWorkplace());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAge(), getName(), getEffects(), founder);
    }
}
