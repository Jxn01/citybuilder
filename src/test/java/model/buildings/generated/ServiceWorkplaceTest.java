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
        this.coords = new Coordinate(1,32);
        this.serviceWorkplace = new ServiceWorkplace(this.coords);
    }

    @Test
    void addPerson() {
        final Person person = new Person(false);

        this.serviceWorkplace.addPerson(person);

        assertTrue(this.serviceWorkplace.getPeople().contains(person));
        assertEquals(SaturationRate.LOW, this.serviceWorkplace.getSaturationRate());
    }

    @Test
    void throw_when_addPerson() {
        final String MESSAGE = "Maximum capacity reached! Can't add new person!";
        final Person person = new Person(false);
        this.serviceWorkplace.setMaxCapacity(0);


        assertEquals(this.serviceWorkplace.getPeople().size(), 0);
        final RuntimeException expected = assertThrows( RuntimeException.class, () -> this.serviceWorkplace.addPerson(person),
                MESSAGE
        );

        assertEquals(MESSAGE, expected.getMessage());
    }

    @Test
    void removePerson() {
        final Person person = new Person(false);

        this.serviceWorkplace.removePerson(person);

        assertFalse(this.serviceWorkplace.getPeople().contains(person));
    }

    @Test
    void getPublicSafety() {
        final int PUBLIC_SAFERY = 50;
        this.serviceWorkplace = new ServiceWorkplace(this.coords, 0.2, false, new ArrayList<>(), SaturationRate.FULL, PUBLIC_SAFERY);

        final int actual = this.serviceWorkplace.getPublicSafety();
        assertEquals(PUBLIC_SAFERY, actual);
    }

    @Test
    void getStatistics() {
        final int PUBLIC_SAFERY = 50;
        this.serviceWorkplace.setPublicSafety(PUBLIC_SAFERY);
        final String  actual = this.serviceWorkplace.getStatistics();
        final String  expected = "Public safety: " + PUBLIC_SAFERY;
        assertTrue(actual.contains(expected));
    }

    @Test
    void setOnFire() {
        this.serviceWorkplace.setOnFire();
        assertTrue(this.serviceWorkplace.isOnFire());
    }

    @Test
    void extinguish() {
        this.serviceWorkplace.extinguish();
        assertFalse(this.serviceWorkplace.isOnFire());
    }
}