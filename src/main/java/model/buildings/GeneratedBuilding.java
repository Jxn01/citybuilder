package model.buildings;

import java.util.ArrayList;
import model.Person;

public abstract class GeneratedBuilding extends Building {
    protected ArrayList<Person> people;
    protected SaturationRate saturationRate;
    protected int publicSafety;
}
