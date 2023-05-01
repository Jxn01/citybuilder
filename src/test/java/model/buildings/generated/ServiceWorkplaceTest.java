package model.buildings.generated;

import model.Coordinate;
import model.Person;
import model.enums.SaturationRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ServiceWorkplaceTest {

    ServiceWorkplace serviceWorkplace;

    Coordinate coords;

    @BeforeEach
    void setUp() {
        this.coords = new Coordinate(1, 32);
        this.serviceWorkplace = new ServiceWorkplace(this.coords);
    }

    @Test
    void addPersonTest() {
        final Person person = new Person(false);

        this.serviceWorkplace.addPerson(person);

        assertTrue(this.serviceWorkplace.getPeople().contains(person));
        assertEquals(SaturationRate.LOW, this.serviceWorkplace.getSaturationRate());
    }

    @Test
    void addPersonThrowExceptionTest() {
        final String MESSAGE = "Maximum capacity reached! Can't add new person!";
        final Person person = new Person(false);
        this.serviceWorkplace.setMaxCapacity(0);


        assertEquals(this.serviceWorkplace.getPeople().size(), 0);
        final RuntimeException expected = assertThrows(RuntimeException.class, () -> this.serviceWorkplace.addPerson(person),
                MESSAGE
        );

        assertEquals(MESSAGE, expected.getMessage());
    }

    @Test
    void removePersonTest() {
        final Person person = new Person(false);

        this.serviceWorkplace.removePerson(person);

        assertFalse(this.serviceWorkplace.getPeople().contains(person));
    }

    @Test
    void getPublicSafetyTest() {
        final int PUBLICSAFETY = 50;
        this.serviceWorkplace = new ServiceWorkplace(this.coords, 0.2, false, new ArrayList<>(), SaturationRate.FULL, PUBLICSAFETY);

        final int actual = this.serviceWorkplace.getPublicSafety();
        assertEquals(PUBLICSAFETY, actual);
    }

    @Test
    void getStatisticsTest() {
        final int PUBLICSAFETY = 50;
        this.serviceWorkplace.setPublicSafety(PUBLICSAFETY);
        final String actual = this.serviceWorkplace.getStatistics();
        final String expected = "Public safety: " + PUBLICSAFETY;
        assertTrue(actual.contains(expected));
    }

    @Test
    void setOnFireTest() {
        this.serviceWorkplace.setOnFire();
        assertTrue(this.serviceWorkplace.isOnFire());
    }

    @Test
    void extinguishTest() {
        this.serviceWorkplace.extinguish();
        assertFalse(this.serviceWorkplace.isOnFire());
    }
}