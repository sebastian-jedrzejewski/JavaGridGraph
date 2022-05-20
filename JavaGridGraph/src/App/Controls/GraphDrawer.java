package App.Controls;

import Core.Graph;
import Core.GraphAlgorithms.Dijkstra;
import Core.Vertex;
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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class GraphDrawer extends AnchorPane
{
    //region PROPERTIES

    private Graph _graph = null;
    private double _minGraphsWeight = Double.NaN;
    private double _maxGraphsWeight = Double.NaN;

    private Insets _margin = new Insets(0);
    private double _verticesSpacingPercent = 0.5;
    private double _edgesSpacingPercent = 0.1;
    private double _edgesNormalWidthPercent = 0.05;
    private double _edgesSelectedWidthPercent = 0.15;
    private double _edgesNormalRadiusPercent = 0.1;
    private double _edgesSelectedRadiusPercent = 0.15;

    private Circle[] _vertices = null;
    private EdgesCollection[] _baseEdges = null;
    private ArrayList<Edge> _dijkstraEdges = null;


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
                updatePosition();
            }
        });
        this.heightProperty().addListener((obs, oldVal, newVal) -> {
            if (_graph != null)
            {
                updatePosition();
            }
        });
    }

    //endregion



    //region PUBLIC METHODS

    public Insets getMargin() { return _margin; }
    public void setMargin(Insets margin)
    {
        _margin = margin;
        if (_graph != null)
        {
            updatePosition();
        }
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

        fromVertexNumber = -1;
        toVerticesNumbers = new ArrayList<>();

        this.getChildren().clear();

        createVerticesAndEdges();
        updatePosition();
    }

    public void reset()
    {
        _vertices[fromVertexNumber].setFill(Color.WHITE);
        fromVertexNumber = -1;
        for (Integer vertex : toVerticesNumbers)
        {
            _vertices[vertex].setFill(Color.WHITE);
        }
        toVerticesNumbers = new ArrayList<>();
        unselectAllEdges();
        updatePosition();
    }

    //endregion



    //region PRIVATE METHODS

    private void createVerticesAndEdges()
    {
        int rows = _graph.getRows();
        int columns = _graph.getColumns();
        int vertices_count = rows * columns;

        _vertices = new Circle[vertices_count];
        _baseEdges = new EdgesCollection[vertices_count];
        _dijkstraEdges = new ArrayList<>();

        for (int i = 0; i < vertices_count; i++)
        {
            int vertexNumber = i;

            _vertices[vertexNumber] = new Circle();
            _vertices[vertexNumber].setFill(Color.WHITE);
            _vertices[vertexNumber].hoverProperty().addListener((obs, oldVal, newValue) ->
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
                _vertices[vertexNumber].setFill(color);
            });
            _vertices[vertexNumber].setOnMouseClicked(new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getButton() == MouseButton.PRIMARY)
                    {
                        if (fromVertexNumber == vertexNumber)
                        {
                            _vertices[fromVertexNumber].setFill(Color.WHITE);
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
                                _vertices[fromVertexNumber].setFill(Color.WHITE);
                            }

                            fromVertexNumber = vertexNumber;
                            _vertices[fromVertexNumber].setFill(Color.RED);
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
                                _vertices[vertexNumber].setFill(Color.WHITE);
                            }
                            else
                            {
                                toVerticesNumbers.add(vertexNumber);
                                _vertices[vertexNumber].setFill(Color.GREEN);
                            }
                            updateDijkstra();
                        }
                    }
                }
            });

            Vertex vertex = _graph.getVertex(i);
            _baseEdges[vertexNumber] = new EdgesCollection();
            if (vertex.hasNeighbourNumber(i - 1))
            {
                Edge edge = new Edge();
                edge.setFill(getEdgeColorByWeight(vertex.getNeighbourWeight(vertex.getNeighbourIndex(i - 1))));
                _baseEdges[vertexNumber].setLeft(i - 1, edge);
            }
            if (vertex.hasNeighbourNumber(i + 1))
            {
                Edge edge = new Edge();
                edge.setFill(getEdgeColorByWeight(vertex.getNeighbourWeight(vertex.getNeighbourIndex(i + 1))));
                _baseEdges[vertexNumber].setRight(i + 1, edge);
            }
            if (vertex.hasNeighbourNumber(i - columns))
            {
                Edge edge = new Edge();
                edge.setFill(getEdgeColorByWeight(vertex.getNeighbourWeight(vertex.getNeighbourIndex(i - columns))));
                _baseEdges[vertexNumber].setTop(i - columns, edge);
            }
            if (vertex.hasNeighbourNumber(i + columns))
            {
                Edge edge = new Edge();
                edge.setFill(getEdgeColorByWeight(vertex.getNeighbourWeight(vertex.getNeighbourIndex(i + columns))));
                _baseEdges[vertexNumber].setBottom(i + columns, edge);
            }
        }

        this.getChildren().addAll(_vertices);
        for (int i = 0; i < vertices_count; i++)
        {
            this.getChildren().addAll(_baseEdges[i].getAllEdges());
        }
    }

    private void updatePosition()
    {
        int rows = _graph.getRows();
        int columns = _graph.getColumns();
        int vertices_count = rows * columns;

        double width = ((Pane)(this.getParent())).getWidth() - (_margin.getLeft() + _margin.getRight());
        double height = ((Pane)(this.getParent())).getHeight() - (_margin.getTop() + _margin.getBottom());

        double verticalDiameter = height / (((1 + _verticesSpacingPercent) * rows) - _verticesSpacingPercent);
        double horizontalDiameter = width / (((1 + _verticesSpacingPercent) * columns) - _verticesSpacingPercent);
        double diameter = Math.min(verticalDiameter, horizontalDiameter);

        double x = diameter / 2 + _margin.getLeft();
        double y = diameter / 2 + _margin.getTop();
        for (int i = 0; i < vertices_count; i++)
        {
            _vertices[i].setCenterX(x);
            _vertices[i].setCenterY(y);
            _vertices[i].setRadius(diameter / 2);

            EdgesCollection baseVertexEdges = _baseEdges[i];
            if (baseVertexEdges.getLeft() != null)
            {
                Edge edge = baseVertexEdges.getLeft();

                double startX = x - (diameter / 2);
                double endX = startX - (diameter * _verticesSpacingPercent);
                double Y = y + (_edgesSpacingPercent * diameter);

                edge.setStartX(startX);
                edge.setStartY(Y);
                edge.setEndX(endX);
                edge.setEndY(Y);
                edge.setWidth(diameter * _edgesNormalWidthPercent);
                edge.setRadius(diameter * _edgesNormalRadiusPercent);
            }
            if (baseVertexEdges.getRight() != null)
            {
                Edge edge = baseVertexEdges.getRight();

                double startX = x + (diameter / 2);
                double endX = startX + (diameter * _verticesSpacingPercent);
                double Y = y - (_edgesSpacingPercent * diameter);

                edge.setStartX(startX);
                edge.setStartY(Y);
                edge.setEndX(endX);
                edge.setEndY(Y);
                edge.setWidth(diameter * _edgesNormalWidthPercent);
                edge.setRadius(diameter * _edgesNormalRadiusPercent);
            }
            if (baseVertexEdges.getTop() != null)
            {
                Edge edge = baseVertexEdges.getTop();

                double startY = y - (diameter / 2);
                double endY = startY - (diameter * _verticesSpacingPercent);
                double X = x - (_edgesSpacingPercent * diameter);

                edge.setStartX(X);
                edge.setStartY(startY);
                edge.setEndX(X);
                edge.setEndY(endY);
                edge.setWidth(diameter * _edgesNormalWidthPercent);
                edge.setRadius(diameter * _edgesNormalRadiusPercent);
            }
            if (baseVertexEdges.getBottom() != null)
            {
                Edge edge = baseVertexEdges.getBottom();

                double startY = y + (diameter / 2);
                double endY = startY + (diameter * _verticesSpacingPercent);
                double X = x + (_edgesSpacingPercent * diameter);

                edge.setStartX(X);
                edge.setStartY(startY);
                edge.setEndX(X);
                edge.setEndY(endY);
                edge.setWidth(diameter * _edgesNormalWidthPercent);
                edge.setRadius(diameter * _edgesNormalRadiusPercent);
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

        for (Edge edge : _dijkstraEdges)
        {
            edge.setWidth(diameter * _edgesSelectedWidthPercent);
            edge.setRadius(diameter * _edgesSelectedRadiusPercent);
        }
    }

    private void updateDijkstra()
    {
        unselectAllEdges();
        if (fromVertexNumber != -1)
        {
            _graph.refresh();
            for (Integer vertex: toVerticesNumbers)
            {
                dijkstra.dijkstraAlgorithm(fromVertexNumber, vertex);
                Vertex v = _graph.getVertex(vertex);
                while (v.getP() != -1)
                {
                    Edge edge = _baseEdges[v.getP()].getByNumber(v.getNumber());
                    edge.setFill(Color.WHITE);
                    _dijkstraEdges.add(edge);
                    v = _graph.getVertex(v.getP());
                }
            }
        }
        updatePosition();
    }

    private void unselectAllEdges()
    {
        int rows = _graph.getRows();
        int columns = _graph.getColumns();
        int vertices_count = rows * columns;
        for (int i = 0; i < vertices_count; i++)
        {
            Vertex vertex = _graph.getVertex(i);
            EdgesCollection edges = _baseEdges[i];
            if (edges.getLeft() != null)
            {
                edges.getLeft().setFill(getEdgeColorByWeight(vertex.getNeighbourWeight(vertex.getNeighbourIndex(edges.getLeftNumber()))));
            }
            if (edges.getRight() != null)
            {
                edges.getRight().setFill(getEdgeColorByWeight(vertex.getNeighbourWeight(vertex.getNeighbourIndex(edges.getRightNumber()))));
            }
            if (edges.getTop() != null)
            {
                edges.getTop().setFill(getEdgeColorByWeight(vertex.getNeighbourWeight(vertex.getNeighbourIndex(edges.getTopNumber()))));
            }
            if (edges.getBottom() != null)
            {
                edges.getBottom().setFill(getEdgeColorByWeight(vertex.getNeighbourWeight(vertex.getNeighbourIndex(edges.getBottomNumber()))));
            }
        }
        _dijkstraEdges.clear();
    }

    private Color getEdgeColorByWeight(double weight)
    {
        return Color.web("hsl(" + (255 - (((weight - _minGraphsWeight) / (_maxGraphsWeight - _minGraphsWeight)) * 255)) + ", 100%, 100%)");
    }

    //endregion



    //region NESTED CLASSES

    private class EdgesCollection
    {
        // PROPERTIES
        private Edge _left = null;
        private int _leftNumber = -1;
        private Edge _right = null;
        private int _rightNumber = -1;
        private Edge _top = null;
        private int _topNumber = -1;
        private Edge _bottom = null;
        private int _bottomNumber = -1;


        // PUBLIC METHODS
        public void setLeft(int number, Edge edge)
        {
            _leftNumber = number;
            _left = edge;
        }
        public Edge getLeft()
        {
            return _left;
        }
        public int getLeftNumber()
        {
            return _leftNumber;
        }

        public void setRight(int number, Edge edge)
        {
            _rightNumber = number;
            _right = edge;
        }
        public Edge getRight()
        {
            return _right;
        }
        public int getRightNumber()
        {
            return _rightNumber;
        }

        public void setTop(int number, Edge edge)
        {
            _topNumber = number;
            _top = edge;
        }
        public Edge getTop()
        {
            return _top;
        }
        public int getTopNumber()
        {
            return _topNumber;
        }

        public void setBottom(int number, Edge edge)
        {
            _bottomNumber = number;
            _bottom = edge;
        }
        public Edge getBottom()
        {
            return _bottom;
        }
        public int getBottomNumber()
        {
            return _bottomNumber;
        }

        public Edge getByNumber(int number)
        {
            if (_leftNumber == number)
            {
                return _left;
            }
            else if (_rightNumber == number)
            {
                return _right;
            }
            else if (_topNumber == number)
            {
                return _top;
            }
            else if (_bottomNumber == number)
            {
                return _bottom;
            }
            else
            {
                throw new IllegalArgumentException("Wrong vertex number");
            }
        }

        public ArrayList<Edge> getAllEdges()
        {
            ArrayList<Edge> list = new ArrayList<>();
            if (_left != null)
            {
                list.add(_left);
            }
            if (_right != null)
            {
                list.add(_right);
            }
            if (_top != null)
            {
                list.add(_top);
            }
            if (_bottom != null)
            {
                list.add(_bottom);
            }
            return list;
        }
    }

    private class Edge extends Group
    {
        // PROPERTIES
        private final Line _line;
        private final Circle _circle;


        // CONSTRUCTORS
        public Edge()
        {
            this(new Line(), new Circle());
        }

        private Edge(Line line, Circle circle)
        {
            super(line, circle);
            this._line = line;
            this._circle = circle;
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
            _line.setStartX(value);
        }
        public final double getStartX()
        {
            return _line.getStartX();
        }
        public final DoubleProperty startXProperty()
        {
            return _line.startXProperty();
        }

        public final void setStartY(double value)
        {
            _line.setStartY(value);
        }
        public final double getStartY()
        {
            return _line.getStartY();
        }
        public final DoubleProperty startYProperty()
        {
            return _line.startYProperty();
        }

        public final void setEndX(double value)
        {
            _line.setEndX(value);
        }
        public final double getEndX()
        {
            return _line.getEndX();
        }
        public final DoubleProperty endXProperty()
        {
            return _line.endXProperty();
        }

        public final void setEndY(double value)
        {
            _line.setEndY(value);
        }
        public final double getEndY()
        {
            return _line.getEndY();
        }
        public final DoubleProperty endYProperty()
        {
            return _line.endYProperty();
        }

        public final void setFill(Color color)
        {
            _line.setStroke(color);
            _circle.setFill(color);
        }

        public final void setWidth(double width)
        {
            _line.setStrokeWidth(width);
        }

        public final void setRadius(double radius)
        {
            _circle.setRadius(radius);
        }
    }

    //endregion
}
