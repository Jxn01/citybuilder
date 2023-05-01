package model.buildings.playerbuilt;

import model.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PoliceStationTest {

    PoliceStation policeStation;

    Coordinate coords;

    @BeforeEach
    void setUp() {
        this.coords = new Coordinate(1, 32);
        this.policeStation = new PoliceStation(coords);
    }

    @Test
    void getStatisticsTest() {
        final int RANGE = 50;
        this.policeStation = new PoliceStation(this.coords, 0.2, false, 10, 21, 10);
        this.policeStation.setRange(RANGE);
        final String actual = this.policeStation.getStatistics();
        final String expected = "Range: " + RANGE;
        assertTrue(actual.contains(expected));
    }

    @Test
    void setOnFireTest() {
        assertDoesNotThrow(() -> this.policeStation.setOnFire());
    }

    @Test
    void extinguishTest() {
        assertDoesNotThrow(() -> this.policeStation.extinguish());
    }

    @Test
    void enableEffectTest() {
        assertDoesNotThrow(() -> this.policeStation.enableEffect());
    }

    @Test
    void disableEffectTest() {
        assertDoesNotThrow(() -> this.policeStation.disableEffect());
    }

    @Test
    void effectTest() {
        assertDoesNotThrow(() -> this.policeStation.effect());
    }
}