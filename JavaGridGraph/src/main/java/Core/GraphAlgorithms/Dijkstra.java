package Core.GraphAlgorithms;

import Core.Graph;
import Core.Helpers.PriorityQueue;
import Core.Vertex;

public class Dijkstra
{
    //region PROPERTIES
    private final Graph graph;

    //endregion



    //region CONSTRUCTORS

    public Dijkstra(Graph graph)
    {
        this.graph = graph;
    }

    //endregion



    //region PUBLIC METHODS

    public void dijkstraAlgorithm(int vA, int vB)
    {
        graph.refresh();

        Vertex vertexA = graph.getVertex(vA);
        Vertex vertexB = graph.getVertex(vB);
        int n = (int)graph.getSize().getArea();

        PriorityQueue pq = new PriorityQueue(n);
        for(int i = 0; i < n; i++)
        {
            pq.push(graph.getVertex(i));
        }

        vertexA.setD(0);

        // Keeping heap properties
        pq.heapUp(pq.getPosition(vA));

        Vertex u, v;
        while(!pq.isEmpty())
        {
            u = pq.pop();

            // loop for each neighbour v of vertex u
            for(int i=0; i < u.getNumberOfNeighbours(); i++)
            {
                v = graph.getVertex(u.getNeighbourNumber(i));
                if(v.getD() > u.getD() + u.getNeighbourWeight(i))
                {
                    v.setD(u.getD() + u.getNeighbourWeight(i));
                    v.setP(u.getNumber());
                    pq.heapUp(pq.getPosition(v.getNumber()));
                }

                if(u.getNumber() == vertexB.getNumber())
                {
                    return;
                }
            }
        }
    }

    //endregion
}
