package org.example.bigassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Optional;

public class HomeController {
    Stage stage;
    Scene scene;
    Parent root;

    // Hàm chuyển sang mục Quản lý sách
    public void switchToQuanLySach (ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("BookManagement.fxml"));
        Scene scene = new Scene(root);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    // Hàm chuyển sang mục Quản lý người mượn
    public void switchToQuanLyNguoiMuon (ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("BorrowerManagement.fxml"));
        Scene scene = new Scene(root);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
