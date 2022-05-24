package Core.GraphAlgorithms;

import Core.Graph;
import Core.Helpers.Dimensions2D;
import Core.Helpers.Range;
import Core.Vertex;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class GraphUtils
{
    //region PUBLIC METHODS

    public static Graph generate(Dimensions2D size, Random random, Range edgeWeight, Range edgeCount)
    {
        return generate(size, random, edgeWeight, edgeCount, edgeCount, false);
    }
    public static Graph generate(Dimensions2D size, Random random, Range edgeWeight, Range inputEdgeCount, Range outputEdgeCount)
    {
        return generate(size, random, edgeWeight, inputEdgeCount, outputEdgeCount, true);
    }
    private static Graph generate(Dimensions2D size, Random random, Range edgeWeight, Range inputEdgeCount, Range outputEdgeCount, boolean directed)
    {
        final int[] edgeCountDrawingWeight = new int[] { 1, 2, 3, 8, 16 };
        int width = (int)size.getWidth();
        int numberOfVertices = (int)size.getArea();

        ArrayList<Integer> inputEdgeCountDrawingList = new ArrayList<>();
        ArrayList<Integer> outputEdgeCountDrawingList = new ArrayList<>();
        for (int i = (int)inputEdgeCount.getLow(); i < (int)inputEdgeCount.getHigh() + 1; i++)
        {
            int weight = edgeCountDrawingWeight[i];
            while (weight > 0)
            {
                inputEdgeCountDrawingList.add(i);
                weight--;
            }
        }
        if (directed)
        {
            for (int i = (int)outputEdgeCount.getLow(); i < (int)outputEdgeCount.getHigh() + 1; i++)
            {
                int weight = edgeCountDrawingWeight[i];
                while (weight > 0)
                {
                    outputEdgeCountDrawingList.add(i);
                    weight--;
                }
            }
        }
        else
        {
            outputEdgeCountDrawingList = inputEdgeCountDrawingList;
        }

        int[] inputEdgeCountArray = new int[numberOfVertices];
        int[] outputEdgeCountArray = new int[numberOfVertices];
        for (int i = 0; i < numberOfVertices; i++)
        {
            inputEdgeCountArray[i] = inputEdgeCountDrawingList.get(random.nextInt((inputEdgeCountDrawingList.size())));
            if (directed)
            {
                outputEdgeCountArray[i] = outputEdgeCountDrawingList.get(random.nextInt((outputEdgeCountDrawingList.size())));
            }
            else
            {
                outputEdgeCountArray[i] = inputEdgeCountArray[i];
            }
        }

        Graph graph = new Graph(size);
        for (int i = 0; i < numberOfVertices; i++)
        {
            int row = i / width;
            ArrayList<Integer> availableVertices = new ArrayList<>();
            if (i - 1 >= 0 && (i - 1) / width == row && graph.getNumberOfInputNeighbours(i - 1) < inputEdgeCountArray[i - 1] && (directed || !graph.getVertex(i).hasNeighbourNumber(i - 1)))
            {
                availableVertices.add(i - 1);
            }
            if ((i + 1) / width == row && graph.getNumberOfInputNeighbours(i + 1) < inputEdgeCountArray[i + 1] && (directed || !graph.getVertex(i).hasNeighbourNumber(i + 1)))
            {
                availableVertices.add(i + 1);
            }
            if (i - width >= 0 && graph.getNumberOfInputNeighbours(i - width) < inputEdgeCountArray[i - width] && (directed || !graph.getVertex(i).hasNeighbourNumber(i - width)))
            {
                availableVertices.add(i - width);
            }
            if (i + width < numberOfVertices && graph.getNumberOfInputNeighbours(i + width) < inputEdgeCountArray[i + width] && (directed || !graph.getVertex(i).hasNeighbourNumber(i + width)))
            {
                availableVertices.add(i + width);
            }
            while (graph.getVertex(i).getNumberOfNeighbours() < outputEdgeCountArray[i] && availableVertices.size() > 0)
            {
                int vertex = availableVertices.get(random.nextInt(availableVertices.size()));
                availableVertices.remove((Integer)vertex);

                double weight = random.nextDouble(edgeWeight.getLow(), edgeWeight.getHigh());

                graph.getVertex(i).addNeighbour(vertex, weight);
                if (!directed)
                {
                    graph.getVertex(vertex).addNeighbour(i, weight);
                }
            }
        }
        return graph;
    }

    public static void write(Graph graph, File file) throws IOException
    {
        PrintWriter printWriter = new PrintWriter(new FileWriter(file));
        printWriter.printf("%d %d\n", (int)graph.getSize().getHeight(), (int)graph.getSize().getWidth());
        for (Vertex vertex : graph)
        {
            printWriter.print("\t");
            for (int i = 0; i < vertex.getNumberOfNeighbours(); i++)
            {
                printWriter.printf(" %d :%f ", vertex.getNeighbourNumber(i), vertex.getNeighbourWeight(i));
            }
            printWriter.print("\n");
        }
        printWriter.close();
    }

    public static Graph read(File file) throws IOException
    {
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
                        graph = new Graph(new Dimensions2D(Integer.parseInt(parts[0]), Integer.parseInt(parts[1])));
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
                {
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
                else
                {
                    throw new IOException("Invalid format of the file");
                }
            }
            lineNumber++;
        }
        return graph;
    }

    public static Graph convertToUnderlying(Graph graph)
    {
        int width = (int)graph.getSize().getWidth();
        int numberOfVertices = (int)graph.getSize().getArea();
        Graph underlyingGraph = new Graph(graph.getSize());
        for (int i = 0; i < numberOfVertices; i++)
        {
            if (i - 1 >= 0 && (graph.getVertex(i).hasNeighbourNumber(i - 1) || graph.getVertex(i - 1).hasNeighbourNumber(i)))
            {
                underlyingGraph.getVertex(i - 1).addNeighbour(i, 0);
                underlyingGraph.getVertex(i).addNeighbour(i - 1, 0);
            }
            if (i + 1 < numberOfVertices && (graph.getVertex(i).hasNeighbourNumber(i + 1) || graph.getVertex(i + 1).hasNeighbourNumber(i)))
            {
                underlyingGraph.getVertex(i + 1).addNeighbour(i, 0);
                underlyingGraph.getVertex(i).addNeighbour(i + 1, 0);
            }
            if (i - width >= 0 && (graph.getVertex(i).hasNeighbourNumber(i - width) || graph.getVertex(i - width).hasNeighbourNumber(i)))
            {
                underlyingGraph.getVertex(i - width).addNeighbour(i, 0);
                underlyingGraph.getVertex(i).addNeighbour(i - width, 0);
            }
            if (i + width < numberOfVertices && (graph.getVertex(i).hasNeighbourNumber(i + width) || graph.getVertex(i + width).hasNeighbourNumber(i)))
            {
                underlyingGraph.getVertex(i + width).addNeighbour(i, 0);
                underlyingGraph.getVertex(i).addNeighbour(i + width, 0);
            }
        }
        return underlyingGraph;
    }

    //endregion
}
