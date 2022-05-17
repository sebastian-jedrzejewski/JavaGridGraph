package GUI;

import Algorithms.Dijkstra;
import App.Graph;
import App.Vertex;
import javafx.beans.InvalidationListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GraphDrawer extends AnchorPane
{
    //region PARAMETERS

    private Graph _graph = null;
    private double _minGraphsWeight;
    private double _maxGraphsWeight;


    private Insets _margin = new Insets(0);
    private double _verticesSpacingPercent = 0.8;
    private double _edgesSpacingPercent = 0.1;
    private double _edgesNormalWidthPercent = 0.05;
    private double _edgesSelectedWidthPercent = 0.1;
    private double _edgesNormalRadiusPercent = 0.1;
    private double _edgesSelectedRadiusPercent = 0.15;


    private Dijkstra dijkstra = null;
    private int fromVertexNumber = -1;
    private ArrayList<Integer> toVerticesNumbers = new ArrayList<>();

    //endregion



    //region CONSTRUCTORS

    public GraphDrawer()
    {
        this.setStyle("-fx-background-color: #000000");
        this.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (_graph != null)
            {
                update();
            }
        });
        this.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (_graph != null)
            {
                update();
            }
        });
    }

    public GraphDrawer(Insets margin)
    {
        this();
        _margin = margin;
    }

    //endregion



    //region PUBLIC METHODS

    public Insets getMargin() { return _margin; }
    public void setMargin(Insets margin)
    {
        _margin = margin;
        update();
    }

    public double getVerticesSpacingPercent() { return _verticesSpacingPercent; }
    public void setVerticesSpacingPercent(double verticesSpacingPercent)
    {
        _verticesSpacingPercent = verticesSpacingPercent;
        update();
    }

    public Graph getGraph() { return _graph; }
    public void setGraph(Graph graph)
    {
        _minGraphsWeight = Double.MAX_VALUE;
        _maxGraphsWeight = 0;
        _graph = graph;
        dijkstra = new Dijkstra(_graph);

        for (Vertex vertex: _graph)
        {
            for (int i = 0; i < vertex.getNumberOfNeighbours(); i++)
            {
                double weight = vertex.getNeighbourWeight(i);
                if (weight > _maxGraphsWeight)
                {
                    _maxGraphsWeight = weight;
                }
                if (weight < _minGraphsWeight)
                {
                    _minGraphsWeight = weight;
                }
            }
        }

        reset();
    }

    public void reset()
    {
        fromVertexNumber = -1;
        toVerticesNumbers = new ArrayList<>();
        update();
    }

    //endregion



    //region PRIVATE METHODS

    private void update()
    {
        this.getChildren().clear();


        int rows = _graph.getRows();
        int columns = _graph.getColumns();
        int vertices_count = rows * columns;


        double width = ((Pane)(this.getParent())).getWidth() - (_margin.getLeft() + _margin.getRight());
        double height = ((Pane)(this.getParent())).getHeight() - (_margin.getTop() + _margin.getBottom());

        double verticalDiameter = height / (((1 + _verticesSpacingPercent) * rows) - _verticesSpacingPercent);
        double horizontalDiameter = width / (((1 + _verticesSpacingPercent) * columns) - _verticesSpacingPercent);
        double diameter = Math.min(verticalDiameter, horizontalDiameter);

        Circle[] vertices = new Circle[vertices_count];
        EdgesCollection[] edges = new EdgesCollection[vertices_count];

        double x = diameter / 2 + _margin.getLeft();
        double y = diameter / 2 + _margin.getTop();
        for (int i = 0; i < vertices_count; i++)
        {
            int vertex_number = i;

            // Vertex
            vertices[vertex_number] = new Circle();
            vertices[vertex_number].setCenterX(x);
            vertices[vertex_number].setCenterY(y);
            vertices[vertex_number].setRadius(diameter / 2);
            vertices[vertex_number].setFill(getVertexColor(vertex_number, false));
            vertices[vertex_number].hoverProperty().addListener((obs, oldVal, newValue) ->
            {
                vertices[vertex_number].setFill(getVertexColor(vertex_number, newValue));
            });
            vertices[vertex_number].setOnMouseClicked(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton() == MouseButton.PRIMARY)
                    {
                        if (toVerticesNumbers.contains(vertex_number))
                        {
                            toVerticesNumbers.remove((Integer)vertex_number);
                        }
                        if (fromVertexNumber == vertex_number)
                        {
                            fromVertexNumber = -1;
                        }
                        else
                        {
                            fromVertexNumber = vertex_number;
                        }
                        update();
                    }
                    else if (mouseEvent.getButton() == MouseButton.SECONDARY)
                    {
                        if (toVerticesNumbers.contains(vertex_number))
                        {
                            toVerticesNumbers.remove((Integer)vertex_number);
                        }
                        else
                        {
                            toVerticesNumbers.add(vertex_number);
                        }
                        update();
                    }
                }
            });

            // Edges
            Vertex vertex = _graph.getVertex(i);
            edges[vertex_number] = new EdgesCollection();

            if (vertex.hasNeighbourNumber(i - 1)) // Left
            {
                double startX = x - (diameter / 2);
                double endX = startX - (diameter * _verticesSpacingPercent);
                double Y = y + (_edgesSpacingPercent * diameter);
                edges[vertex_number].setLeft(i - 1, drawEdge(startX, Y, endX, Y, vertex.getNeighbourWeight(vertex.getNeighbourIndex(i - 1)), diameter));
            }
            if (vertex.hasNeighbourNumber(i + 1)) // Right
            {
                double startX = x + (diameter / 2);
                double endX = startX + (diameter * _verticesSpacingPercent);
                double Y = y - (_edgesSpacingPercent * diameter);
                edges[vertex_number].setRight(i + 1, drawEdge(startX, Y, endX, Y, vertex.getNeighbourWeight(vertex.getNeighbourIndex(i + 1)), diameter));
            }
            if (vertex.hasNeighbourNumber(i - columns)) // Top
            {
                double startY = y - (diameter / 2);
                double endY = startY - (diameter * _verticesSpacingPercent);
                double X = x - (_edgesSpacingPercent * diameter);
                edges[vertex_number].setTop(i - columns, drawEdge(X, startY, X, endY, vertex.getNeighbourWeight(vertex.getNeighbourIndex(i - columns)), diameter));
            }
            if (vertex.hasNeighbourNumber(i + columns)) // Bottom
            {
                double startY = y + (diameter / 2);
                double endY = startY + (diameter * _verticesSpacingPercent);
                double X = x + (_edgesSpacingPercent * diameter);
                edges[vertex_number].setBottom(i + columns, drawEdge(X, startY, X, endY, vertex.getNeighbourWeight(vertex.getNeighbourIndex(i + columns)), diameter));
            }

            if (((i + 1) % columns) == 0)
            {
                x = diameter / 2 + _margin.getLeft();
                y += (1 + _verticesSpacingPercent) * diameter;
            }
            else
            {
                x += (1 + _verticesSpacingPercent) * diameter;
            }
        }

        if (fromVertexNumber != -1)
        {
            vertices[fromVertexNumber].setFill(getVertexColor(fromVertexNumber, false));
        }

        for (Integer vertex: toVerticesNumbers)
        {
            vertices[vertex].setFill(getVertexColor(vertex, false));
        }

        if (fromVertexNumber != -1)
        {
            System.out.println(fromVertexNumber);
            for (Integer vertex: toVerticesNumbers)
            {
                dijkstra.dijkstraAlgorithm(fromVertexNumber, vertex);

                Vertex v = _graph.getVertex(vertex);
                while (v.getP() != -1)
                {
                    System.out.println(v.getP());
                    Arrow edge = edges[v.getP()].getByNumber(v.getNumber());
                    edge.setFill(Color.WHITE);
                    edge.setWidth(diameter * _edgesSelectedWidthPercent);
                    edge.setRadius(diameter * _edgesSelectedRadiusPercent);
                    v = _graph.getVertex(v.getP());
                }
            }
        }

        this.getChildren().addAll(vertices);
        for (int i = 0; i < vertices_count; i++)
        {
            this.getChildren().addAll(edges[i].getAllEdges());
        }
    }

    private Color getVertexColor(int i, boolean hover)
    {
        Color color;
        if (hover)
        {
            if (i == fromVertexNumber)
            {
                color = Color.DARKRED;
            }
            else if (toVerticesNumbers.contains(i))
            {
                color = Color.DARKGREEN;
            }
            else
            {
                color = Color.LIGHTGREY;
            }
        }
        else
        {
            if (i == fromVertexNumber)
            {
                color = Color.RED;
            }
            else if (toVerticesNumbers.contains(i))
            {
                color = Color.GREEN;
            }
            else
            {
                color = Color.WHITE;
            }
        }
        return color;
    }

    private Arrow drawEdge(double startX, double startY, double endX, double endY, double weight, double baseWidth)
    {
        Arrow edge = new Arrow();
        edge.setStartX(startX);
        edge.setStartY(startY);
        edge.setEndX(endX);
        edge.setEndY(endY);
        edge.setWidth(baseWidth * _edgesNormalWidthPercent);
        edge.setRadius(baseWidth * _edgesNormalRadiusPercent);
        edge.setFill(Color.web("hsl(" + (255 - (((weight - _minGraphsWeight) / (_maxGraphsWeight - _minGraphsWeight)) * 255)) + ", 100%, 100%)"));
        return edge;
    }

    //endregion
}
