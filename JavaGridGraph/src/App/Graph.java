package App;

public class Graph {
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
        if(i >= rows*columns) {
            throw new IllegalArgumentException();
        }
        return vertices[i];
    }
}
