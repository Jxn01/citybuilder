package model.buildings.playerbuilt;

import model.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ForestTest {

    Forest forest;

    Coordinate coords;

    @BeforeEach
    void setUp() {
        this.coords = new Coordinate(1, 32);
        this.forest = new Forest(this.coords);
    }


    @Test
    void isNotFullyGrownTest() {
        this.forest = new Forest(this.coords, 1.0, false, 1, 1, 1, 10, 1);
        final int actual = this.forest.getGrowStage();
        this.forest.grow();
        final int expected = this.forest.getGrowStage() + 1;
        assertNotEquals(expected, actual);
    }

    @Test
    void isFullyGrownTest() {
        final int GROWSTAGE = this.forest.getGrowStage();
        this.forest.setGrowTime(GROWSTAGE);

        this.forest.grow();

        final int actual = this.forest.getGrowStage();
        assertEquals(GROWSTAGE, actual);
    }


    @Test
    void getStatisticsTest() {
        final int GROWSTAGE = 50;
        this.forest.setGrowStage(GROWSTAGE);

        final String actual = this.forest.getStatistics();
        final String expected = "Forest age: " + GROWSTAGE;
        assertTrue(actual.contains(expected));
    }

    @Test
    void setOnFireTest() {
        this.forest.setOnFire();
        assertTrue(this.forest.isOnFire());
    }

    @Test
    void extinguishTest() {
        this.forest.extinguish();
        assertFalse(this.forest.isOnFire());
    }

    @Test
    void getAddressTest() {
        this.forest.setAddress("POZ");
        assertEquals("POZ", this.forest.getAddress());
    }

    @Test
    void getFirePossibilityTest() {
        this.forest.setFirePossibility(2.1);
        assertEquals(2.1, this.forest.getFirePossibility());
    }

    @Test
    void getCoordsTest() {
        final Coordinate coordinate = new Coordinate(2, 2);

        this.forest.setCoords(coordinate);
        assertEquals(coordinate, this.forest.getCoords());
    }

    @Test
    void enableEffectTest() {
        assertDoesNotThrow(() -> this.forest.enableEffect());
    }

    @Test
    void disableEffectTest() {
        assertDoesNotThrow(() -> this.forest.disableEffect());
    }

    @Test
    void effectTest() {
        assertDoesNotThrow(() -> this.forest.effect());
    }

}