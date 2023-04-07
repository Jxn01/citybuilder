package model;

import java.util.ArrayList;

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

    public Person(int age, String name, int satisfaction, int distancceToWork, double deceaseChance, double moveAwayChance) {
        this.age = age;
        this.name = name;
        this.satisfaction = satisfaction;
        this.distancceToWork = distancceToWork;
        this.deceaseChance = deceaseChance;
        this.moveAwayChance = moveAwayChance;
        this.effects = new ArrayList<>();
    }

    public void addEffect(Effect effect){
        effects.add(effect);
    }

    public void removeEffect(Effect effect){
        effects.remove(effect);
    }

    public boolean isRetired(){
        return age > 65;
    }

    public void decease(boolean instantly){

    }

    public void moveAway(boolean instantly){

    }

    public void payTaxes(Integer budget){

    }

    public void retire(){

    }

    public void calculateSatisfaction(){

    }

    public int getMaxsatisfaction() {
        return maxsatisfaction;
    }

    public int getMinsatisfaction() {
        return minsatisfaction;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSatisfaction() {
        return satisfaction;
    }

    public void setSatisfaction(int satisfaction) {
        this.satisfaction = satisfaction;
    }

    public int getDistancceToWork() {
        return distancceToWork;
    }

    public void setDistancceToWork(int distancceToWork) {
        this.distancceToWork = distancceToWork;
    }

    public double getDeceaseChance() {
        return deceaseChance;
    }

    public void setDeceaseChance(double deceaseChance) {
        this.deceaseChance = deceaseChance;
    }

    public double getMoveAwayChance() {
        return moveAwayChance;
    }

    public void setMoveAwayChance(double moveAwayChance) {
        this.moveAwayChance = moveAwayChance;
    }

    public ArrayList<Effect> getEffects() {
        return effects;
    }

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
