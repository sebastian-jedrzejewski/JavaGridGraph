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
}
