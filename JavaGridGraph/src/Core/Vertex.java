package Core;

import java.util.ArrayList;

public class Vertex {
    private final int number;
    private double d; // distance from source vertex
    private int p; // predecessor in the shortest path
    private final ArrayList<Neighbour> neighbours;

    public Vertex(int n) {
        this.number = n;
        this.d = Double.MAX_VALUE;
        this.p = -1;
        this.neighbours = new ArrayList<>();
    }

    public int getNumber() {
        return number;
    }

    public double getD() {
        return d;
    }

    public int getP() {
        return p;
    }

    public void setD(double d) {
        this.d = d;
    }

    public void setP(int p) {
        this.p = p;
    }

    public int getNumberOfNeighbours() {
        return neighbours.size();
    }

    public int getNeighbourNumber(int i) {
        if(i < 0 || i >= neighbours.size()) {
            throw new IllegalArgumentException("Invalid index of neighbour");
        }
        return neighbours.get(i).n;
    }

    public int getNeighbourIndex(int n) {
        for (int i = 0; i < neighbours.size(); i++)
        {
            if (neighbours.get(i).n == n)
            {
                return i;
            }
        }
        throw new IllegalArgumentException("Invalid number of neighbour");
    }

    public double getNeighbourWeight(int i) {
        if(i < 0 || i >= neighbours.size()) {
            throw new IllegalArgumentException("Invalid index of neighbour");
        }
        return neighbours.get(i).weight;
    }

    public boolean hasNeighbourNumber(int n) {
        for(Neighbour neighbour: neighbours) {
            if(neighbour.n == n)
                return true;
        }
        return false;
    }

    public void addNeighbour(int n, double w) {
        neighbours.add(new Neighbour(n, w));
    }

    static class Neighbour {
        int n;
        double weight;

        public Neighbour(int n, double w) {
            this.n = n;
            this.weight = w;
        }
    }
}