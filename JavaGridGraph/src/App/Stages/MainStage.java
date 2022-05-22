package App.Stages;

import App.Controls.GraphDrawer;
import Core.Graph;
import Core.GraphAlgorithms.BFS;
import Core.GraphAlgorithms.GraphUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class MainStage extends Stage
{
    //region PROPERTIES

    private final GraphDrawer _graphDrawer;
    private final Label _connectivityLabel;

    //endregion



    //region CONSTRUCTORS

    public MainStage()
    {
        BorderPane basePane = new BorderPane();
        Scene scene = new Scene(basePane, 500, 500);

        // Initialize view
        BorderPane appbarPane = new BorderPane();
        appbarPane.setPadding(new Insets(10));
        basePane.setTop(appbarPane);

        _connectivityLabel = new Label("Connectivity: Unknown (Graph not loaded)");
        _connectivityLabel.setAlignment(Pos.CENTER_LEFT);
        appbarPane.setLeft(_connectivityLabel);

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
        newGraphButton.setOnAction(new newGraphButtonClicked());
        buttonsBox.getChildren().add(newGraphButton);

        _graphDrawer = new GraphDrawer();
        _graphDrawer.setMargin(new Insets(10, 10, 55, 10));
        basePane.setCenter(_graphDrawer);

        this.setTitle("JavaGridGraph");
        this.setScene(scene);
    }

    //endregion



    //region PUBLIC METHODS

    public void setGraph(Graph graph)
    {
        if (graph != null)
        {
            _graphDrawer.setGraph(graph);
            BFS bfs = new BFS(graph);
            if (bfs.isGraphConnected())
            {
                _connectivityLabel.setText("Connectivity: Connected");
            }
            else
            {
                _connectivityLabel.setText("Connectivity: Disconnected");
            }
        }
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
            File file = fileChooser.showOpenDialog(MainStage.this.getOwner());
            if (file != null)
            {
                try
                {
                    setGraph(GraphUtils.read(file));
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

    private class newGraphButtonClicked implements EventHandler<ActionEvent>
    {
        @Override
        public void handle(ActionEvent event)
        {
            NewGraphStage newGraphStage = new NewGraphStage();
            setGraph(newGraphStage.showAndWaitForGraph());
        }
    }

    //endregion
}
