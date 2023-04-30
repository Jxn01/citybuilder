package model.buildings.playerbuilt;

import model.Coordinate;
import model.buildings.generated.IndustrialWorkplace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FireDepartmentTest {

    FireDepartment fireDepartment;
    Coordinate coords;

    @BeforeEach
    void setUp() {
        this.coords = new Coordinate(1,32);
        this.fireDepartment = new FireDepartment(this.coords);
    }

    @Test
    void getMaxFireTrucks() {
        final int expected = 121;
        this.fireDepartment.setMaxFireTrucks(expected);
        final int actual = this.fireDepartment.getMaxFireTrucks();
        assertEquals(expected, actual);
    }

    @Test
    void getAvailableFireTrucks() {
        final int expected = 121;
        this.fireDepartment.setAvailableFireTrucks(expected);
        final int actual = this.fireDepartment.getAvailableFireTrucks();
        assertEquals(expected, actual);
    }

    @Test
    void getStatistics() {
        final int RANGE = 50;

        this.fireDepartment.setRange(RANGE);

        final String  actual = this.fireDepartment.getStatistics();
        final String  expected = "Range: " + RANGE;
        assertTrue(actual.contains(expected));
    }

    @Test
    void setOnFire() {
        assertDoesNotThrow(() -> this.fireDepartment.setOnFire());
    }

    @Test
    void extinguish() {
        assertDoesNotThrow(() -> this.fireDepartment.extinguish());
    }

    @Test
    void enableEffect() {
        assertDoesNotThrow(() -> this.fireDepartment.enableEffect());
    }

    @Test
    void disableEffect() {
        assertDoesNotThrow(() -> this.fireDepartment.disableEffect());
    }

    @Test
    void effect() {
        assertDoesNotThrow(() -> this.fireDepartment.effect());
    }

    @Test
    void testToString() {
        final int RANGE = 50;
        this.fireDepartment = new FireDepartment(this.coords, 1.0, false, 1, 1, RANGE, 1, 1 );
        final String  actual = this.fireDepartment.toString();
        final String  expected = ", range=" + RANGE;
        assertTrue(actual.contains(expected));
    }
}