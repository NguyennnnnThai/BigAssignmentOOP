module org.example.bigassignment {
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires de.jensd.fx.glyphs.fontawesome;
    requires com.jfoenix;
    requires javafx.controls;
    requires javafx.media;

    opens org.example.bigassignment to javafx.fxml;
    exports org.example.bigassignment;
}