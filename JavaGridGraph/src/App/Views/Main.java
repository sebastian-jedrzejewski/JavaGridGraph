package App.Views;

import App.Controls.GraphDrawer;
import Core.Graph;
import Core.ReadUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;

public class Main extends Scene
{
    //region PROPERTIES

    private final GraphDrawer _graphDrawer;

    //endregion



    //region CONSTRUCTORS

    public Main()
    {
        this(new BorderPane());
    }

    private Main(BorderPane basePane)
    {
        super(basePane, 500, 500);

        // Initialize view
        BorderPane appbarPane = new BorderPane();
        appbarPane.setPadding(new Insets(10));
        basePane.setTop(appbarPane);

        Label connectivityLabel = new Label("Connectivity: Unknown (Graph not loaded)");
        connectivityLabel.setAlignment(Pos.CENTER_LEFT);
        appbarPane.setLeft(connectivityLabel);

        HBox buttonsBox = new HBox(8);
        buttonsBox.setAlignment(Pos.CENTER_RIGHT);
        appbarPane.setRight(buttonsBox);

        Button resetGraphDrawerButton = new Button("Reset");
        resetGraphDrawerButton.setOnAction(new resetGraphDrawerButtonClicked());
        buttonsBox.getChildren().add(resetGraphDrawerButton);

        Separator buttonsSeparator = new Separator(Orientation.VERTICAL);
        buttonsBox.getChildren().add(buttonsSeparator);

        Button loadGraphButton = new Button("Load");
        loadGraphButton.setOnAction(new loadGraphButtonClicked());
        buttonsBox.getChildren().add(loadGraphButton);

        Button newGraphButton = new Button("New");
        buttonsBox.getChildren().add(newGraphButton);

        _graphDrawer = new GraphDrawer();
        _graphDrawer.setMargin(new Insets(10, 10, 55, 10));
        basePane.setCenter(_graphDrawer);
    }

    //endregion



    //region EVENT HANDLERS

    private class loadGraphButtonClicked implements EventHandler<ActionEvent>
    {
        @Override
        public void handle(ActionEvent event)
        {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open graph file");
            File file = fileChooser.showOpenDialog(Main.this.getWindow());
            if (file != null)
            {
                try
                {
                    Graph graph = ReadUtils.readGraph(file);
                    _graphDrawer.setGraph(graph);
                }
                catch (IOException e)
                {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR");
                    alert.setHeaderText("Graph loading error");
                    alert.setContentText("Wrong data format");
                    alert.show();
                }
            }
        }
    }

    private class resetGraphDrawerButtonClicked implements EventHandler<ActionEvent>
    {
        @Override
        public void handle(ActionEvent event)
        {
            if (_graphDrawer.getGraph() != null)
            {
                _graphDrawer.reset();
            }
        }
    }

    //endregion
}
