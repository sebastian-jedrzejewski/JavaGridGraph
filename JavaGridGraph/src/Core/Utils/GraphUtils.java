package Core.Utils;

import Core.Graph;
import Core.Helpers.Range;
import Core.Vertex;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class GraphUtils
{
    //region PUBLIC METHODS

    public static Graph generate(int width, int height, Random random, Range<Double> edgeWeight, Range<Integer> edgeCount)
    {
        return generate(width, height, random, edgeWeight, edgeCount, edgeCount, false);
    }
    public static Graph generate(int width, int height, Random random, Range<Double> edgeWeight, Range<Integer> inputEdgeCount, Range<Integer> outputEdgeCount)
    {
        return generate(width, height, random, edgeWeight, inputEdgeCount, outputEdgeCount, true);
    }
    private static Graph generate(int width, int height, Random random, Range<Double> edgeWeight, Range<Integer> inputEdgeCount, Range<Integer> outputEdgeCount, boolean directed)
    {
        final int[] edgeCountDrawingWeight = new int[] { 1, 2, 3, 8, 16 };
        int numberOfVertices = width * height;

        ArrayList<Integer> inputEdgeCountDrawingList = new ArrayList<>();
        ArrayList<Integer> outputEdgeCountDrawingList = new ArrayList<>();
        for (int i = inputEdgeCount.getLow(); i < inputEdgeCount.getHigh() + 1; i++)
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
            for (int i = outputEdgeCount.getLow(); i < outputEdgeCount.getHigh() + 1; i++)
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

        Graph graph = new Graph(width, height);
        for (int i = 0; i < numberOfVertices; i++)
        {
            ArrayList<Integer> availableVertices = new ArrayList<>();
            if (i % width != 0 && graph.getNumberOfInputNeighbours(i - 1) < inputEdgeCountArray[i - 1] && (directed || !graph.getVertex(i).hasNeighbourNumber(i - 1)))
            {
                availableVertices.add(i - 1);
            }
            if ((i + 1) % width != 0 && graph.getNumberOfInputNeighbours(i + 1) < inputEdgeCountArray[i + 1] && (directed || !graph.getVertex(i).hasNeighbourNumber(i + 1)))
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
        printWriter.printf("%d %d\n", graph.getRows(), graph.getColumns());
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

    //endregion
}
