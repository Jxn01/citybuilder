package model.buildings.generated;

import model.Coordinate;
import model.Person;
import model.enums.SaturationRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.*;

class IndustrialWorkplaceTest {

    IndustrialWorkplace industrialWorkplace;

    Coordinate coords;

    @BeforeEach
    void setUp() {
        this.coords = new Coordinate(1,32);
        this.industrialWorkplace = new IndustrialWorkplace(this.coords);
    }

    @Test
    void getStatistics() {
        final int PUBLIC_SAFERY = 50;
        this.industrialWorkplace.setPublicSafety(PUBLIC_SAFERY);
        final String  actual = this.industrialWorkplace.getStatistics();
        final String  expected = "Public safety: " + PUBLIC_SAFERY;
        assertTrue(actual.contains(expected));
    }

    @Test
    void getPublicSafety() {
        final int PUBLIC_SAFERY = 50;
        this.industrialWorkplace = new IndustrialWorkplace(this.coords, 0.2, false, new ArrayList<>(), SaturationRate.FULL, PUBLIC_SAFERY);
        this.industrialWorkplace.getCoords().setX(0);
        this.industrialWorkplace.getCoords().setY(0);
        final int actual = this.industrialWorkplace.getPublicSafety();
        assertEquals(PUBLIC_SAFERY, actual);
    }

    @Test
    void updateSaturationRateSetSaturationRateToLow() {
        final SaturationRate expected = SaturationRate.LOW;
        final int MAX_CAP = 80;
        this.industrialWorkplace.setMaxCapacity(MAX_CAP);
        this.industrialWorkplace.addPerson(new Person());

        this.industrialWorkplace.updateSaturationRate();

        final SaturationRate actual = this.industrialWorkplace.getSaturationRate();
        assertEquals(expected, actual);
    }

    @Test
    void updateSaturationRateSetSaturationRateToMEDIUM() {
        final SaturationRate expected = SaturationRate.MEDIUM;
        final int MAX_CAP = 8;
        this.industrialWorkplace.setMaxCapacity(MAX_CAP);
        this.industrialWorkplace.addPerson(new Person());
        this.industrialWorkplace.addPerson(new Person());

        this.industrialWorkplace.updateSaturationRate();

        final SaturationRate actual = this.industrialWorkplace.getSaturationRate();
        assertEquals(expected, actual);
    }

    @Test
    void updateSaturationRateSetSaturationRateToHIGH() {
        final SaturationRate expected = SaturationRate.HIGH;
        final int MAX_CAP = 5;
        this.industrialWorkplace.setMaxCapacity(MAX_CAP);
        this.industrialWorkplace.addPerson(new Person());
        this.industrialWorkplace.addPerson(new Person());

        this.industrialWorkplace.updateSaturationRate();

        final SaturationRate actual = this.industrialWorkplace.getSaturationRate();
        assertEquals(expected, actual);
    }

    @Test
    void updateSaturationRateSetSaturationRateToFULL() {
        final SaturationRate expected = SaturationRate.FULL;
        final int MAX_CAP = 2;
        this.industrialWorkplace.setMaxCapacity(MAX_CAP);
        this.industrialWorkplace.addPerson(new Person());
        this.industrialWorkplace.addPerson(new Person());

        this.industrialWorkplace.updateSaturationRate();

        final SaturationRate actual = this.industrialWorkplace.getSaturationRate();
        assertEquals(expected, actual);
    }

    @Test
    void setOnFire() {
        this.industrialWorkplace.setOnFire();
        assertTrue(this.industrialWorkplace.isOnFire());
    }

    @Test
    void extinguish() {
        this.industrialWorkplace.extinguish();
        assertFalse(this.industrialWorkplace.isOnFire());
    }

    @Test
    void addPerson() {
        final Person person = new Person(false);

        this.industrialWorkplace.addPerson(person);

        assertTrue(this.industrialWorkplace.getPeople().contains(person));
        assertEquals(SaturationRate.LOW, this.industrialWorkplace.getSaturationRate());
    }

    @Test
    void throw_when_addPerson() {
        final String MESSAGE = "Maximum capacity reached! Can't add new person!";
        final Person person = new Person(false);
        this.industrialWorkplace.setMaxCapacity(0);


        assertEquals(this.industrialWorkplace.getPeople().size(), 0);
       final RuntimeException expected = assertThrows( RuntimeException.class, () -> this.industrialWorkplace.addPerson(person),
               MESSAGE
        );

        assertEquals(MESSAGE, expected.getMessage());
    }

    @Test
    void removePerson() {
        final Person person = new Person(false);

        this.industrialWorkplace.removePerson(person);

        assertFalse(this.industrialWorkplace.getPeople().contains(person));
    }
}