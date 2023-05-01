package model.buildings.playerbuilt;

import model.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StadiumTest {

    Stadium stadium;
    private Coordinate topLeftCoords;
    private Coordinate topRightCoords;
    private Coordinate bottomLeftCoords;
    private Coordinate bottomRightCoords;

    @BeforeEach
    void setUp() {
        this.topLeftCoords = new Coordinate(1, 32);
        this.topRightCoords = new Coordinate(1, 32);
        this.bottomLeftCoords = new Coordinate(1, 32);
        this.bottomRightCoords = new Coordinate(1, 32);
        this.stadium = new Stadium(topLeftCoords, topRightCoords, bottomLeftCoords, bottomRightCoords);
    }


    @Test
    void changeCoordsTest() {
        this.stadium.setTopRightCoords(new Coordinate(2, 32));
        this.stadium.setBottomRightCoords(new Coordinate(2, 32));
        this.stadium.setBottomLeftCoords(new Coordinate(2, 32));

        assertEquals(this.stadium.getTopRightCoords().getX(), 2);
        assertEquals(this.stadium.getBottomRightCoords().getX(), 2);
        assertEquals(this.stadium.getBottomLeftCoords().getX(), 2);
    }

    @Test
    void getStatisticsTest() {
        final int RANGE = 50;
        this.stadium = new Stadium(this.topRightCoords, 0.2, false, 10, 21, 10);
        this.stadium.setRange(RANGE);
        final String actual = this.stadium.getStatistics();
        final String expected = "Range: " + RANGE;
        assertTrue(actual.contains(expected));
    }

    @Test
    void setOnFireTest() {
        assertDoesNotThrow(() -> this.stadium.setOnFire());
    }

    @Test
    void extinguishTest() {
        assertDoesNotThrow(() -> this.stadium.extinguish());
    }

    @Test
    void enableEffectTest() {
        assertDoesNotThrow(() -> this.stadium.enableEffect());
    }

    @Test
    void disableEffectTest() {
        assertDoesNotThrow(() -> this.stadium.disableEffect());
    }

    @Test
    void effectTest() {
        assertDoesNotThrow(() -> this.stadium.effect());
    }

}