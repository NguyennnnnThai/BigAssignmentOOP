package org.example.bigassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

import java.io.IOException;

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

    // Hàm chuyển sang mục Quản lý mượn trả sách
    public void switchToQuanLyMuonTraSach (ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("LoanManagement.fxml"));
        Scene scene = new Scene(root);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    // Hàm đăng xuất
    public void logOut(ActionEvent event) throws IOException {
        // Hiển thị hộp thoại xác nhận đăng xuất
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Xác nhận đăng xuất");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Bạn có chắc chắn muốn đăng xuất không?");

        // Nếu người dùng chọn "OK", thực hiện đăng xuất
        if (confirmationAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            root = FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
            Scene scene = new Scene(root);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

}
