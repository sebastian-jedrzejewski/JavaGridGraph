package App.Stages;

import App.Controls.NumericTextField;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.SetChangeListener;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class NewGraphStage extends Stage
{
    //region PROPERTIES



    //endregion



    //region CONSTRUCTORS

    public NewGraphStage()
    {
        GridPane basePane = new GridPane();
        basePane.setPadding(new Insets(10));
        Scene scene = new Scene(basePane, 500, 500);

        // Initialize view
        GridPane dimensionsPane = new GridPane();
        dimensionsPane.setHgap(10);
        dimensionsPane.setVgap(10);
        basePane.add(dimensionsPane, 0, 0);

        Label widthLabel = new Label("Number of columns (width)");
        dimensionsPane.add(widthLabel, 0, 0);

        NumericTextField widthField = new NumericTextField(NumericTextField.Type.PositiveInteger);
        dimensionsPane.add(widthField, 1, 0);

        Label heightLabel = new Label("Number of rows (height)");
        dimensionsPane.add(heightLabel, 0, 1);

        NumericTextField heightField = new NumericTextField(NumericTextField.Type.PositiveInteger);
        dimensionsPane.add(heightField, 1, 1);

        GridPane weightPane = new GridPane();
        weightPane.setHgap(10);
        basePane.add(weightPane, 0, 1);

        Label weightLabel = new Label("Weight of edges (min and max)");
        weightPane.add(weightLabel, 0, 0);

        NumericTextField minWeightField = new NumericTextField(NumericTextField.Type.PositiveDouble, "0");
        weightPane.add(minWeightField, 1, 0);

        NumericTextField maxWeightField = new NumericTextField(NumericTextField.Type.PositiveDouble, "1");
        weightPane.add(maxWeightField, 2, 0);


        this.setTitle("New");
        this.setScene(scene);
    }

    //endregion
}
