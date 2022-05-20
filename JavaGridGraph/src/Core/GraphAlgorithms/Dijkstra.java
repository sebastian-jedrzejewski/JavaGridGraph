package Core.GraphAlgorithms;

import Core.Graph;
import Core.PriorityQueue;
import Core.Vertex;

public class Dijkstra {
    private final Graph graph;

    public Dijkstra(Graph graph) {
        this.graph = graph;
    }

    public void dijkstraAlgorithm(int vA, int vB) {
        graph.refresh();

        Vertex vertexA = graph.getVertex(vA);
        Vertex vertexB = graph.getVertex(vB);
        int n = graph.getColumns() * graph.getRows();

        PriorityQueue pq = new PriorityQueue(n);
        for(int i = 0; i < n; i++) {
            pq.push(graph.getVertex(i));
        }

        vertexA.setD(0);

        // Keeping heap properties
        pq.heapUp(pq.getPosition(vA));

        Vertex u, v;
        while(!pq.isEmpty()) {
            u = pq.pop();

            // loop for each neighbour v of vertex u
            for(int i=0; i < u.getNumberOfNeighbours(); i++) {
                v = graph.getVertex(u.getNeighbourNumber(i));
                if(v.getD() > u.getD() + u.getNeighbourWeight(i)) {
                    v.setD(u.getD() + u.getNeighbourWeight(i));
                    v.setP(u.getNumber());
                    pq.heapUp(pq.getPosition(v.getNumber()));
                }

                if(u.getNumber() == vertexB.getNumber()) {
                    return;
                }
            }
        }

    }
}
