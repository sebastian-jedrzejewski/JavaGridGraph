package Core.GraphAlgorithms;

import Core.Graph;
import Core.Vertex;

import java.util.*;

public class BFS
{
    //region PROPERTIES

    private final Graph _graph;
    private final Result[] _results;

    //endregion



    //region CONSTRUCTORS

    public BFS(Graph graph)
    {
        _graph = graph;
        _results = new Result[_graph.getColumns() * _graph.getRows()];
        Arrays.fill(_results, null);
    }

    //endregion



    //region PUBLIC METHODS

    public boolean isGraphConnected()
    {
        List<Result> nonNullResults = Arrays.stream(_results).filter(Objects::nonNull).toList();
        Result result;
        if (nonNullResults.stream().count() > 0)
        {
            result = nonNullResults.get(0);
        }
        else
        {
            result = _results[0] = run(0);
        }

        for (boolean connectedVertex : result.Visited)
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
        int numberOfVertices = _graph.getColumns() * _graph.getRows();

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
            Vertex currentVertexObject = _graph.getVertex(currentVertex);
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
        private final int Vertex;
        private final boolean[] Visited;
        private final int[] Predecessors;
        private final int[] Distance;


        // CONSTRUCTORS
        private Result(int vertex, boolean[] visited, int[] predecessors, int[] distance) {
            Vertex = vertex;
            Visited = visited;
            Predecessors = predecessors;
            Distance = distance;
        }
    }

    //endregion
}
