package core.graphalgorithms;

import core.Graph;
import core.helpers.PriorityQueue;
import core.Vertex;

import java.util.ArrayList;
import java.util.List;

public class Dijkstra
{
    //region PROPERTIES
    private final Graph graph;
    private final List<Double> d;
    private final List<Integer> p;

    //endregion



    //region CONSTRUCTORS

    public Dijkstra(Graph graph)
    {
        this.graph = graph;
        this.p = new ArrayList<>((int) this.graph.getSize().getArea());
        this.d = new ArrayList<>((int) this.graph.getSize().getArea());
    }

    //endregion



    //region PUBLIC METHODS

    public List<Integer> dijkstraAlgorithm(int vA, int vB)
    {
        arraysRefresh();

        Vertex vertexA = graph.getVertex(vA);
        Vertex vertexB = graph.getVertex(vB);
        int n = (int)graph.getSize().getArea();

        PriorityQueue pq = new PriorityQueue(n);
        for(int i = 0; i < n; i++)
        {
            pq.push(graph.getVertex(i), d);
        }

        d.set(vertexA.getNumber(), 0.0);

        // Keeping heap properties
        pq.heapUp(pq.getPosition(vA), d);

        Vertex u, v;
        while(!pq.isEmpty())
        {
            u = pq.pop(d);

            // loop for each neighbour v of vertex u
            for(int i=0; i < u.getNumberOfNeighbours(); i++)
            {
                v = graph.getVertex(u.getNeighbourNumber(i));
                if(d.get(v.getNumber()) > d.get(u.getNumber()) + u.getNeighbourWeight(i))
                {
                    d.set(v.getNumber(), d.get(u.getNumber()) + u.getNeighbourWeight(i));
                    p.set(v.getNumber(), u.getNumber());
                    pq.heapUp(pq.getPosition(v.getNumber()), d);
                }

                if(u.getNumber() == vertexB.getNumber())
                {
                     return p;
                }
            }
        }
        return p;
    }


    //endregion


    //region PRIVATE METHODS


    private void arraysRefresh() {
        if(d.size() != 0) {
            d.removeAll(d);
        }
        if(p.size() != 0) {
            p.removeAll(p);
        }
        for(int i=0; i < (int) graph.getSize().getArea(); i++) {
            d.add(Double.MAX_VALUE);
            p.add(-1);
        }
    }


    //endregion
}
