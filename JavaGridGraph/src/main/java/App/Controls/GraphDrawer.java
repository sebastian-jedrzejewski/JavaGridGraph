package app.controls;

import core.Graph;
import core.graphalgorithms.Dijkstra;
import core.helpers.Range;
import core.Vertex;
import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class GraphDrawer extends AnchorPane
{
    //region PROPERTIES

    private Graph graph;

    private Insets margin;

    private Circle[] vertices;
    private EdgesCollection[] baseEdges;
    private ArrayList<Edge> selectedEdges;

    private Dijkstra dijkstra;
    private int fromVertexNumber;
    private ArrayList<Integer> toVerticesNumbers;

    //endregion



    //region CONSTRUCTORS

    public GraphDrawer()
    {
        this.setStyle("-fx-background-color: #000000");
        this.widthProperty().addListener((obs, oldVal, newVal) -> {
            if (this.graph != null)
            {
                updatePosition();
            }
        });
        this.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (this.graph != null)
            {
                updatePosition();
            }
        });

        this.graph = null;

        this.margin = new Insets(0);

        this.vertices = new Circle[0];
        this.baseEdges = new EdgesCollection[0];
        this.selectedEdges = new ArrayList<>();

        this.dijkstra = null;
        this.fromVertexNumber = -1;
        this.toVerticesNumbers = new ArrayList<>();
    }

    //endregion



    //region PUBLIC METHODS

    public Graph getGraph() { return graph; }
    public void setGraph(Graph graph)
    {
        fromVertexNumber = -1;
        toVerticesNumbers = new ArrayList<>();

        this.getChildren().clear();

        if (graph != null)
        {
            this.graph = graph;
            dijkstra = new Dijkstra(graph);

            int width = (int)graph.getSize().getWidth();
            int verticesCount = (int)graph.getSize().getArea();

            vertices = new Circle[verticesCount];
            baseEdges = new EdgesCollection[verticesCount];
            selectedEdges = new ArrayList<>();

            for (int i = 0; i < verticesCount; i++)
            {
                int vertexNumber = i;

                vertices[vertexNumber] = new Circle();
                vertices[vertexNumber].setFill(Color.WHITE);
                vertices[vertexNumber].hoverProperty().addListener((obs, oldVal, newValue) ->
                {
                    Color color;
                    if (newValue)
                    {
                        if (vertexNumber == fromVertexNumber)
                        {
                            color = Color.DARKRED;
                        }
                        else if (toVerticesNumbers.contains(vertexNumber))
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
                        if (vertexNumber == fromVertexNumber)
                        {
                            color = Color.RED;
                        }
                        else if (toVerticesNumbers.contains(vertexNumber))
                        {
                            color = Color.GREEN;
                        }
                        else
                        {
                            color = Color.WHITE;
                        }
                    }
                    vertices[vertexNumber].setFill(color);
                });
                vertices[vertexNumber].setOnMouseClicked(new EventHandler<MouseEvent>()
                {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (mouseEvent.getButton() == MouseButton.PRIMARY)
                        {
                            if (fromVertexNumber == vertexNumber)
                            {
                                vertices[fromVertexNumber].setFill(Color.WHITE);
                                fromVertexNumber = -1;
                            }
                            else
                            {
                                if (toVerticesNumbers.contains(vertexNumber))
                                {
                                    toVerticesNumbers.remove((Integer)vertexNumber);
                                }

                                if (fromVertexNumber != -1)
                                {
                                    vertices[fromVertexNumber].setFill(Color.WHITE);
                                }

                                fromVertexNumber = vertexNumber;
                                vertices[fromVertexNumber].setFill(Color.RED);
                            }
                            updateDijkstra();
                        }
                        else if (mouseEvent.getButton() == MouseButton.SECONDARY)
                        {
                            if (fromVertexNumber != vertexNumber)
                            {
                                if (toVerticesNumbers.contains(vertexNumber))
                                {
                                    toVerticesNumbers.remove((Integer)vertexNumber);
                                    vertices[vertexNumber].setFill(Color.WHITE);
                                }
                                else
                                {
                                    toVerticesNumbers.add(vertexNumber);
                                    vertices[vertexNumber].setFill(Color.GREEN);
                                }
                                updateDijkstra();
                            }
                        }
                    }
                });

                Vertex vertex = graph.getVertex(i);
                baseEdges[vertexNumber] = new EdgesCollection();
                int row = i / width;
                Range graphWeightRange = graph.getWeightRange();
                if (vertex.hasNeighbourNumber(i - 1) && (i - 1) / width == row)
                {
                    Edge edge = new Edge();
                    edge.setFill(Color.web("hsl(" + (255 - (((vertex.getNeighbourWeight(vertex.getNeighbourIndex(i - 1)) - graphWeightRange.getLow()) / (graphWeightRange.getDifference())) * 255)) + ", 100%, 100%)"));
                    baseEdges[vertexNumber].setLeft(i - 1, edge);
                }
                if (vertex.hasNeighbourNumber(i + 1) && (i + 1) / width == row)
                {
                    Edge edge = new Edge();
                    edge.setFill(Color.web("hsl(" + (255 - (((vertex.getNeighbourWeight(vertex.getNeighbourIndex(i + 1)) - graphWeightRange.getLow()) / (graphWeightRange.getDifference())) * 255)) + ", 100%, 100%)"));
                    baseEdges[vertexNumber].setRight(i + 1, edge);
                }
                if (vertex.hasNeighbourNumber(i - width))
                {
                    Edge edge = new Edge();
                    edge.setFill(Color.web("hsl(" + (255 - (((vertex.getNeighbourWeight(vertex.getNeighbourIndex(i - width)) - graphWeightRange.getLow()) / (graphWeightRange.getDifference())) * 255)) + ", 100%, 100%)"));
                    baseEdges[vertexNumber].setTop(i - width, edge);
                }
                if (vertex.hasNeighbourNumber(i + width))
                {
                    Edge edge = new Edge();
                    edge.setFill(Color.web("hsl(" + (255 - (((vertex.getNeighbourWeight(vertex.getNeighbourIndex(i + width)) - graphWeightRange.getLow()) / (graphWeightRange.getDifference())) * 255)) + ", 100%, 100%)"));
                    baseEdges[vertexNumber].setBottom(i + width, edge);
                }
            }

            this.getChildren().addAll(vertices);
            for (int i = 0; i < verticesCount; i++)
            {
                this.getChildren().addAll(baseEdges[i].getAllEdges());
            }

            updatePosition();
        }
    }

    public Insets getMargin() { return margin; }
    public void setMargin(Insets margin)
    {
        this.margin = margin;
        if (graph != null)
        {
            updatePosition();
        }
    }

    public void reset()
    {
        vertices[fromVertexNumber].setFill(Color.WHITE);
        fromVertexNumber = -1;
        for (Integer vertex : toVerticesNumbers)
        {
            vertices[vertex].setFill(Color.WHITE);
        }
        toVerticesNumbers = new ArrayList<>();

        unselectAllEdges();
        updatePosition();
    }

    //endregion



    //region PRIVATE METHODS

    private void updatePosition()
    {
        final double verticesSpacingRatio = 0.5;
        final double edgesSpacingRatio = 0.1;
        final double edgesNormalWidthRatio = 0.05;
        final double edgesSelectedWidthRatio = 0.15;
        final double edgesNormalRadiusRatio = 0.1;
        final double edgesSelectedRadiusRatio = 0.15;

        int graphHeight = (int)graph.getSize().getHeight();
        int graphWidth = (int)graph.getSize().getWidth();
        int verticesCount = (int)graph.getSize().getArea();

        double paneWidth = ((Pane)(this.getParent())).getWidth() - (margin.getLeft() + margin.getRight());
        double paneHeight = ((Pane)(this.getParent())).getHeight() - (margin.getTop() + margin.getBottom());

        double verticalDiameter = paneHeight / (((1 + verticesSpacingRatio) * graphHeight) - verticesSpacingRatio);
        double horizontalDiameter = paneWidth / (((1 + verticesSpacingRatio) * graphWidth) - verticesSpacingRatio);
        double diameter = Math.min(verticalDiameter, horizontalDiameter);

        double x = diameter / 2 + margin.getLeft();
        double y = diameter / 2 + margin.getTop();
        for (int i = 0; i < verticesCount; i++)
        {
            vertices[i].setCenterX(x);
            vertices[i].setCenterY(y);
            vertices[i].setRadius(diameter / 2);

            EdgesCollection baseVertexEdges = baseEdges[i];
            if (baseVertexEdges.getLeft() != null)
            {
                Edge edge = baseVertexEdges.getLeft();

                double startX = x - (diameter / 2);
                double endX = startX - (diameter * verticesSpacingRatio);
                double Y = y + (edgesSpacingRatio * diameter);

                edge.setStartX(startX);
                edge.setStartY(Y);
                edge.setEndX(endX);
                edge.setEndY(Y);
                edge.setLineWidth(diameter * edgesNormalWidthRatio);
                edge.setCircleRadius(diameter * edgesNormalRadiusRatio);
            }
            if (baseVertexEdges.getRight() != null)
            {
                Edge edge = baseVertexEdges.getRight();

                double startX = x + (diameter / 2);
                double endX = startX + (diameter * verticesSpacingRatio);
                double Y = y - (edgesSpacingRatio * diameter);

                edge.setStartX(startX);
                edge.setStartY(Y);
                edge.setEndX(endX);
                edge.setEndY(Y);
                edge.setLineWidth(diameter * edgesNormalWidthRatio);
                edge.setCircleRadius(diameter * edgesNormalRadiusRatio);
            }
            if (baseVertexEdges.getTop() != null)
            {
                Edge edge = baseVertexEdges.getTop();

                double startY = y - (diameter / 2);
                double endY = startY - (diameter * verticesSpacingRatio);
                double X = x - (edgesSpacingRatio * diameter);

                edge.setStartX(X);
                edge.setStartY(startY);
                edge.setEndX(X);
                edge.setEndY(endY);
                edge.setLineWidth(diameter * edgesNormalWidthRatio);
                edge.setCircleRadius(diameter * edgesNormalRadiusRatio);
            }
            if (baseVertexEdges.getBottom() != null)
            {
                Edge edge = baseVertexEdges.getBottom();

                double startY = y + (diameter / 2);
                double endY = startY + (diameter * verticesSpacingRatio);
                double X = x + (edgesSpacingRatio * diameter);

                edge.setStartX(X);
                edge.setStartY(startY);
                edge.setEndX(X);
                edge.setEndY(endY);
                edge.setLineWidth(diameter * edgesNormalWidthRatio);
                edge.setCircleRadius(diameter * edgesNormalRadiusRatio);
            }

            if (((i + 1) % graphWidth) == 0)
            {
                x = diameter / 2 + margin.getLeft();
                y += (1 + verticesSpacingRatio) * diameter;
            }
            else
            {
                x += (1 + verticesSpacingRatio) * diameter;
            }
        }

        for (Edge edge : selectedEdges)
        {
            edge.setLineWidth(diameter * edgesSelectedWidthRatio);
            edge.setCircleRadius(diameter * edgesSelectedRadiusRatio);
        }
    }

    private void updateDijkstra()
    {
        unselectAllEdges();
        if (fromVertexNumber != -1)
        {
            for (Integer vertex: toVerticesNumbers)
            {
                dijkstra.dijkstraAlgorithm(fromVertexNumber, vertex);
                Vertex v = graph.getVertex(vertex);
                while (v.getP() != -1)
                {
                    Edge edge = baseEdges[v.getP()].getByNumber(v.getNumber());
                    edge.setFill(Color.WHITE);
                    selectedEdges.add(edge);
                    v = graph.getVertex(v.getP());
                }
            }
        }
        updatePosition();
    }

    private void unselectAllEdges()
    {
        Range graphWeightRange = graph.getWeightRange();
        int verticesCount = (int)graph.getSize().getArea();
        for (int i = 0; i < verticesCount; i++)
        {
            Vertex vertex = graph.getVertex(i);
            EdgesCollection edges = baseEdges[i];
            if (edges.getLeft() != null)
            {
                edges.getLeft().setFill(Color.web("hsl(" + (255 - (((vertex.getNeighbourWeight(vertex.getNeighbourIndex(edges.getLeftNumber())) - graphWeightRange.getLow()) / (graphWeightRange.getDifference())) * 255)) + ", 100%, 100%)"));
            }
            if (edges.getRight() != null)
            {
                edges.getRight().setFill(Color.web("hsl(" + (255 - (((vertex.getNeighbourWeight(vertex.getNeighbourIndex(edges.getRightNumber())) - graphWeightRange.getLow()) / (graphWeightRange.getDifference())) * 255)) + ", 100%, 100%)"));
            }
            if (edges.getTop() != null)
            {
                edges.getTop().setFill(Color.web("hsl(" + (255 - (((vertex.getNeighbourWeight(vertex.getNeighbourIndex(edges.getTopNumber())) - graphWeightRange.getLow()) / (graphWeightRange.getDifference())) * 255)) + ", 100%, 100%)"));
            }
            if (edges.getBottom() != null)
            {
                edges.getBottom().setFill(Color.web("hsl(" + (255 - (((vertex.getNeighbourWeight(vertex.getNeighbourIndex(edges.getBottomNumber())) - graphWeightRange.getLow()) / (graphWeightRange.getDifference())) * 255)) + ", 100%, 100%)"));
            }
        }
        selectedEdges.clear();
    }

    //endregion



    //region NESTED CLASSES

    private class EdgesCollection
    {
        // PROPERTIES
        private Edge left = null;
        private int leftNumber = -1;
        private Edge right = null;
        private int rightNumber = -1;
        private Edge top = null;
        private int topNumber = -1;
        private Edge bottom = null;
        private int bottomNumber = -1;


        // PUBLIC METHODS
        public void setLeft(int number, Edge edge)
        {
            leftNumber = number;
            left = edge;
        }
        public Edge getLeft()
        {
            return left;
        }
        public int getLeftNumber()
        {
            return leftNumber;
        }

        public void setRight(int number, Edge edge)
        {
            rightNumber = number;
            right = edge;
        }
        public Edge getRight()
        {
            return right;
        }
        public int getRightNumber()
        {
            return rightNumber;
        }

        public void setTop(int number, Edge edge)
        {
            topNumber = number;
            top = edge;
        }
        public Edge getTop()
        {
            return top;
        }
        public int getTopNumber()
        {
            return topNumber;
        }

        public void setBottom(int number, Edge edge)
        {
            bottomNumber = number;
            bottom = edge;
        }
        public Edge getBottom()
        {
            return bottom;
        }
        public int getBottomNumber()
        {
            return bottomNumber;
        }

        public Edge getByNumber(int number)
        {
            if (leftNumber == number)
            {
                return left;
            }
            else if (rightNumber == number)
            {
                return right;
            }
            else if (topNumber == number)
            {
                return top;
            }
            else if (bottomNumber == number)
            {
                return bottom;
            }
            else
            {
                throw new IllegalArgumentException("Wrong vertex number");
            }
        }

        public ArrayList<Edge> getAllEdges()
        {
            ArrayList<Edge> list = new ArrayList<>();
            if (left != null)
            {
                list.add(left);
            }
            if (right != null)
            {
                list.add(right);
            }
            if (top != null)
            {
                list.add(top);
            }
            if (bottom != null)
            {
                list.add(bottom);
            }
            return list;
        }
    }

    private class Edge extends Group
    {
        // PROPERTIES
        private final Line line;
        private final Circle circle;


        // CONSTRUCTORS
        public Edge()
        {
            this(new Line(), new Circle());
        }

        private Edge(Line line, Circle circle)
        {
            super(line, circle);
            this.line = line;
            this.circle = circle;
            circle.setRadius(3);
            InvalidationListener updater = o ->
            {
                double endX = getEndX();
                double endY = getEndY();
                double startX = getStartX();
                double startY = getStartY();

                line.setStartX(startX);
                line.setStartY(startY);
                line.setEndX(endX);
                line.setEndY(endY);
                circle.setCenterX(endX);
                circle.setCenterY(endY);
            };
            // add updater to properties
            startXProperty().addListener(updater);
            startYProperty().addListener(updater);
            endXProperty().addListener(updater);
            endYProperty().addListener(updater);
            updater.invalidated(null);
        }


        // PUBLIC METHODS
        public final void setStartX(double value)
        {
            line.setStartX(value);
        }
        public final double getStartX()
        {
            return line.getStartX();
        }
        public final DoubleProperty startXProperty()
        {
            return line.startXProperty();
        }

        public final void setStartY(double value)
        {
            line.setStartY(value);
        }
        public final double getStartY()
        {
            return line.getStartY();
        }
        public final DoubleProperty startYProperty()
        {
            return line.startYProperty();
        }

        public final void setEndX(double value)
        {
            line.setEndX(value);
        }
        public final double getEndX()
        {
            return line.getEndX();
        }
        public final DoubleProperty endXProperty()
        {
            return line.endXProperty();
        }

        public final void setEndY(double value)
        {
            line.setEndY(value);
        }
        public final double getEndY()
        {
            return line.getEndY();
        }
        public final DoubleProperty endYProperty()
        {
            return line.endYProperty();
        }

        public final void setFill(Paint color)
        {
            line.setStroke(color);
            circle.setFill(color);
        }
        public final Paint getFill()
        {
            return line.getStroke();
        }

        public final void setLineWidth(double width)
        {
            line.setStrokeWidth(width);
        }
        public final double getLineWidth()
        {
            return line.getStrokeWidth();
        }

        public final void setCircleRadius(double radius)
        {
            circle.setRadius(radius);
        }
        public final double getCircleRadius()
        {
            return circle.getRadius();
        }
    }

    //endregion
}
