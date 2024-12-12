package org.example.bigassignment;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Image icon = new Image(HelloApplication.class.getResource("/org/example/bigassignment/Image/anh_sach.jpg").toString());
        stage.getIcons().add(icon);
        stage.setTitle("Library Management");
        //  stage.setResizable(false); // hàm giúp không cho phép thay đổi kích thước cửa sổ
        //  stage.setFullScreen(true);

        try {
            Parent root = FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
        }
    }

    public static void main(String[] args) {
        launch();
    }
}