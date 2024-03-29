package controller;

import model.Coordinate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * This class tests all methods of the GameManager class
 */
public class GameManagerTest {

    private GameManager gm;

    /**
     * This code runs before every test case
     */
    @BeforeEach
    void setUp() {
        gm = new GameManager();
    }

    /**
     * Test for initGame method - city name is set
     */
    @Test
    public void testInitGame() {
        String cityName = "New York";
        gm.initGame(cityName);
        String setCityName = GameManager.getGameData().getCityName();
        Assertions.assertEquals(cityName, setCityName, "initGame");
    }

    /**
     * Test for calculateDistance method - UNFINISHED
     */
    @Test
    public void testCalculateDistance() {
        Coordinate start = new Coordinate(2, 5);
        Coordinate finish = new Coordinate(6, 3);

        //List<Coordinate> result = new ArrayList<>();
        List<Coordinate> result = GameManager.findShortestPath(start, finish);
        List<Coordinate> testResult = new ArrayList<>();

        //log the path
        /*
        for (Coordinate element : result) {
            Logger.log( String.valueOf(element.getX()) );
        }
        */
        //Assertions.assertEquals(result,testResult, "GameManager.calculateDistance");
    }

    /**
     * Test for doFinancials
     */
    @Test
    public void testDoFinancials() {
        //TODO
    }

}
