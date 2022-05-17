package App;

import GUI.GraphDrawer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        BorderPane mainPane = new BorderPane();
        Scene scene = new Scene(mainPane, 1000, 1000);
        primaryStage.setTitle("JavaGridGraph");
        primaryStage.setScene(scene);

        BorderPane appbarPane = new BorderPane();
        appbarPane.setPadding(new Insets(10));
        mainPane.setTop(appbarPane);

        Label connectivityLabel = new Label("Connectivity: Unknown (Graph not loaded)");
        connectivityLabel.setAlignment(Pos.CENTER_LEFT);
        appbarPane.setLeft(connectivityLabel);

        HBox buttonsBox = new HBox(8);
        buttonsBox.setAlignment(Pos.CENTER_RIGHT);
        appbarPane.setRight(buttonsBox);

        Button resetGraphDrawerButton = new Button("Reset");
        buttonsBox.getChildren().add(resetGraphDrawerButton);

        Separator buttonsSeparator = new Separator(Orientation.VERTICAL);
        buttonsBox.getChildren().add(buttonsSeparator);

        Button loadGraphButton = new Button("Load");

        buttonsBox.getChildren().add(loadGraphButton);

        Button newGraphButton = new Button("New");
        buttonsBox.getChildren().add(newGraphButton);

        GraphDrawer graphDrawer = new GraphDrawer(new Insets(10, 10, 55, 10));

        mainPane.setCenter(graphDrawer);

        primaryStage.show();

        loadGraphButton.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open graph file");
                File file = fileChooser.showOpenDialog(primaryStage);
                try
                {
                    Graph graph = ReadUtils.readGraph(file);
                    graphDrawer.setGraph(graph);
                }
                catch (IOException e)
                {
                    System.out.println("niedziała");
                }
            }
        });
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
