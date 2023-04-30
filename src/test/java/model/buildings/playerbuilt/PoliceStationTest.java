package model.buildings.playerbuilt;

import model.Coordinate;
import model.buildings.generated.IndustrialWorkplace;
import model.enums.SaturationRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PoliceStationTest {

    PoliceStation policeStation;

    Coordinate coords;

    @BeforeEach
    void setUp() {
        this.coords = new Coordinate(1,32);
        this.policeStation = new PoliceStation(coords);
    }

    @Test
    void getStatistics() {
        final int RANGE = 50;
        this.policeStation = new PoliceStation(this.coords, 0.2, false, 10, 21, 10);
        this.policeStation.setRange(RANGE);
        final String  actual = this.policeStation.getStatistics();
        final String  expected = "Range: " + RANGE;
        assertTrue(actual.contains(expected));
    }

    @Test
    void setOnFire() {
        assertDoesNotThrow(() -> this.policeStation.setOnFire());
    }

    @Test
    void extinguish() {
        assertDoesNotThrow(() -> this.policeStation.extinguish());
    }

    @Test
    void enableEffect() {
        assertDoesNotThrow(() -> this.policeStation.enableEffect());
    }

    @Test
    void disableEffect() {
        assertDoesNotThrow(() -> this.policeStation.disableEffect());
    }

    @Test
    void effect() {
        assertDoesNotThrow(() -> this.policeStation.effect());
    }
}