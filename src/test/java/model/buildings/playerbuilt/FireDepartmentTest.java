package model.buildings.playerbuilt;

import model.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FireDepartmentTest {

    FireDepartment fireDepartment;
    Coordinate coords;

    @BeforeEach
    void setUp() {
        this.coords = new Coordinate(1, 32);
        this.fireDepartment = new FireDepartment(this.coords);
    }

    @Test
    void getMaxFireTrucksTest() {
        final int expected = 121;
        this.fireDepartment.setMaxFireTrucks(expected);
        final int actual = this.fireDepartment.getMaxFireTrucks();
        assertEquals(expected, actual);
    }

    @Test
    void getAvailableFireTrucksTest() {
        final int expected = 121;
        this.fireDepartment.setAvailableFireTrucks(expected);
        final int actual = this.fireDepartment.getAvailableFireTrucks();
        assertEquals(expected, actual);
    }

    @Test
    void getStatisticsTest() {
        final int RANGE = 50;

        this.fireDepartment.setRange(RANGE);

        final String actual = this.fireDepartment.getStatistics();
        final String expected = "Range: " + RANGE;
        assertTrue(actual.contains(expected));
    }

    @Test
    void setOnFireTest() {
        assertDoesNotThrow(() -> this.fireDepartment.setOnFire());
    }

    @Test
    void extinguishTest() {
        assertDoesNotThrow(() -> this.fireDepartment.extinguish());
    }


    @Test
    void effectTest() {
        assertDoesNotThrow(() -> this.fireDepartment.effect());
    }

}