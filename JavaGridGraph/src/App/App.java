package App;

import App.Controls.GraphDrawer;
import App.Views.Main;
import Core.Graph;
import Core.ReadUtils;
import javafx.application.Application;
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
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setTitle("JavaGridGraph");
        primaryStage.setScene(new Main());
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
