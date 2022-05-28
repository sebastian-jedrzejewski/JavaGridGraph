package Core.GraphAlgorithms;

import Core.Graph;
import Core.Helpers.Dimensions2D;
import Core.Helpers.Range;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

import static org.junit.Assert.*;

public class GraphUtilsTest {

    private static Graph graph1;
    private static Graph graph2;
    private static Path actualPath1;
    private static Path actualPath2;
    private static Path actualPath3;
    private static Path expectedPath;

    @BeforeClass
    public static void setUp() {
        actualPath1 = Path.of("JavaGridGraph/src/test/Core/GraphAlgorithms/testResults/test1.txt");
        actualPath2 = Path.of("JavaGridGraph/src/test/Core/GraphAlgorithms/testResults/test2.txt");
        actualPath3 = Path.of("JavaGridGraph/src/test/Core/GraphAlgorithms/testResults/test3.txt");
        expectedPath = Path.of("JavaGridGraph/src/test/Core/GraphAlgorithms/testResults/expectedTest1.txt");
    }

    @Test
    public void testGenerate() throws IOException {
        /* Generating two graphs with seed=0 and comparing them with expected graph */
        Dimensions2D size = new Dimensions2D(500, 500);
        Random random = new Random();
        random.setSeed(0);
        Range edgeWeight = new Range(0, 1);
        Range edgeCount = new Range(0, 4);
        graph1 = GraphUtils.generate(size, random, edgeWeight, edgeCount);
        random.setSeed(0);
        graph2 = GraphUtils.generate(size, random, edgeWeight, edgeCount);

        GraphUtils.write(graph1, new File(String.valueOf(actualPath1)));
        GraphUtils.write(graph2, new File(String.valueOf(actualPath2)));

        assertEquals(Files.readString(actualPath1), Files.readString(actualPath2));
        assertEquals(Files.readString(actualPath1), Files.readString(expectedPath));
    }

    @Test
    public void testRead() throws IOException {
        /* reading a graph generated in previous test, writing it to a new file
         and checking if it is equal to the expected (which is the same as in previous test) */
        graph1 = GraphUtils.read(new File(String.valueOf(actualPath1)));
        GraphUtils.write(graph1, new File(String.valueOf(actualPath3)));

        assertEquals(Files.readString(actualPath3), Files.readString(expectedPath));
    }
}