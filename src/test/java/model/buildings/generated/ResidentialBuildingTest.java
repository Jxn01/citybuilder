package model.buildings.generated;

import model.Coordinate;
import model.Person;
import model.enums.SaturationRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class ResidentialBuildingTest {

    ResidentialBuilding residentialBuilding;

    Coordinate coords;

    @BeforeEach
    void setUp() {
        this.coords = new Coordinate(1, 32);
        this.residentialBuilding = new ResidentialBuilding(this.coords);
    }

    @Test
    void addPersonTest() {
        final Person person = new Person(false);

        this.residentialBuilding.addPerson(person);

        assertTrue(this.residentialBuilding.getPeople().contains(person));
    }

    @Test
    void addPersonThrowExceptionTest() {
        final String MESSAGE = "Maximum capacity reached! Can't add new person!";
        final Person person = new Person(false);
        person.setFounder(false);
        person.setAge(10);
        person.setEffects(new TreeSet<>());
        this.residentialBuilding.setMaxCapacity(0);


        assertEquals(this.residentialBuilding.getPeople().size(), 0);
        final RuntimeException expected = assertThrows(RuntimeException.class, () -> this.residentialBuilding.addPerson(person),
                MESSAGE
        );

        assertEquals(MESSAGE, expected.getMessage());
    }

    @Test
    void removePersonTest() {
        final Person person = new Person(false);

        this.residentialBuilding.removePerson(person);

        assertFalse(this.residentialBuilding.getPeople().contains(person));
    }

    @Test
    void getStatisticsTest() {
        final int PUBLICSAFETY = 50;
        this.residentialBuilding.setPublicSafety(PUBLICSAFETY);
        final String actual = this.residentialBuilding.getStatistics();
        final String expected = "Public safety: " + PUBLICSAFETY;
        assertTrue(actual.contains(expected));
    }

    @Test
    void getPublicSafetyTest() {
        final int PUBLICSAFETY = 50;
        this.residentialBuilding = new ResidentialBuilding(this.coords, 0.2, false, new ArrayList<>(), SaturationRate.FULL, PUBLICSAFETY);

        final int actual = this.residentialBuilding.getPublicSafety();
        assertEquals(PUBLICSAFETY, actual);
    }

    @Test
    void setOnFireTest() {
        this.residentialBuilding.setOnFire();
        assertTrue(this.residentialBuilding.isOnFire());
    }

    @Test
    void extinguishTest() {
        this.residentialBuilding.extinguish();
        assertFalse(this.residentialBuilding.isOnFire());
    }
}