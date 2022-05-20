package App;

import App.Views.Main;
import javafx.application.Application;
import javafx.stage.Stage;

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
