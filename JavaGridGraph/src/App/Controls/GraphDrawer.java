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
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.util.ArrayList;

public class GraphDrawer extends AnchorPane
{
    //region PROPERTIES

    private Graph _graph;
    private double _minGraphsWeight;
    private double _maxGraphsWeight;

    private Insets _margin;
    private double _verticesSpacingRatio;
    private double _edgesSpacingRatio;
    private double _edgesNormalWidthRatio;
    private double _edgesSelectedWidthRatio;
    private double _edgesNormalRadiusRatio;
    private double _edgesSelectedRadiusRatio;

    private Circle[] _vertices;
    private EdgesCollection[] _baseEdges;
    private ArrayList<Edge> _dijkstraEdges;

    private Dijkstra _dijkstra;
    private int _fromVertexNumber;
    private ArrayList<Integer> _toVerticesNumbers;

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

        _graph = null;
        _minGraphsWeight = Double.NaN;
        _maxGraphsWeight = Double.NaN;

        _margin = new Insets(0);
        _verticesSpacingRatio = 0.5;
        _edgesSpacingRatio = 0.1;
        _edgesNormalWidthRatio = 0.05;
        _edgesSelectedWidthRatio = 0.15;
        _edgesNormalRadiusRatio = 0.1;
        _edgesSelectedRadiusRatio = 0.15;

        _vertices = new Circle[0];
        _baseEdges = new EdgesCollection[0];
        _dijkstraEdges = new ArrayList<>();

        _dijkstra = null;
        _fromVertexNumber = -1;
        _toVerticesNumbers = new ArrayList<>();
    }

    //endregion



    //region PUBLIC METHODS

    public Graph getGraph() { return _graph; }
    public void setGraph(Graph graph)
    {
        _fromVertexNumber = -1;
        _toVerticesNumbers = new ArrayList<>();

        this.getChildren().clear();

        if (graph != null)
        {
            _minGraphsWeight = Double.MAX_VALUE;
            _maxGraphsWeight = 0;
            _graph = graph;
            _dijkstra = new Dijkstra(_graph);

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
                        if (vertexNumber == _fromVertexNumber)
                        {
                            color = Color.DARKRED;
                        }
                        else if (_toVerticesNumbers.contains(vertexNumber))
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
                        if (vertexNumber == _fromVertexNumber)
                        {
                            color = Color.RED;
                        }
                        else if (_toVerticesNumbers.contains(vertexNumber))
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
                            if (_fromVertexNumber == vertexNumber)
                            {
                                _vertices[_fromVertexNumber].setFill(Color.WHITE);
                                _fromVertexNumber = -1;
                            }
                            else
                            {
                                if (_toVerticesNumbers.contains(vertexNumber))
                                {
                                    _toVerticesNumbers.remove((Integer)vertexNumber);
                                }

                                if (_fromVertexNumber != -1)
                                {
                                    _vertices[_fromVertexNumber].setFill(Color.WHITE);
                                }

                                _fromVertexNumber = vertexNumber;
                                _vertices[_fromVertexNumber].setFill(Color.RED);
                            }
                            updateDijkstra();
                        }
                        else if (mouseEvent.getButton() == MouseButton.SECONDARY)
                        {
                            if (_fromVertexNumber != vertexNumber)
                            {
                                if (_toVerticesNumbers.contains(vertexNumber))
                                {
                                    _toVerticesNumbers.remove((Integer)vertexNumber);
                                    _vertices[vertexNumber].setFill(Color.WHITE);
                                }
                                else
                                {
                                    _toVerticesNumbers.add(vertexNumber);
                                    _vertices[vertexNumber].setFill(Color.GREEN);
                                }
                                updateDijkstra();
                            }
                        }
                    }
                });

                Vertex vertex = _graph.getVertex(i);
                _baseEdges[vertexNumber] = new EdgesCollection();
                int row = i / columns;
                if (vertex.hasNeighbourNumber(i - 1) && (i - 1) / columns == row)
                {
                    Edge edge = new Edge();
                    edge.setFill(getEdgeColorByWeight(vertex.getNeighbourWeight(vertex.getNeighbourIndex(i - 1))));
                    _baseEdges[vertexNumber].setLeft(i - 1, edge);
                }
                if (vertex.hasNeighbourNumber(i + 1) && (i + 1) / columns == row)
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

            updatePosition();
        }
    }

    public Insets getMargin() { return _margin; }
    public void setMargin(Insets margin)
    {
        _margin = margin;
        if (_graph != null)
        {
            updatePosition();
        }
    }
    
    public double getVerticesSpacingRatio()
    {
        return _verticesSpacingRatio;
    }
    public void setVerticesSpacingRatio(double value)
    {
        _verticesSpacingRatio = value;
        if (_graph != null)
        {
            updatePosition();
        }
    }
    
    public double getEdgesSpacingRatio()
    {
        return _edgesSpacingRatio;
    }
    public void setEdgesSpacingRatio(double value)
    {
        _edgesSpacingRatio = value;
        if (_graph != null)
        {
            updatePosition();
        }
    }

    public double getEdgesNormalWidthRatio()
    {
        return _edgesNormalWidthRatio;
    }
    public void setEdgesNormalWidthRatio(double value)
    {
        _edgesNormalWidthRatio = value;
        if (_graph != null)
        {
            updatePosition();
        }
    }

    public double getEdgesSelectedWidthRatio()
    {
        return _edgesSelectedWidthRatio;
    }
    public void setEdgesSelectedWidthRatio(double value)
    {
        _edgesSelectedWidthRatio = value;
        if (_graph != null)
        {
            updatePosition();
        }
    }

    public double getEdgesNormalRadiusRatio()
    {
        return _edgesNormalRadiusRatio;
    }
    public void setEdgesNormalRadiusRatio(double value)
    {
        _edgesNormalRadiusRatio = value;
        if (_graph != null)
        {
            updatePosition();
        }
    }

    public double getEdgesSelectedRadiusRatio()
    {
        return _edgesSelectedRadiusRatio;
    }
    public void setEdgesSelectedRadiusRatio(double value) 
    {
        _edgesSelectedRadiusRatio = value;
        if (_graph != null) 
        {
            updatePosition();
        }
    }

    public void reset()
    {
        _vertices[_fromVertexNumber].setFill(Color.WHITE);
        _fromVertexNumber = -1;
        for (Integer vertex : _toVerticesNumbers)
        {
            _vertices[vertex].setFill(Color.WHITE);
        }
        _toVerticesNumbers = new ArrayList<>();

        unselectAllEdges();
        updatePosition();
    }

    //endregion



    //region PRIVATE METHODS

    private void updatePosition()
    {
        int rows = _graph.getRows();
        int columns = _graph.getColumns();
        int vertices_count = rows * columns;

        double width = ((Pane)(this.getParent())).getWidth() - (_margin.getLeft() + _margin.getRight());
        double height = ((Pane)(this.getParent())).getHeight() - (_margin.getTop() + _margin.getBottom());

        double verticalDiameter = height / (((1 + _verticesSpacingRatio) * rows) - _verticesSpacingRatio);
        double horizontalDiameter = width / (((1 + _verticesSpacingRatio) * columns) - _verticesSpacingRatio);
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
                double endX = startX - (diameter * _verticesSpacingRatio);
                double Y = y + (_edgesSpacingRatio * diameter);

                edge.setStartX(startX);
                edge.setStartY(Y);
                edge.setEndX(endX);
                edge.setEndY(Y);
                edge.setLineWidth(diameter * _edgesNormalWidthRatio);
                edge.setCircleRadius(diameter * _edgesNormalRadiusRatio);
            }
            if (baseVertexEdges.getRight() != null)
            {
                Edge edge = baseVertexEdges.getRight();

                double startX = x + (diameter / 2);
                double endX = startX + (diameter * _verticesSpacingRatio);
                double Y = y - (_edgesSpacingRatio * diameter);

                edge.setStartX(startX);
                edge.setStartY(Y);
                edge.setEndX(endX);
                edge.setEndY(Y);
                edge.setLineWidth(diameter * _edgesNormalWidthRatio);
                edge.setCircleRadius(diameter * _edgesNormalRadiusRatio);
            }
            if (baseVertexEdges.getTop() != null)
            {
                Edge edge = baseVertexEdges.getTop();

                double startY = y - (diameter / 2);
                double endY = startY - (diameter * _verticesSpacingRatio);
                double X = x - (_edgesSpacingRatio * diameter);

                edge.setStartX(X);
                edge.setStartY(startY);
                edge.setEndX(X);
                edge.setEndY(endY);
                edge.setLineWidth(diameter * _edgesNormalWidthRatio);
                edge.setCircleRadius(diameter * _edgesNormalRadiusRatio);
            }
            if (baseVertexEdges.getBottom() != null)
            {
                Edge edge = baseVertexEdges.getBottom();

                double startY = y + (diameter / 2);
                double endY = startY + (diameter * _verticesSpacingRatio);
                double X = x + (_edgesSpacingRatio * diameter);

                edge.setStartX(X);
                edge.setStartY(startY);
                edge.setEndX(X);
                edge.setEndY(endY);
                edge.setLineWidth(diameter * _edgesNormalWidthRatio);
                edge.setCircleRadius(diameter * _edgesNormalRadiusRatio);
            }

            if (((i + 1) % columns) == 0)
            {
                x = diameter / 2 + _margin.getLeft();
                y += (1 + _verticesSpacingRatio) * diameter;
            }
            else
            {
                x += (1 + _verticesSpacingRatio) * diameter;
            }
        }

        for (Edge edge : _dijkstraEdges)
        {
            edge.setLineWidth(diameter * _edgesSelectedWidthRatio);
            edge.setCircleRadius(diameter * _edgesSelectedRadiusRatio);
        }
    }

    private void updateDijkstra()
    {
        unselectAllEdges();
        if (_fromVertexNumber != -1)
        {
            _graph.refresh();
            for (Integer vertex: _toVerticesNumbers)
            {
                _dijkstra.dijkstraAlgorithm(_fromVertexNumber, vertex);
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

        public final void setFill(Paint color)
        {
            _line.setStroke(color);
            _circle.setFill(color);
        }
        public final Paint getFill()
        {
            return _line.getStroke();
        }

        public final void setLineWidth(double width)
        {
            _line.setStrokeWidth(width);
        }
        public final double getLineWidth()
        {
            return _line.getStrokeWidth();
        }

        public final void setCircleRadius(double radius)
        {
            _circle.setRadius(radius);
        }
        public final double getCircleRadius()
        {
            return _circle.getRadius();
        }
    }

    //endregion
}
