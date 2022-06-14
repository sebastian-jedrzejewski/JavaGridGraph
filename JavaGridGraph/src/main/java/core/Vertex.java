package core;

import java.util.ArrayList;

public class Vertex
{
    //region PROPERTIES

    private final int number;
    private final ArrayList<Neighbour> neighbours;

    //endregion



    //region CONSTRUCTORS

    public Vertex(int n)
    {
        this.number = n;
        this.neighbours = new ArrayList<>();
    }

    //endregion



    //region PUBLIC METHODS

    public int getNumber() {
        return number;
    }

    public int getNumberOfNeighbours() {
        return neighbours.size();
    }

    public int getNeighbourNumber(int i)
    {
        if(i < 0 || i >= neighbours.size())
        {
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

    //endregion



    //region NESTED CLASSES

    static class Neighbour {
        int n;
        double weight;

        public Neighbour(int n, double w) {
            this.n = n;
            this.weight = w;
        }
    }

    //endregion
}
