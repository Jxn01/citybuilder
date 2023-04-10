package model.buildings;

import model.Person;

import java.util.ArrayList;

public abstract class GeneratedBuilding extends Building {
    protected ArrayList<Person> people;
    protected SaturationRate saturationRate;
    protected int publicSafety;
}
