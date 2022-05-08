package App;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        /* Code to test if drawing a graph works properly
        Graph graph = ReadUtils.readGraph("JavaGridGraph/src/App/test.txt");
        primaryStage.setTitle("JavaGridGraph");


        BorderPane border = new BorderPane();
        Scene scene = new Scene(border,1280,700);
        GraphDrawer graphDrawer = new GraphDrawer(graph, scene.getWidth(), scene.getHeight(), 30, 0, 1);
        AnchorPane anchorPane = graphDrawer.drawGraph();


        border.setCenter(anchorPane);
        BorderPane.setMargin(anchorPane, new Insets(10));
        primaryStage.setScene(scene);
        */


//        Label label = new Label("Hello World");
//        label.setPadding(new Insets(10));

        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
