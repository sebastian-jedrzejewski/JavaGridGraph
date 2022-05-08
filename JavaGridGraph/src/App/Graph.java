package App;

import java.util.Iterator;

public class Graph implements Iterable<Vertex> {
    private int rows;
    private int columns;
    private Vertex [] vertices;

    public Graph() {

    }

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

    public Vertex getVertex(int i) {
        if(i < 0 || i >= rows*columns) {
            throw new IllegalArgumentException("Invalid index of vertex");
        }
        return vertices[i];
    }

    @Override
    public Iterator<Vertex> iterator() {
        return new VertexIterator();
    }

    class VertexIterator implements Iterator<Vertex> {

        private final Vertex [] v;
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
