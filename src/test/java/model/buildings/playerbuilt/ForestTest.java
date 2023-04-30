package model.buildings.playerbuilt;

import model.Coordinate;
import model.buildings.generated.IndustrialWorkplace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;

import static org.junit.jupiter.api.Assertions.*;

class ForestTest {

    Forest forest;

    Coordinate coords;

    @BeforeEach
    void setUp() {
        this.coords = new Coordinate(1,32);
        this.forest = new Forest(this.coords);
    }


    @Test
    void is_not_fully_grown_when_call_grow() {
        this.forest = new Forest(this.coords, 1.0, false, 1, 1, 1, 10, 1 );
        final int actual = this.forest.getGrowStage();
        this.forest.grow();
        final int expected = this.forest.getGrowStage() + 1;
        assertNotEquals(expected, actual);
    }

    @Test
    void is_fully_grown_when_call_grow() {
        final int GROW_STAGE = this.forest.getGrowStage();
        this.forest.setGrowTime(GROW_STAGE);

        this.forest.grow();

        final int actual = this.forest.getGrowStage();
        assertEquals(GROW_STAGE, actual);
    }


    @Test
    void getStatistics() {
        final int GROW_STAGE = 50;
        this.forest.setGrowStage(GROW_STAGE);

        final String  actual = this.forest.getStatistics();
        final String  expected = "Forest age: " + GROW_STAGE;
        assertTrue(actual.contains(expected));
    }

    @Test
    void setOnFire() {
        this.forest.setOnFire();
        assertTrue(this.forest.isOnFire());
    }

    @Test
    void extinguish() {
        this.forest.extinguish();
        assertFalse(this.forest.isOnFire());
    }

    @Test
    void getAddress() {
        this.forest.setAddress("POZ");
        assertEquals("POZ", this.forest.getAddress());
    }

    @Test
    void getFirePossibility() {
        this.forest.setFirePossibility(2.1);
        assertEquals(2.1, this.forest.getFirePossibility());
    }

    @Test
    void getCoords() {
        final Coordinate coordinate = new Coordinate(2,2);

        this.forest.setCoords(coordinate );
        assertEquals(coordinate, this.forest.getCoords());
    }

    @Test
    void enableEffect() {
        assertDoesNotThrow(() -> this.forest.enableEffect());
    }

    @Test
    void disableEffect() {
        assertDoesNotThrow(() -> this.forest.disableEffect());
    }

    @Test
    void effect() {
        assertDoesNotThrow(() -> this.forest.effect());
    }

    @Test
    void testToString() {
        final int RANGE = 40;
        final int COST = 50;
        final int TIME = 60;
        this.forest = new Forest(this.coords, 1.0, false, 1, 1, RANGE, TIME, 1 );
        this.forest.setMaintenanceCost(COST);
        this.forest.setBuildCost(COST);
        final String  actual = this.forest.toString();
        final String  expected = ", range=" + RANGE;
        assertTrue(actual.contains(expected));
        assertEquals(this.forest.getMaintenanceCost(), COST);
        assertEquals(this.forest.getRange(), RANGE);
        assertEquals(this.forest.getGrowTime(), TIME);
        assertEquals(this.forest.getBuildCost(), COST);
    }
}