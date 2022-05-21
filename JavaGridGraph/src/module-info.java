module JavaGridGraph {

    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;

    opens App;
    opens App.Controls;
    opens App.Stages;
    opens Core;
    opens Core.GraphAlgorithms;
    opens Core.Helpers;
    opens Core.Utils;
}