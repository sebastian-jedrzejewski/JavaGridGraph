package Core.GraphAlgorithms;

import Core.Graph;
import Core.Vertex;

import java.util.*;

public class BFS
{
    //region PROPERTIES

    private final Graph graph;
    private final Result[] results;

    //endregion



    //region CONSTRUCTORS

    public BFS(Graph graph)
    {
        this.graph = graph;
        this.results = new Result[(int)graph.getSize().getArea()];
        Arrays.fill(this.results, null);
    }

    //endregion



    //region PUBLIC METHODS

    public boolean isGraphConnected()
    {
        Graph underlyingGraph = GraphUtils.convertToUnderlying(graph);
        for (boolean connectedVertex : run(0, underlyingGraph).visited)
        {
            if (!connectedVertex)
            {
                return false;
            }
        }
        return true;
    }

    //endregion



    //region PRIVATE METHODS

    private Result run(int vertex)
    {
        return run(vertex, graph);
    }
    private Result run(int vertex, Graph graph)
    {
        int numberOfVertices = (int)graph.getSize().getArea();

        boolean[] visited = new boolean[numberOfVertices];
        int[] predecessors = new int[numberOfVertices];
        int[] distance = new int[numberOfVertices];
        Arrays.fill(visited, false);
        Arrays.fill(predecessors, 0);
        Arrays.fill(distance, Integer.MAX_VALUE);

        visited[vertex] = true;
        distance[vertex] = 0;

        ArrayDeque<Integer> queue = new ArrayDeque<>();
        queue.add(vertex);

        int currentVertex;
        while (!(queue.isEmpty()))
        {
            currentVertex = queue.pop();
            Vertex currentVertexObject = graph.getVertex(currentVertex);
            for (int i = 0; i < currentVertexObject.getNumberOfNeighbours(); i++)
            {
                int neighbour = currentVertexObject.getNeighbourNumber(i);
                if (!visited[neighbour])
                {
                    visited[neighbour] = true;
                    distance[neighbour] = distance[currentVertex] + 1;
                    predecessors[neighbour] = currentVertex;
                    queue.add(neighbour);
                }
            }
            visited[currentVertex] = true;
        }

        return new Result(vertex, visited, predecessors, distance);
    }

    //endregion



    //region NESTED CLASSES

    private class Result
    {
        // PROPERTIES
        private final int vertex;
        private final boolean[] visited;
        private final int[] predecessors;
        private final int[] distance;


        // CONSTRUCTORS
        private Result(int vertex, boolean[] visited, int[] predecessors, int[] distance) {
            this.vertex = vertex;
            this.visited = visited;
            this.predecessors = predecessors;
            this.distance = distance;
        }
    }

    //endregion
}
