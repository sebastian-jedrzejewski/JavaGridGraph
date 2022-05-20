package App.Views;

import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

public class NewGraph extends Scene
{
    //region PROPERTIES



    //endregion



    //region CONSTRUCTORS

    public NewGraph()
    {
        this(new BorderPane());
    }

    private NewGraph(BorderPane basePane)
    {
        super(basePane, 500, 500);
    }

    //endregion
}
