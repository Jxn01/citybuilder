package model.buildings.generated;

import model.Coordinate;
import model.Person;
import model.enums.SaturationRate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class ResidentialBuildingTest {

    ResidentialBuilding residentialBuilding;

    Coordinate coords;

    @BeforeEach
    void setUp() {
        this.coords = new Coordinate(1,32);
        this.residentialBuilding = new ResidentialBuilding(this.coords);
    }

    @Test
    void addPerson() {
        final Person person = new Person(false);

        this.residentialBuilding.addPerson(person);

        assertTrue(this.residentialBuilding.getPeople().contains(person));
        assertEquals(SaturationRate.LOW, this.residentialBuilding.getSaturationRate());
    }

    @Test
    void throw_when_addPerson() {
        final String MESSAGE = "Maximum capacity reached! Can't add new person!";
        final Person person = new Person(false);
        person.setFounder(false);
        person.setAge(10);
        person.setEffects(new ArrayList<>());
        this.residentialBuilding.setMaxCapacity(0);


        assertEquals(this.residentialBuilding.getPeople().size(), 0);
        final RuntimeException expected = assertThrows( RuntimeException.class, () -> this.residentialBuilding.addPerson(person),
                MESSAGE
        );

        assertEquals(MESSAGE, expected.getMessage());
    }

    @Test
    void removePerson() {
        final Person person = new Person(false);

        this.residentialBuilding.removePerson(person);

        assertFalse(this.residentialBuilding.getPeople().contains(person));
    }

    @Test
    void getStatistics() {
        final int PUBLIC_SAFERY = 50;
        this.residentialBuilding.setPublicSafety(PUBLIC_SAFERY);
        final String  actual = this.residentialBuilding.getStatistics();
        final String  expected = "Public safety: " + PUBLIC_SAFERY;
        assertTrue(actual.contains(expected));
    }

    @Test
    void getPublicSafety() {
        final int PUBLIC_SAFERY = 50;
        this.residentialBuilding = new ResidentialBuilding(this.coords, 0.2, false, new ArrayList<>(), SaturationRate.FULL, PUBLIC_SAFERY);

        final int actual = this.residentialBuilding.getPublicSafety();
        assertEquals(PUBLIC_SAFERY, actual);
    }

    @Test
    void setOnFire() {
        this.residentialBuilding.setOnFire();
        assertTrue(this.residentialBuilding.isOnFire());
    }

    @Test
    void extinguish() {
        this.residentialBuilding.extinguish();
        assertFalse(this.residentialBuilding.isOnFire());
    }
}