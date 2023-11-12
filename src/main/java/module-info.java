module com.connect4.connect4usingjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.almasb.fxgl.all;

    opens com.connect4.connect4javafx to javafx.fxml;
    exports com.connect4.connect4javafx;
}