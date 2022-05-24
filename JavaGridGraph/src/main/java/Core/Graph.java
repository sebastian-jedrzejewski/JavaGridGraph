package Core;

import Core.Helpers.Dimensions2D;
import Core.Helpers.Range;

import java.util.Iterator;

public class Graph implements Iterable<Vertex>
{
    //region PROPERTIES

    private final Dimensions2D size;
    private final Vertex[] vertices;

    //endregion



    //region CONSTRUCTORS

    public Graph(Dimensions2D size)
    {
        this.size = size;
        this.vertices = new Vertex[(int)this.size.getArea()];
        for(int i = 0; i < (int)this.size.getArea(); i++)
        {
            this.vertices[i] = new Vertex(i);
        }
    }
    public Graph(int height, int width)
    {
        this(new Dimensions2D(width, height));
    }

    //endregion



    //region PUBLIC METHODS

    public Dimensions2D getSize()
    {
        return size;
    }

    public final Range getWeightRange()
    {
        double min = Double.MAX_VALUE;
        double max = Double.MIN_VALUE;
        for (int i = 0; i < (int)size.getArea(); i++)
        {
            Vertex vertex = vertices[i];
            for (int j = 0; j < vertex.getNumberOfNeighbours(); j++)
            {
                if (vertex.getNeighbourWeight(j) < min)
                {
                    min = vertex.getNeighbourWeight(j);
                }
                if (vertex.getNeighbourWeight(j) > max)
                {
                    max = vertex.getNeighbourWeight(j);
                }
            }
        }
        return new Range(min, max);
    }

    public int getNumberOfInputNeighbours(int number)
    {
        int count = 0;
        if(number - 1 >= 0 && number % size.getWidth() != 0 && vertices[number-1].hasNeighbourNumber(number))
            count++;
        if(number + 1 < vertices.length && (number+1) % size.getWidth() != 0 && vertices[number+1].hasNeighbourNumber(number))
            count++;
        if(number - size.getWidth() >= 0 && vertices[number-(int)size.getWidth()].hasNeighbourNumber(number))
            count++;
        if(number + size.getWidth() < vertices.length && vertices[number+(int)size.getWidth()].hasNeighbourNumber(number))
            count++;

        return count;
    }

    public Vertex getVertex(int i)
    {
        if (i < 0 || i >= (int)size.getArea())
        {
            throw new IllegalArgumentException("Invalid index of vertex");
        }
        return vertices[i];
    }

    public void refresh() {
        for(int i=0; i < size.getArea(); i++) {
            vertices[i].setP(-1);
            vertices[i].setD(Double.MAX_VALUE);
        }
    }

    //endregion



    //region ITERATOR

    @Override
    public Iterator<Vertex> iterator() {
        return new VertexIterator();
    }

    class VertexIterator implements Iterator<Vertex> {

        private final Vertex[] v;
        private final int lgt;
        private int current;

        public VertexIterator() {
            v = vertices;
            lgt = (int)size.getArea();
            current = 0;
        }

        @Override
        public boolean hasNext() {
            return current < lgt;
        }

        @Override
        public Vertex next() {
            return v[current++];
        }
    }

    //endregion
}
