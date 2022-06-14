package app.stages;

import app.controls.NumericTextField;
import core.Graph;
import core.helpers.Dimensions2D;
import core.helpers.Range;
import core.graphalgorithms.GraphUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.Size;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class NewGraphStage extends Stage
{
    //region PROPERTIES

    private Graph graph;

    private final NumericTextField widthField;
    private final NumericTextField heightField;
    private final NumericTextField minWeightField;
    private final NumericTextField maxWeightField;
    private final RadioButton directedGraphRadioButton;
    private final RadioButton notDirectedGraphRadioButton;
    private final Label outputEdgesLabel;
    private final NumericTextField minOutputEdgesField;
    private final NumericTextField maxOutputEdgesField;
    private final Label inputEdgesLabel;
    private final GridPane inputEdgesFieldPane;
    private final NumericTextField minInputEdgesField;
    private final NumericTextField maxInputEdgesField;
    private final NumericTextField customSeedField;
    private final CheckBox loadCheckBox;
    private final CheckBox saveToFileCheckBox;

    //endregion



    //region CONSTRUCTORS

    public NewGraphStage()
    {
        graph = null;

        BorderPane basePane = new BorderPane();
        basePane.setPadding(new Insets(10));
        Scene scene = new Scene(basePane);

        // Initialize view
        GridPane labelFieldPane = new GridPane();
        labelFieldPane.setHgap(10);
        labelFieldPane.setVgap(10);
        BorderPane.setMargin(labelFieldPane, new Insets(0, 0, 15, 0));
        basePane.setTop(labelFieldPane);

        Label widthLabel = new Label("Number of columns (width)");
        labelFieldPane.add(widthLabel, 0, 0);

        widthField = new NumericTextField(NumericTextField.Type.PositiveInteger);
        widthField.setMinValue(1);
        labelFieldPane.add(widthField, 1, 0);

        Label heightLabel = new Label("Number of rows (height)");
        labelFieldPane.add(heightLabel, 0, 1);

        heightField = new NumericTextField(NumericTextField.Type.PositiveInteger);
        heightField.setMinValue(1);
        labelFieldPane.add(heightField, 1, 1);

        Separator separator1 = new Separator(Orientation.HORIZONTAL);
        labelFieldPane.add(separator1, 0, 2,2,1);

        Label weightLabel = new Label("Weight of edges");
        labelFieldPane.add(weightLabel, 0, 3);

        GridPane weightFieldPane = new GridPane();
        weightFieldPane.setHgap(5);
        weightFieldPane.setVgap(5);
        labelFieldPane.add(weightFieldPane, 1, 3);

        Label minWeightLabel = new Label("Min");
        weightFieldPane.add(minWeightLabel, 0 , 0);

        minWeightField = new NumericTextField(NumericTextField.Type.PositiveDouble, "0");
        minWeightField.setMinValue(0);
        weightFieldPane.add(minWeightField, 1 , 0);

        Label maxWeightLabel = new Label("Max");
        weightFieldPane.add(maxWeightLabel, 0 , 1);

        maxWeightField = new NumericTextField(NumericTextField.Type.PositiveDouble, "1");
        maxWeightField.setMinValue(0);
        weightFieldPane.add(maxWeightField, 1 , 1);

        Separator separator2 = new Separator(Orientation.HORIZONTAL);
        labelFieldPane.add(separator2, 0, 4,2,1);

        Label directedGraphLabel = new Label("Graph type");
        labelFieldPane.add(directedGraphLabel, 0, 5);

        VBox directedGraphRadiobuttonPane = new VBox();
        directedGraphRadiobuttonPane.setSpacing(10);
        labelFieldPane.add(directedGraphRadiobuttonPane, 1, 5);
        ToggleGroup directedGraphRadioButtonToggleGroup = new ToggleGroup();

        directedGraphRadioButton = new RadioButton("Directed");
        directedGraphRadioButton.setToggleGroup(directedGraphRadioButtonToggleGroup);
        directedGraphRadioButton.setSelected(true);
        directedGraphRadiobuttonPane.getChildren().add(directedGraphRadioButton);

        notDirectedGraphRadioButton = new RadioButton("Not directed");
        notDirectedGraphRadioButton.setToggleGroup(directedGraphRadioButtonToggleGroup);
        directedGraphRadiobuttonPane.getChildren().add(notDirectedGraphRadioButton);

        outputEdgesLabel = new Label("Number of output edges");
        labelFieldPane.add(outputEdgesLabel, 0, 6);

        GridPane outputEdgesFieldPane = new GridPane();
        outputEdgesFieldPane.setHgap(5);
        outputEdgesFieldPane.setVgap(5);
        labelFieldPane.add(outputEdgesFieldPane, 1, 6);

        Label minOutputEdgesLabel = new Label("Min");
        outputEdgesFieldPane.add(minOutputEdgesLabel, 0 , 0);

        minOutputEdgesField = new NumericTextField(NumericTextField.Type.PositiveInteger, "0");
        minOutputEdgesField.setMinValue(0);
        minOutputEdgesField.setMaxValue(4);
        outputEdgesFieldPane.add(minOutputEdgesField, 1 , 0);

        Label maxOutputEdgesLabel = new Label("Max");
        outputEdgesFieldPane.add(maxOutputEdgesLabel, 0 , 1);

        maxOutputEdgesField = new NumericTextField(NumericTextField.Type.PositiveInteger, "4");
        maxOutputEdgesField.setMinValue(0);
        maxOutputEdgesField.setMaxValue(4);
        outputEdgesFieldPane.add(maxOutputEdgesField, 1 , 1);

        inputEdgesLabel = new Label("Number of input edges");
        labelFieldPane.add(inputEdgesLabel, 0, 7);

        inputEdgesFieldPane = new GridPane();
        inputEdgesFieldPane.setHgap(5);
        inputEdgesFieldPane.setVgap(5);
        labelFieldPane.add(inputEdgesFieldPane, 1, 7);

        Label minInputEdgesLabel = new Label("Min");
        inputEdgesFieldPane.add(minInputEdgesLabel, 0 , 0);

        minInputEdgesField = new NumericTextField(NumericTextField.Type.PositiveInteger, "0");
        minInputEdgesField.setMinValue(0);
        minInputEdgesField.setMaxValue(4);
        inputEdgesFieldPane.add(minInputEdgesField, 1 , 0);

        Label maxInputEdgesLabel = new Label("Max");
        inputEdgesFieldPane.add(maxInputEdgesLabel, 0 , 1);

        maxInputEdgesField = new NumericTextField(NumericTextField.Type.PositiveInteger, "4");
        maxInputEdgesField.setMinValue(0);
        maxInputEdgesField.setMaxValue(4);
        inputEdgesFieldPane.add(maxInputEdgesField, 1 , 1);

        directedGraphRadioButtonToggleGroup.selectedToggleProperty().addListener(new DirectedGraphRadioButtonToggleGroupToggleChanged());

        Separator separator3 = new Separator(Orientation.HORIZONTAL);
        labelFieldPane.add(separator3, 0, 8,2,1);

        Label customSeedLabel = new Label("Custom RNG seed");
        labelFieldPane.add(customSeedLabel, 0, 9);

        customSeedField = new NumericTextField(NumericTextField.Type.Integer);
        labelFieldPane.add(customSeedField, 1, 9);

        VBox savingOptionsPane = new VBox();
        savingOptionsPane.setSpacing(10);
        BorderPane.setMargin(savingOptionsPane, new Insets(15, 0, 15, 0));
        basePane.setCenter(savingOptionsPane);

        saveToFileCheckBox = new CheckBox("Save to file");
        savingOptionsPane.getChildren().add(saveToFileCheckBox);

        loadCheckBox = new CheckBox("Load graph after generating");
        loadCheckBox.setSelected(true);
        savingOptionsPane.getChildren().add(loadCheckBox);

        Button generateButton = new Button("Generate");
        generateButton.setOnAction(new GenerateButtonClicked());
        BorderPane.setAlignment(generateButton, Pos.CENTER);
        BorderPane.setMargin(generateButton, new Insets(15, 0, 0, 0));
        basePane.setBottom(generateButton);


        this.setTitle("New");
        this.setResizable(false);
        this.setScene(scene);
    }

    //endregion



    //region PUBLIC METHODS

    public Graph showAndWaitForGraph()
    {
        super.showAndWait();

        if (loadCheckBox.isSelected())
        {
            return graph;
        }
        else
        {
            return null;
        }
    }

    //endregion



    //region EVENT HANDLERS

    private class GenerateButtonClicked implements EventHandler<ActionEvent>
    {
        @Override
        public void handle(ActionEvent event)
        {
            if (widthField.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Graph generating error");
                alert.setContentText("Number of columns (width) not specified");
                alert.show();
                return;
            }
            if (heightField.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Graph generating error");
                alert.setContentText("Number of rows (height) not specified");
                alert.show();
                return;
            }
            Dimensions2D size = new Dimensions2D(Integer.parseInt(widthField.getText()), Integer.parseInt(heightField.getText()));

            if (minWeightField.getText().isEmpty())
            {
                minWeightField.setText("0");
            }
            if (maxWeightField.getText().isEmpty())
            {
                maxWeightField.setText("1");
            }
            double minWeight = Double.parseDouble(minWeightField.getText());
            double maxWeight = Double.parseDouble(maxWeightField.getText());
            if (maxWeight < minWeight)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Graph generating error");
                alert.setContentText("Minimum weight of edge must be greater or equal than maximum weight of edge");
                alert.show();
                return;
            }

            Random random = new Random();
            if (!customSeedField.getText().isEmpty())
            {
                random.setSeed(Integer.parseInt(customSeedField.getText()));
            }

            if (minOutputEdgesField.getText().isEmpty())
            {
                minOutputEdgesField.setText("0");
            }
            if (maxOutputEdgesField.getText().isEmpty())
            {
                maxOutputEdgesField.setText("4");
            }
            int minOutputEdges = Integer.parseInt(minOutputEdgesField.getText());
            int maxOutputEdges = Integer.parseInt(maxOutputEdgesField.getText());
            if (maxOutputEdges < minOutputEdges)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Graph generating error");
                String text = directedGraphRadioButton.isSelected() ? "output edges" : "neighbours";
                alert.setContentText(String.format("Minimum number of %s must be greater or equal than maximum number of %s", text, text));
                alert.show();
                return;
            }

            if (minInputEdgesField.getText().isEmpty())
            {
                minInputEdgesField.setText("0");
            }
            if (maxInputEdgesField.getText().isEmpty())
            {
                maxInputEdgesField.setText("4");
            }
            if (directedGraphRadioButton.isSelected())
            {
                int minInputEdges = Integer.parseInt(minInputEdgesField.getText());
                int maxInputEdges = Integer.parseInt(maxInputEdgesField.getText());
                if (maxInputEdges < minInputEdges)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Graph generating error");
                    alert.setContentText("Minimum number of input edges must be greater or equal than maximum number of input edges");
                    alert.show();
                    return;
                }

                graph = GraphUtils.generate(size, random, new Range(minWeight, maxWeight), new Range(minOutputEdges, maxOutputEdges), new Range(minInputEdges, maxInputEdges));
            }
            else
            {
                graph = GraphUtils.generate(size, random, new Range(minWeight, maxWeight), new Range(minOutputEdges, maxOutputEdges));
            }

            if (saveToFileCheckBox.isSelected())
            {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open graph file");
                File file = fileChooser.showSaveDialog(NewGraphStage.this.getOwner());
                if (file != null)
                {
                    try
                    {
                        GraphUtils.write(graph, file);
                    }
                    catch (IOException e)
                    {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("ERROR");
                        alert.setHeaderText("Graph generating error");
                        alert.setContentText("File permission error");
                        alert.show();
                        return;
                    }
                }
            }

            NewGraphStage.this.close();
        }
    }

    private class DirectedGraphRadioButtonToggleGroupToggleChanged implements ChangeListener<Toggle>
    {
        @Override
        public void changed(ObservableValue<? extends Toggle> observableValue, Toggle oldValue, Toggle newValue)
        {
            if (newValue == directedGraphRadioButton)
            {
                outputEdgesLabel.setText("Number of output edges");
                inputEdgesLabel.setText("Number of input edges");
                inputEdgesLabel.setDisable(false);
                inputEdgesFieldPane.setDisable(false);
            }
            else if (newValue == notDirectedGraphRadioButton)
            {
                outputEdgesLabel.setText("Number of neighbours");
                inputEdgesLabel.setText("Number of neighbours");
                inputEdgesLabel.setDisable(true);
                inputEdgesFieldPane.setDisable(true);
            }
        }
    }


    //endregion
}
