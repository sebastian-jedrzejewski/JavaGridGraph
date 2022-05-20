package Core;

import Core.Helpers.Range;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.random.RandomGenerator;

public class GraphUtils
{
    //region PUBLIC METHODS

    public static Graph Generate(int width, int height, Random random, Range<Double> edgeWeight, Range<Integer> edgeCount)
    {
        return Generate(width, height, random, edgeWeight, edgeCount, edgeCount, false);
    }
    public static Graph Generate(int width, int height, Random random, Range<Double> edgeWeight, Range<Integer> inputEdgeCount, Range<Integer> outputEdgeCount)
    {
        return Generate(width, height, random, edgeWeight, inputEdgeCount, outputEdgeCount, true);
    }
    private static Graph Generate(int width, int height, Random random, Range<Double> edgeWeight, Range<Integer> inputEdgeCount, Range<Integer> outputEdgeCount, boolean directed)
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
            if (i % width != 0 && graph.getVertex(i - 1).getNumberOfInputs() < inputEdgeCountArray[i - 1] && (directed || !graph.getVertex(i).hasNeighbourNumber(i - 1)))
            {
                availableVertices.add(i - 1);
            }
            if ((i + 1) % width != 0 && graph.getVertex(i + 1).getNumberOfInputs() < inputEdgeCountArray[i + 1] && (directed || !graph.getVertex(i).hasNeighbourNumber(i + 1)))
            {
                availableVertices.add(i + 1);
            }
            if (i - width >= 0 && graph.getVertex(i - width).getNumberOfInputs() < inputEdgeCountArray[i - width] && (directed || !graph.getVertex(i).hasNeighbourNumber(i - width)))
            {
                availableVertices.add(i - width);
            }
            if (i + width < numberOfVertices && graph.getVertex(i + width).getNumberOfInputs() < inputEdgeCountArray[i + width] && (directed || !graph.getVertex(i).hasNeighbourNumber(i + width)))
            {
                availableVertices.add(i + width);
            }

            while (graph.getVertex(i).getNumberOfOutputs() < outputEdgeCountArray[i] && availableVertices.size() > 0)
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

    public static void Write(Graph graph, File file) throws IOException
    {
        FileWriter fileWriter = new FileWriter(file);
        PrintWriter printWriter = new PrintWriter(fileWriter);
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

    public static void Read(File file)
    {

    }

    //endregion



    //region PRIVATE METHODS



    //endregion
}
