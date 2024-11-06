module org.example.bigassignment {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;
    requires java.sql;

    opens org.example.bigassignment to javafx.fxml;
    exports org.example.bigassignment;
}