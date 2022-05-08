package App;

import javafx.event.EventHandler;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class GraphDrawer {

    private final Graph graph;
    private final double width; // horizontal space to draw a graph
    private final double height; // vertical space to draw a graph
    private final double topBarHeight;
    private final double weightMin;
    private final double weightMax;
    private boolean fromSelected;
    private int circleNumberFrom;

    public GraphDrawer(Graph graph, double sceneWidth, double sceneHeight, double topBarHeight, double weightMin, double weightMax) {
        this.graph = graph;
        this.width = sceneWidth - 20;
        this.height = sceneHeight - 20 - topBarHeight;
        this.topBarHeight = topBarHeight;
        this.weightMin = weightMin;
        this.weightMax = weightMax;
        fromSelected = false;
        circleNumberFrom = -1;
    }

    public AnchorPane drawGraph() {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setStyle("-fx-background-color: #000000");

        int r = graph.getRows();
        int c = graph.getColumns();

        double [] x = new double[c];
        double [] y = new double[r];

        // Count the radius of the circle (15 px margin from all sides in anchor pane)
        int diameterNumberInRows = 2 * r - 1;
        int diameterNumberInColumns = 2 * c - 1;
        double radius1 = ((width-30) / diameterNumberInColumns) / 2;
        double radius2 = ((height-30) / diameterNumberInRows) / 2;
        double radius = Math.max(radius1, radius2);
        if(((radius * 2) * diameterNumberInColumns > (width-30)) || ((radius * 2) * diameterNumberInRows > (height-30))) {
            radius = Math.min(radius1, radius2);
        }

        // First x and y coordinates; the other ones are relative to them
        x[0] = radius + 25;
        y[0] = radius + topBarHeight + 25;

        // Fill x and y coordinates; there is c different x coordinates and r different y coordinates
        for(int i = 1; i < c; i++) {
            x[i] = x[i-1] + 4 * radius;
        }
        for(int j = 1; j < r; j++) {
            y[j] = y[j-1] + 4 * radius;
        }

        Circle [] circles = createCircles(x, y, radius,r*c);
        drawEdges(x, y, radius,r*c, anchorPane);

        anchorPane.getChildren().addAll(circles);

        return anchorPane;
    }

    // Method that creates the array of circles with appropriate coordinates
    private Circle [] createCircles(double [] x, double [] y, double radius, int n) {
        Circle [] circles = new Circle[n];

        int j = 0;
        int k = 0;
        for(int i = 0; i < n; i++) {
            circles[i] = new Circle();
            circles[i].setCenterX(x[j++]);
            circles[i].setCenterY(y[k]);
            circles[i].setRadius(radius);
            circles[i].setFill(Color.LIGHTGREY);

            // Check if it is a new row; then x coordinates change from the beginning and y must be changed
            if((i+1) % x.length == 0) {
                j=0;
                k++;
            }

            // Add mouse event to each circle
            int finalI = i;
            circles[i].setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(mouseEvent.getButton() == MouseButton.PRIMARY) {
                        fromSelected = true;
                        circleNumberFrom = finalI;
                    }
                    else if(mouseEvent.getButton() == MouseButton.SECONDARY && fromSelected) {
                        // Here we can call Dijkstra algorithm
                        System.out.println("Dijkstra algorithm called with vertex numbers: "
                                + circleNumberFrom + " and " + finalI);
                    }
                }
            });
        }

        return circles;
    }

    private void drawEdges(double [] x, double [] y, double radius, int n, AnchorPane anchorPane) {
        int number;
        double weight;
        double startX, startY, endX, endY;
        int k = 0;
        int l = 0;
        for(int i = 0; i < n; i++) {
            Vertex v = graph.getVertex(i);
            // Loop for all neighbours of vertex number i and draw right and up edges
            for(int j = 0; j < v.getNumberOfNeighbours(); j++) {
                number = v.getNeighbourNumber(j);
                weight = v.getNeighbourWeight(j);

                // Right edge
                if((i + 1) == number) {
                    startX = x[k] + radius;
                    startY = y[l];
                    endX = x[k] + radius * 3;
                    endY = y[l];
                    drawEdge(startX, startY, endX, endY, weight, anchorPane);
                }
                // Up edge
                else if((i > (graph.getColumns()-1) && i > number) && ((i-number) % graph.getColumns() == 0)) {
                    startX = x[k];
                    startY = y[l] - radius;
                    endX = x[k];
                    endY = y[l] - radius * 3;
                    drawEdge(startX, startY, endX, endY, weight, anchorPane);
                }
            }
            k++;
            if((i+1) % x.length == 0) {
                k=0;
                l++;
            }
        }
    }

    private void drawEdge(double startX, double startY, double endX, double endY, double weight, AnchorPane anchorPane) {
        // Scale weight to range [0, 1]
        double newWeight = (weight - weightMin) / (weightMax - weightMin);
        // Count h number in hsl (0 is red and 255 is blue)
        double colorHNumber = 255 - (newWeight * 255);

        Line line = new Line();
        line.setStartX(startX);
        line.setStartY(startY);
        line.setEndX(endX);
        line.setEndY(endY);
        line.setStroke(Color.web("hsl(" + colorHNumber + ", 100%, 100%)"));

        anchorPane.getChildren().add(line);
    }

}
