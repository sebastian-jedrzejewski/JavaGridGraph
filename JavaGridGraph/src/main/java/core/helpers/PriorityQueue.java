package core.helpers;

import core.Vertex;

import java.util.List;

public class PriorityQueue
{
    //region PROPERTIES

    private final Vertex[] q; // array of vertices; priority is the smallest weight to reach the vertex from the source
    private final int [] pn; // positions of vertices in q
    private int n; // number of elements in q

    //endregion



    //region CONSTRUCTORS

    public PriorityQueue(int n)
    {
        this.q = new Vertex[n];
        this.pn = new int[n];
        this.n = 0;
    }

    //endregion



    //region PUBLIC METHODS

    public int getPosition(int i)
    {
        if (i < 0 || i >= pn.length)
        {
            throw new IllegalArgumentException("Invalid index in positions array");
        }
        return pn[i];
    }

    public boolean isEmpty()
    {
        return n == 0;
    }

    public void push(Vertex v, List<Double> d)
    {
        // q.length is also max size of priority queue because it is the number of vertices in graph, which is constant
        if(n == q.length)
        {
            throw new IllegalArgumentException("Too many elements in priority queue");
        }

        q[n++] = v;
        pn[v.getNumber()] = n-1;
        heapUp(n-1, d);
    }

    public Vertex pop(List<Double> d)
    {
        Vertex v = q[0];
        pn[q[0].getNumber()] = -1;
        pn[q[n-1].getNumber()] = 0;
        q[0] = q[--n];
        heapDown(d);
        return v;
    }

    public void heapUp(int from, List<Double> d) {
        int i = from;
        int p = (from - 1) / 2;
        Vertex tmp;
        while (i > 0) {
            if(d.get(q[p].getNumber()) <= d.get(q[i].getNumber())) {
                break;
            }

            tmp = q[i];
            q[i] = q[p];
            q[p] = tmp;
            pn[q[i].getNumber()] = i;
            pn[q[p].getNumber()] = p;

            i = p;
            p = (i - 1) / 2;
        }
    }

    //endregion



    //region PRIVATE METHODS

    private void heapDown(List<Double> d) {
        int i = 0;
        int c = 1;
        Vertex tmp;

        while(c < n) {
            if(c + 1 < n && d.get(q[c + 1].getNumber()) < d.get(q[c].getNumber())) {
                c++;
            }

            if(d.get(q[i].getNumber()) <= d.get(q[c].getNumber())) {
                break;
            }

            tmp = q[i];
            q[i] = q[c];
            q[c] = tmp;
            pn[q[i].getNumber()] = i;
            pn[q[c].getNumber()] = c;

            i = c;
            c = 2*i + 1;
        }
    }

    //endregion
}
