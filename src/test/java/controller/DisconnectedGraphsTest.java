package controller;

import com.google.common.graph.MutableGraph;
import model.Coordinate;
import model.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DisconnectedGraphsTest {

    @BeforeEach
    void setUp() {
        GameManager gm = new GameManager();
        gm.setGameData(new GameData("Test", 1, 1, 1, 1));
    }

    @Test
    public void countDisconnectedGraphsSingleGraphTest() {
        // Create a graph with a single connected component
        MutableGraph<Coordinate> graph = GameManager.getGraph();
        Coordinate A = new Coordinate(0, 0);
        Coordinate B = new Coordinate(1, 0);
        Coordinate C = new Coordinate(1, 1);
        graph.putEdge(A, B);
        graph.putEdge(B, C);

        // Test when there is only one connected component
        int expectedCount = 1;
        int actualCount = GameManager.countDisconnectedGraphs(graph);
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void countDisconnectedGraphsMultipleGraphsTest() {
        // Create a graph with multiple disconnected components
        MutableGraph<Coordinate> graph = GameManager.getGraph();
        Coordinate A = new Coordinate(0, 0);
        Coordinate B = new Coordinate(1, 0);
        Coordinate C = new Coordinate(1, 1);
        Coordinate D = new Coordinate(2, 0);
        Coordinate E = new Coordinate(3, 3);
        graph.putEdge(A, B);
        graph.putEdge(B, C);
        graph.putEdge(D, E);

        // Test when there are multiple disconnected components
        int expectedCount = 2;
        int actualCount = GameManager.countDisconnectedGraphs(graph);
        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void countDisconnectedGraphsEmptyGraphTest() {
        // Create an empty graph
        MutableGraph<Coordinate> graph = GameManager.getGraph();

        // Test when the graph is empty
        int expectedCount = 0;
        int actualCount = GameManager.countDisconnectedGraphs(graph);
        assertEquals(expectedCount, actualCount);
    }
}

