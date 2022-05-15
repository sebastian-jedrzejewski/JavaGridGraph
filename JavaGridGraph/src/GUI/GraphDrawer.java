package GUI;

import App.Graph;
import App.Vertex;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class GraphDrawer extends AnchorPane {

    private Graph _graph = null;

    private double _verticesSpacingPercent = 0.8;
    private double _edgesSpacing = 4;
    private Insets _margin = new Insets(0);

    private boolean fromSelected;
    private int circleNumberFrom;

    public GraphDrawer()
    {
        this.setStyle("-fx-background-color: #000000");
        this.widthProperty().addListener((obs, oldVal, newVal) -> {
            onResize();
        });
        this.heightProperty().addListener((obs, oldVal, newVal) -> {
            onResize();
        });
    }
    public GraphDrawer(Insets margin)
    {
        _margin = margin;
        this.setStyle("-fx-background-color: #000000");
        this.widthProperty().addListener((obs, oldVal, newVal) -> {
            onResize();
        });
        this.heightProperty().addListener((obs, oldVal, newVal) -> {
            onResize();
        });
    }

    public void setMargin(Insets margin)
    {
        _margin = margin;
        drawGraph();
    }

    public Insets getMargin()
    {
        return _margin;
    }

    public void setGraph(Graph graph)
    {
        _graph = graph;
        drawGraph();
    }

    private void onResize()
    {
        if (_graph != null)
        {
            drawGraph();
        }
    }

    private void drawGraph()
    {
        this.getChildren().clear();

        double width = ((Pane)(this.getParent())).getWidth() - (_margin.getLeft() + _margin.getRight());
        double height = ((Pane)(this.getParent())).getHeight() - (_margin.getTop() + _margin.getBottom());
        System.out.println(width);
        System.out.println(height);
        System.out.println();

        int rows = _graph.getRows();
        int columns = _graph.getColumns();

        double verticalDiameter = height / (((1 + _verticesSpacingPercent) * rows) - _verticesSpacingPercent);
        double horizontalDiameter = width / (((1 + _verticesSpacingPercent) * columns) - _verticesSpacingPercent);
        double diameter = Math.min(verticalDiameter, horizontalDiameter);

        int vertices_count = rows * columns;
        Circle[] vertices = new Circle[vertices_count];

        double x = diameter / 2 + _margin.getLeft();
        double y = diameter / 2 + _margin.getTop();
        for (int i = 0; i < vertices_count; i++)
        {
            vertices[i] = new Circle();
            vertices[i].setCenterX(x);
            vertices[i].setCenterY(y);
            vertices[i].setRadius(diameter / 2);
            vertices[i].setFill(Color.LIGHTGREY);

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

        this.getChildren().addAll(vertices);


        x = diameter / 2 + _margin.getLeft();
        y = diameter / 2 + _margin.getTop();
        for (int i = 0; i < vertices_count; i++)
        {
            Vertex vertex = _graph.getVertex(i);

            // Left edge
            if (i % columns != 0)
            {
                double startX = x - (diameter / 2);
                double endX = startX - (diameter * _verticesSpacingPercent);
                double Y = y + (_edgesSpacing / 2);
                drawEdge(startX, Y, endX, Y, vertex.getNeighbourWeight(vertex.getNeighbourIndex(i - 1)));
            }

            // Right edge
            if ((i + 1) % columns != 0)
            {
                double startX = x + (diameter / 2);
                double endX = startX + (diameter * _verticesSpacingPercent);
                double Y = y - (_edgesSpacing / 2);
                drawEdge(startX, Y, endX, Y, vertex.getNeighbourWeight(vertex.getNeighbourIndex(i + 1)));
            }

            // Top edge
            if (i - columns > 0)
            {
                double startY = y - (diameter / 2);
                double endY = startY - (diameter * _verticesSpacingPercent);
                double X = x - (_edgesSpacing / 2);
                drawEdge(X, startY, X, endY, vertex.getNeighbourWeight(vertex.getNeighbourIndex(i - columns)));
            }

            // Bottom edge
            if (i + columns < vertices_count)
            {
                double startY = y + (diameter / 2);
                double endY = startY + (diameter * _verticesSpacingPercent);
                double X = x + (_edgesSpacing / 2);
                drawEdge(X, startY, X, endY, vertex.getNeighbourWeight(vertex.getNeighbourIndex(i + columns)));
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
    }

    private void drawEdge(double startX, double startY, double endX, double endY, double weight)
    {
        double colorHNumber = 255 - (weight * 255);

        Line edge = new Line();
        edge.setStartX(startX);
        edge.setStartY(startY);
        edge.setEndX(endX);
        edge.setEndY(endY);
        edge.setStroke(Color.web("hsl(" + colorHNumber + ", 100%, 100%)"));

        this.getChildren().add(edge);
    }
}
