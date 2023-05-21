package model.buildings.generated;

import model.Coordinate;
import model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class IndustrialWorkplaceTest {

    IndustrialWorkplace industrialWorkplace;
    Coordinate coords;

    @BeforeEach
    void setUp() {
        this.coords = new Coordinate(1, 32);
        this.industrialWorkplace = new IndustrialWorkplace(this.coords);
    }

    @Test
    void getStatisticsTest() {
        final int PUBLICSAFETY = 50;
        this.industrialWorkplace.setPublicSafety(PUBLICSAFETY);
        final String actual = this.industrialWorkplace.getStatistics();
        final String expected = "Public safety: " + PUBLICSAFETY;
        assertTrue(actual.contains(expected));
    }

    @Test
    void getPublicSafetyTest() {
        final int PUBLICSAFETY = 50;
        this.industrialWorkplace = new IndustrialWorkplace(this.coords, 0.2, false, new ArrayList<>(), PUBLICSAFETY);
        this.industrialWorkplace.getCoords().setX(0);
        this.industrialWorkplace.getCoords().setY(0);
        final int actual = this.industrialWorkplace.getPublicSafety();
        assertEquals(PUBLICSAFETY, actual);
    }

    @Test
    void setOnFireTest() {
        this.industrialWorkplace.setOnFire();
        assertTrue(this.industrialWorkplace.isOnFire());
    }

    @Test
    void extinguishTest() {
        this.industrialWorkplace.extinguish();
        assertFalse(this.industrialWorkplace.isOnFire());
    }

    @Test
    void addPersonTest() {
        final Person person = new Person(false);

        this.industrialWorkplace.addPerson(person);

        assertTrue(this.industrialWorkplace.getPeople().contains(person));
    }

    @Test
    void addPersonThrowExceptionTest() {
        final String MESSAGE = "Maximum capacity reached! Can't add new person!";
        final Person person = new Person(false);
        this.industrialWorkplace.setMaxCapacity(0);

        assertEquals(this.industrialWorkplace.getPeople().size(), 0);
        final RuntimeException expected = assertThrows(RuntimeException.class, () -> this.industrialWorkplace.addPerson(person),
                MESSAGE
        );

        assertEquals(MESSAGE, expected.getMessage());
    }

    @Test
    void removePersonTest() {
        final Person person = new Person(false);
        this.industrialWorkplace.removePerson(person);
        assertFalse(this.industrialWorkplace.getPeople().contains(person));
    }
}