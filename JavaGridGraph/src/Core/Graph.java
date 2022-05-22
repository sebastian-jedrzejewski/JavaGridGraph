package Core;

import java.util.Iterator;

public class Graph implements Iterable<Vertex> {
    private final int rows;
    private final int columns;
    private final Vertex[] vertices;

    public Graph(int r, int c) {
        this.rows = r;
        this.columns = c;
        this.vertices = new Vertex[r*c];

        for(int i=0; i < r*c; i++) {
            this.vertices[i] = new Vertex(i);
        }
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }

    public int getNumberOfInputNeighbours(int number) {
        int count = 0;
        if(number % columns != 0 && number - 1 >= 0 && vertices[number-1].hasNeighbourNumber(number))
            count++;
        if((number+1) % columns != 0 && number + 1 < vertices.length && vertices[number+1].hasNeighbourNumber(number))
            count++;
        if(number - columns >= 0 && vertices[number-columns].hasNeighbourNumber(number))
            count++;
        if(number + columns < vertices.length && vertices[number+columns].hasNeighbourNumber(number))
            count++;

        return count;
    }

    public Vertex getVertex(int i) {
        if(i < 0 || i >= rows*columns) {
            throw new IllegalArgumentException("Invalid index of vertex");
        }
        return vertices[i];
    }

    public void refresh() {
        for(int i=0; i < rows*columns; i++) {
            vertices[i].setP(-1);
            vertices[i].setD(Double.MAX_VALUE);
        }
    }

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
            lgt = rows * columns;
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
}
