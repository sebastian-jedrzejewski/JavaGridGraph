package App;

import App.Stages.MainStage;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage)
    {
        primaryStage = new MainStage();
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
