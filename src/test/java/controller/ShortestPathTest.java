package controller;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.MutableGraph;
import model.Coordinate;
import model.GameData;
import model.buildings.generated.IndustrialWorkplace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ShortestPathTest {

    @BeforeEach
    void setUp() {
        GameManager gm = new GameManager();
        gm.setGameData(new GameData("Test", 1, 1, 1, 1));
    }

    @Test
    public void findShortestPathSimpleGraphTest() {
        // Create a simple graph with 5 coordinates
        MutableGraph<Coordinate> graph = GameManager.getGraph();
        Coordinate A = new Coordinate(0, 0);
        Coordinate B = new Coordinate( 1, 0);
        Coordinate C = new Coordinate( 1, 1);
        Coordinate D = new Coordinate( 2, 0);
        Coordinate E = new Coordinate( 2, 1);
        graph.putEdge(A, B);
        graph.putEdge(A, C);
        graph.putEdge(B, D);
        graph.putEdge(C, D);
        graph.putEdge(C, E);
        graph.putEdge(D, E);

        // Test the shortest path from A to E
        List<Coordinate> expectedPath = Arrays.asList(A, C, E);
        List<Coordinate> actualPath = GameManager.findShortestPath(A, E);
        assertEquals(expectedPath, actualPath);
    }

    @Test
    public void findShortestPathNoPathTest() {
        // Create a graph with no path between start and end coordinates
        MutableGraph<Coordinate> graph = GameManager.getGraph();
        Coordinate A = new Coordinate( 0, 0);
        Coordinate B = new Coordinate( 1, 0);
        Coordinate C = new Coordinate( 1, 1);
        Coordinate D = new Coordinate( 1, 2);
        graph.putEdge(A, B);
        graph.putEdge(B, D);

        // Test when there is no path between A and C
        List<Coordinate> actualPath = GameManager.findShortestPath(A, C);
        assertNull(actualPath);
    }

    @Test
    public void findShortestPathSameStartAndEndTest() {
        // Create a graph with a single coordinate
        MutableGraph<Coordinate> graph = GameManager.getGraph();
        Coordinate A = new Coordinate(0, 0);
        graph.addNode(A);

        // Test when the start and end coordinates are the same
        List<Coordinate> actualPath = GameManager.findShortestPath(A, A);
        assertNull(actualPath);
    }
}

