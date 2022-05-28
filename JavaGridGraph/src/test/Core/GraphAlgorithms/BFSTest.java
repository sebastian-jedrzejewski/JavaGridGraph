package Core.GraphAlgorithms;

import Core.Graph;
import Core.Helpers.Dimensions2D;
import Core.Helpers.Range;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class BFSTest {

    @Test
    public void testIsGraphConnected() {
        Dimensions2D size = new Dimensions2D(50, 50);
        Random random = new Random();
        Range edgeWeight = new Range(0, 1);

        /* Generating not directed graph which is disconnected and checking if BFS checks connectivity properly */
        Range edgeCount = new Range(0, 1);
        Graph graphNotDirectedDisconnected = GraphUtils.generate(size, random, edgeWeight, edgeCount);
        BFS bfs1 = new BFS(graphNotDirectedDisconnected);
        assertFalse("Not directed graph should be disconnected!", bfs1.isGraphConnected());

        /* Generating not directed graph which is connected and checking if BFS checks connectivity properly */
        edgeCount.setLow(4);
        edgeCount.setHigh(4);
        Graph graphNotDirectedConnected = GraphUtils.generate(size, random, edgeWeight, edgeCount);
        BFS bfs2 = new BFS(graphNotDirectedConnected);
        assertTrue("Not directed graph should be connected!", bfs2.isGraphConnected());

        /* Generating directed graph which is disconnected and checking if BFS checks connectivity properly */
        Range inputEdgeCount = new Range(0, 1);
        Range outputEdgeCount = new Range(0, 1);
        Graph graphDirectedDisconnected = GraphUtils.generate(size, random, edgeWeight, inputEdgeCount, outputEdgeCount);
        BFS bfs3 = new BFS(graphDirectedDisconnected);
        assertFalse("Directed graph should be disconnected!", bfs3.isGraphConnected());

        /* Generating directed graph which is connected and checking if BFS checks connectivity properly */
        inputEdgeCount.setLow(4);
        inputEdgeCount.setHigh(4);
        outputEdgeCount.setLow(4);
        outputEdgeCount.setHigh(4);
        Graph graphDirectedConnected = GraphUtils.generate(size, random, edgeWeight, inputEdgeCount, outputEdgeCount);
        BFS bfs4 = new BFS(graphDirectedConnected);
        assertTrue("Directed graph should be connected!", bfs4.isGraphConnected());
    }
}