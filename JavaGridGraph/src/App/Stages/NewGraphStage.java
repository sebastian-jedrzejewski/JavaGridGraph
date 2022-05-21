package App.Stages;

import App.Controls.NumericTextField;
import Core.Graph;
import Core.Helpers.Range;
import Core.Utils.GraphUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.SetChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class NewGraphStage extends Stage
{
    //region PROPERTIES

    private Graph _graph;

    private final NumericTextField _widthField;
    private final NumericTextField _heightField;
    private final CheckBox _saveToFileCheckBox;
    private final NumericTextField _minWeightField;
    private final NumericTextField _maxWeightField;
    private final RadioButton _directedGraphRadioButton;
    private final RadioButton _notDirectedGraphRadioButton;
    private final Label _outputEdgesLabel;
    private final NumericTextField _minOutputEdgesField;
    private final NumericTextField _maxOutputEdgesField;
    private final Label _inputEdgesLabel;
    private final GridPane _inputEdgesFieldPane;
    private final NumericTextField _minInputEdgesField;
    private final NumericTextField _maxInputEdgesField;
    private final NumericTextField _customSeedField;
    private final CheckBox _loadCheckBox;

    //endregion



    //region CONSTRUCTORS

    public NewGraphStage()
    {
        _graph = null;

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

        _widthField = new NumericTextField(NumericTextField.Type.PositiveInteger);
        _widthField.setMinValue(1);
        labelFieldPane.add(_widthField, 1, 0);

        Label heightLabel = new Label("Number of rows (height)");
        labelFieldPane.add(heightLabel, 0, 1);

        _heightField = new NumericTextField(NumericTextField.Type.PositiveInteger);
        _heightField.setMinValue(0);
        labelFieldPane.add(_heightField, 1, 1);

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

        _minWeightField = new NumericTextField(NumericTextField.Type.PositiveDouble, "0");
        _minWeightField.setMinValue(0);
        weightFieldPane.add(_minWeightField, 1 , 0);

        Label maxWeightLabel = new Label("Max");
        weightFieldPane.add(maxWeightLabel, 0 , 1);

        _maxWeightField = new NumericTextField(NumericTextField.Type.PositiveDouble, "1");
        _maxWeightField.setMinValue(0);
        weightFieldPane.add(_maxWeightField, 1 , 1);

        Separator separator2 = new Separator(Orientation.HORIZONTAL);
        labelFieldPane.add(separator2, 0, 4,2,1);

        Label directedGraphLabel = new Label("Graph type");
        labelFieldPane.add(directedGraphLabel, 0, 5);

        VBox directedGraphRadiobuttonPane = new VBox();
        directedGraphRadiobuttonPane.setSpacing(10);
        labelFieldPane.add(directedGraphRadiobuttonPane, 1, 5);
        ToggleGroup directedGraphRadioButtonToggleGroup = new ToggleGroup();

        _directedGraphRadioButton = new RadioButton("Directed");
        _directedGraphRadioButton.setToggleGroup(directedGraphRadioButtonToggleGroup);
        _directedGraphRadioButton.setSelected(true);
        directedGraphRadiobuttonPane.getChildren().add(_directedGraphRadioButton);

        _notDirectedGraphRadioButton = new RadioButton("Not directed");
        _notDirectedGraphRadioButton.setToggleGroup(directedGraphRadioButtonToggleGroup);
        directedGraphRadiobuttonPane.getChildren().add(_notDirectedGraphRadioButton);

        _outputEdgesLabel = new Label("Number of output edges");
        labelFieldPane.add(_outputEdgesLabel, 0, 6);

        GridPane outputEdgesFieldPane = new GridPane();
        outputEdgesFieldPane.setHgap(5);
        outputEdgesFieldPane.setVgap(5);
        labelFieldPane.add(outputEdgesFieldPane, 1, 6);

        Label minOutputEdgesLabel = new Label("Min");
        outputEdgesFieldPane.add(minOutputEdgesLabel, 0 , 0);

        _minOutputEdgesField = new NumericTextField(NumericTextField.Type.PositiveInteger, "0");
        _minOutputEdgesField.setMinValue(0);
        _minOutputEdgesField.setMaxValue(4);
        outputEdgesFieldPane.add(_minOutputEdgesField, 1 , 0);

        Label maxOutputEdgesLabel = new Label("Max");
        outputEdgesFieldPane.add(maxOutputEdgesLabel, 0 , 1);

        _maxOutputEdgesField = new NumericTextField(NumericTextField.Type.PositiveInteger, "4");
        _maxOutputEdgesField.setMinValue(0);
        _maxOutputEdgesField.setMaxValue(4);
        outputEdgesFieldPane.add(_maxOutputEdgesField, 1 , 1);

        _inputEdgesLabel = new Label("Number of input edges");
        labelFieldPane.add(_inputEdgesLabel, 0, 7);

        _inputEdgesFieldPane = new GridPane();
        _inputEdgesFieldPane.setHgap(5);
        _inputEdgesFieldPane.setVgap(5);
        labelFieldPane.add(_inputEdgesFieldPane, 1, 7);

        Label minInputEdgesLabel = new Label("Min");
        _inputEdgesFieldPane.add(minInputEdgesLabel, 0 , 0);

        _minInputEdgesField = new NumericTextField(NumericTextField.Type.PositiveInteger, "0");
        _minInputEdgesField.setMinValue(0);
        _minInputEdgesField.setMaxValue(4);
        _inputEdgesFieldPane.add(_minInputEdgesField, 1 , 0);

        Label maxInputEdgesLabel = new Label("Max");
        _inputEdgesFieldPane.add(maxInputEdgesLabel, 0 , 1);

        _maxInputEdgesField = new NumericTextField(NumericTextField.Type.PositiveInteger, "4");
        _maxInputEdgesField.setMinValue(0);
        _maxInputEdgesField.setMaxValue(4);
        _inputEdgesFieldPane.add(_maxInputEdgesField, 1 , 1);

        directedGraphRadioButtonToggleGroup.selectedToggleProperty().addListener(new directedGraphRadioButtonToggleGroupToggleChanged());

        Separator separator3 = new Separator(Orientation.HORIZONTAL);
        labelFieldPane.add(separator3, 0, 8,2,1);

        Label customSeedLabel = new Label("Custom RNG seed");
        labelFieldPane.add(customSeedLabel, 0, 9);

        _customSeedField = new NumericTextField(NumericTextField.Type.Integer);
        labelFieldPane.add(_customSeedField, 1, 9);

        VBox savingOptionsPane = new VBox();
        savingOptionsPane.setSpacing(10);
        BorderPane.setMargin(savingOptionsPane, new Insets(15, 0, 15, 0));
        basePane.setCenter(savingOptionsPane);

        _saveToFileCheckBox = new CheckBox("Save to file");
        savingOptionsPane.getChildren().add(_saveToFileCheckBox);

        _loadCheckBox = new CheckBox("Load graph after generating");
        savingOptionsPane.getChildren().add(_loadCheckBox);

        Button generateButton = new Button("Generate");
        generateButton.setOnAction(new generateButtonClicked());
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

        if (_loadCheckBox.isSelected())
        {
            return _graph;
        }
        else
        {
            return null;
        }
    }

    //endregion



    //region EVENT HANDLERS

    private class generateButtonClicked implements EventHandler<ActionEvent>
    {
        @Override
        public void handle(ActionEvent event)
        {
            if (_widthField.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Graph generating error");
                alert.setContentText("Number of columns (width) not specified");
                alert.show();
                return;
            }
            int width = Integer.parseInt(_widthField.getText());
            if (_heightField.getText().isEmpty())
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Graph generating error");
                alert.setContentText("Number of rows (height) not specified");
                alert.show();
                return;
            }
            int height = Integer.parseInt(_heightField.getText());

            if (_minWeightField.getText().isEmpty())
            {
                _minWeightField.setText("0");
            }
            if (_maxWeightField.getText().isEmpty())
            {
                _maxWeightField.setText("1");
            }
            double minWeight = Double.parseDouble(_minWeightField.getText());
            double maxWeight = Double.parseDouble(_maxWeightField.getText());
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
            if (!_customSeedField.getText().isEmpty())
            {
                random.setSeed(Integer.parseInt(_customSeedField.getText()));
            }

            if (_minOutputEdgesField.getText().isEmpty())
            {
                _minOutputEdgesField.setText("0");
            }
            if (_maxOutputEdgesField.getText().isEmpty())
            {
                _maxOutputEdgesField.setText("4");
            }
            int minOutputEdges = Integer.parseInt(_minOutputEdgesField.getText());
            int maxOutputEdges = Integer.parseInt(_maxOutputEdgesField.getText());
            if (maxOutputEdges < minOutputEdges)
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR");
                alert.setHeaderText("Graph generating error");
                String text = _directedGraphRadioButton.isSelected() ? "output edges" : "edges";
                alert.setContentText(String.format("Minimum number of %s must be greater or equal than maximum number of %s", text, text));
                alert.show();
                return;
            }

            if (_minInputEdgesField.getText().isEmpty())
            {
                _minInputEdgesField.setText("0");
            }
            if (_maxInputEdgesField.getText().isEmpty())
            {
                _maxInputEdgesField.setText("4");
            }
            if (_directedGraphRadioButton.isSelected())
            {
                int minInputEdges = Integer.parseInt(_minInputEdgesField.getText());
                int maxInputEdges = Integer.parseInt(_maxInputEdgesField.getText());
                if (maxOutputEdges < minOutputEdges)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Graph generating error");
                    alert.setContentText("Minimum number of input edges must be greater or equal than maximum number of input edges");
                    alert.show();
                    return;
                }

                _graph = GraphUtils.generate(width, height, random, new Range<>(minWeight, maxWeight), new Range<>(minOutputEdges, maxOutputEdges), new Range<>(minInputEdges, maxInputEdges));
            }
            else
            {
                _graph = GraphUtils.generate(width, height, random, new Range<>(minWeight, maxWeight), new Range<>(minOutputEdges, maxOutputEdges));
            }

            if (_saveToFileCheckBox.isSelected())
            {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open graph file");
                File file = fileChooser.showSaveDialog(NewGraphStage.this.getOwner());
                if (file != null)
                {
                    try
                    {
                        GraphUtils.write(_graph, file);
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

    private class directedGraphRadioButtonToggleGroupToggleChanged implements ChangeListener<Toggle>
    {
        @Override
        public void changed(ObservableValue<? extends Toggle> observableValue, Toggle oldValue, Toggle newValue)
        {
            if (newValue == _directedGraphRadioButton)
            {
                _outputEdgesLabel.setText("Number of output edges");
                _inputEdgesLabel.setText("Number of input edges");
                _inputEdgesLabel.setDisable(false);
                _inputEdgesFieldPane.setDisable(false);
            }
            else if (newValue == _notDirectedGraphRadioButton)
            {
                _outputEdgesLabel.setText("Number of edges");
                _inputEdgesLabel.setText("Number of edges");
                _inputEdgesLabel.setDisable(true);
                _inputEdgesFieldPane.setDisable(true);
            }
        }
    }


    //endregion
}
