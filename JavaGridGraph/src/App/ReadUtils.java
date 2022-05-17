package App;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ReadUtils {

    public static Graph readGraph(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        int lineNumber = 0;
        String line;
        String [] parts; // split the line into this array
        Graph graph = null;

        while ((line = br.readLine()) != null) {
            parts = line.split("\\s+");
            if(lineNumber == 0) {  // there should be dimensions
                if(parts.length == 2) {
                    try {
                        graph = new Graph(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
                    } catch (NumberFormatException e) {
                        throw new IOException("Dimensions of the graph must be numbers");
                    }
                }
                else {
                    throw new IOException("Invalid format of the file (2 dimensions must be given)");
                }
            }
            else {
                if(parts.length % 2 != 0 || parts.length == 0)
                { // there should be odd number of elements (pair of vertices and weights + nothing at index 0)
                    addNeighbours(graph, parts, lineNumber);
                }
                else
                {
                    throw new IOException("Invalid format of the file");
                }
            }
            lineNumber++;
        }
        return graph;
    }

    private static void addNeighbours(Graph graph, String [] parts, int lineNumber) throws IOException {
        Vertex v = graph.getVertex(lineNumber-1);
        for(int i = 1; i < parts.length-1; i+=2) {
            try {
                int vertex = Integer.parseInt(parts[i]);
                if (parts[i + 1].contains(":")) { // delete ":" in weights
                    parts[i + 1] = parts[i + 1].replace(":", "");
                }
                double weight = Double.parseDouble(parts[i + 1]);
                v.addNeighbour(vertex, weight);
            } catch(NumberFormatException e) {
                throw new IOException("Invalid format of the file");
            }
        }
    }

    public static void displayGraph(Graph graph) { // method for tests
        for(Vertex v: graph) {
            System.out.println("Vertex number " + v.getNumber() + ":");
            for(int i = 0; i < v.getNumberOfNeighbours(); i++) {
                System.out.print("Neighbour number " + v.getNeighbourNumber(i));
                System.out.print(" weight: " + v.getNeighbourWeight(i) + "; ");
            }
            System.out.println();
            System.out.println();
        }
    }

}
