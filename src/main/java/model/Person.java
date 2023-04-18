package model;

import java.util.ArrayList;

/**
 * This class represents a person in the game
 */
public class Person {
    private final int maxsatisfaction = 100;
    private final int minsatisfaction = 20;
    private int age;
    private String name;
    private int satisfaction;
    private int distancceToWork;
    private double deceaseChance;
    private double moveAwayChance;
    private ArrayList<Effect> effects;

    /**
     * Constructor for Person
     * @param age the age of the person
     * @param name the name of the person
     * @param satisfaction the satisfaction of the person
     * @param distancceToWork the distance to work of the person
     * @param deceaseChance the chance of the person to decease
     * @param moveAwayChance the chance of the person to move away
     */
    public Person(int age, String name, int satisfaction, int distancceToWork, double deceaseChance, double moveAwayChance) {
        this.age = age;
        this.name = name;
        this.satisfaction = satisfaction;
        this.distancceToWork = distancceToWork;
        this.deceaseChance = deceaseChance;
        this.moveAwayChance = moveAwayChance;
        this.effects = new ArrayList<>();
    }

    /**
     * This method adds an effect to the person
     * @param effect the effect to add
     */
    public void addEffect(Effect effect) {
        effects.add(effect);
    }

    /**
     * This method removes an effect from the person
     * @param effect the effect to remove
     */
    public void removeEffect(Effect effect) {
        effects.remove(effect);
    }

    /**
     * This method checks if the person is retired
     * @return true if the person is retired
     */
    public boolean isRetired() {
        return age > 65;
    }

    /**
     * This method checks if the person is dead
     * @return true if the person is dead
     */
    public void decease(boolean instantly) {

    }

    /**
     * This method checks if the person is moved away
     * @return true if the person is moved away
     */
    public void moveAway(boolean instantly) {

    }

    /**
     * This method pays the taxes of the person
     * @param budget the budget of the city
     */
    public void payTaxes(Integer budget) {

    }

    /**
     * This method retires the person
     */
    public void retire() {

    }

    /**
     * This method calculates the satisfaction of the person
     */
    public void calculateSatisfaction() {

    }

    /**
     * Getter for the max satisfaction
     * @return the maximum satisfaction of the person
     */
    public int getMaxsatisfaction() {
        return maxsatisfaction;
    }

    /**
     * Getter for the min satisfaction
     * @return the minimum satisfaction of the person
     */
    public int getMinsatisfaction() {
        return minsatisfaction;
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
        this.name = name;
    }

    /**
     * Getter for the satisfaction
     * @return the satisfaction of the person
     */
    public int getSatisfaction() {
        return satisfaction;
    }

    /**
     * Setter for the satisfaction
     * @param satisfaction the satisfaction of the person
     */
    public void setSatisfaction(int satisfaction) {
        this.satisfaction = satisfaction;
    }

    /**
     * Getter for the distance to work
     * @return the distance to work of the person
     */
    public int getDistancceToWork() {
        return distancceToWork;
    }

    /**
     * Setter for the distance to work
     * @param distancceToWork the distance to work of the person
     */
    public void setDistancceToWork(int distancceToWork) {
        this.distancceToWork = distancceToWork;
    }

    /**
     * Getter for the decease chance
     * @return the decease chance of the person
     */
    public double getDeceaseChance() {
        return deceaseChance;
    }

    /**
     * Setter for the decease chance
     * @param deceaseChance the decease chance of the person
     */
    public void setDeceaseChance(double deceaseChance) {
        this.deceaseChance = deceaseChance;
    }

    /**
     * Getter for the move away chance
     * @return the move away chance of the person
     */
    public double getMoveAwayChance() {
        return moveAwayChance;
    }

    /**
     * Setter for the move away chance
     * @param moveAwayChance the move away chance of the person
     */
    public void setMoveAwayChance(double moveAwayChance) {
        this.moveAwayChance = moveAwayChance;
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
        this.effects = effects;
    }

    @Override
    public String toString() {
        return "Person{" +
                "maxsatisfaction=" + maxsatisfaction +
                ", minsatisfaction=" + minsatisfaction +
                ", age=" + age +
                ", name='" + name + '\'' +
                ", satisfaction=" + satisfaction +
                ", distancceToWork=" + distancceToWork +
                ", deceaseChance=" + deceaseChance +
                ", moveAwayChance=" + moveAwayChance +
                ", effects=" + effects +
                '}';
    }
}
