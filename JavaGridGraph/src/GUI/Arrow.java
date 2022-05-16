package GUI;

import javafx.beans.InvalidationListener;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class Arrow extends Group
{
    //region PARAMETERS

    private final Line line;
    private final Circle circle;

    //endregion



    //region CONSTRUCTORS

    public Arrow()
    {
        this(new Line(), new Circle());
    }

    private Arrow(Line line, Circle circle)
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

    //endregion



    //region PUBLIC METHODS

    public final void setStartX(double value) {
        line.setStartX(value);
    }

    public final double getStartX() {
        return line.getStartX();
    }

    public final DoubleProperty startXProperty() {
        return line.startXProperty();
    }

    public final void setStartY(double value) {
        line.setStartY(value);
    }

    public final double getStartY() {
        return line.getStartY();
    }

    public final DoubleProperty startYProperty() {
        return line.startYProperty();
    }

    public final void setEndX(double value) {
        line.setEndX(value);
    }

    public final double getEndX() {
        return line.getEndX();
    }

    public final DoubleProperty endXProperty() {
        return line.endXProperty();
    }

    public final void setEndY(double value) {
        line.setEndY(value);
    }

    public final double getEndY() {
        return line.getEndY();
    }

    public final DoubleProperty endYProperty() {
        return line.endYProperty();
    }

    public final void setFill(Color color)
    {
        line.setStroke(color);
        circle.setFill(color);
    }

    public final void setWidth(double width)
    {
        line.setStrokeWidth(width);
    }

    //endregion
}
