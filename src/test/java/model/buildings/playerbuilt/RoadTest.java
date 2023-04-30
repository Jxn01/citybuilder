package model.buildings.playerbuilt;

import model.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoadTest {

    Road road;

    Coordinate coords;

    @BeforeEach
    void setUp() {
        this.coords = new Coordinate(1,32);
        this.road = new Road(coords);
    }

    @Test
    void getStatistics() {
        final int COST = 50;

        this.road = new Road(this.coords, 0.2, false, 10, 21);

        this.road.setBuildCost(COST);
        final String  actual = this.road.getStatistics();
        final String  expected = "Build cost: " + COST;
        assertTrue(actual.contains(expected));
    }

    @Test
    void setOnFire() {
        assertDoesNotThrow(() -> this.road.setOnFire());
    }

    @Test
    void extinguish() {
        assertDoesNotThrow(() -> this.road.extinguish());
    }
}