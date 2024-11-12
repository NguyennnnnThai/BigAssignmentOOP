package org.example.bigassignment;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class InterfaceSceneController implements Initializable {


    @FXML
    private ChoiceBox<String> myChoiceBox;

    private String[] options = {"Đăng xuất", "Cài đặt tài khoản", "Chế độ ban đêm"};

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        myChoiceBox.getItems().addAll(options);
        myChoiceBox.setOnAction(event -> handleChoiceBoxSelection());
    }

    private void handleChoiceBoxSelection() {
        String selectedOption = myChoiceBox.getValue();

        if ("Đăng xuất".equals(selectedOption)) {
            logout();
        } else if ("Chế độ ban đêm".equals(selectedOption)) {
            toggleNightMode();
        } else if ("Cài đặt tài khoản".equals(selectedOption)) {
            openAccountSettings();
        }
    }

    private void logout() {
        try {
            // Tải FXML của màn hình đăng nhập
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginScene.fxml"));
            Parent loginRoot = loader.load();

            // Lấy Stage hiện tại từ một control trên giao diện hiện tại
            Stage currentStage = (Stage) myChoiceBox.getScene().getWindow();

            // Thiết lập lại cảnh với LoginScene
            Scene loginScene = new Scene(loginRoot);
            currentStage.setScene(loginScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Xử lý ngoại lệ, có thể hiển thị thông báo lỗi cho người dùng nếu cần
        }
    }

    private void toggleNightMode() {
        // Thêm mã để bật/tắt chế độ ban đêm
    }

    private void openAccountSettings() {
        try {
            // Tải FXML của màn hình cài đặt tài khoản
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InfoScene.fxml"));
            Parent accountSettingsRoot = loader.load();

            // Lấy Stage hiện tại từ một control trên giao diện hiện tại
            Stage currentStage = (Stage) myChoiceBox.getScene().getWindow();

            // Thiết lập lại cảnh với màn hình cài đặt tài khoản
            Scene accountSettingsScene = new Scene(accountSettingsRoot);
            currentStage.setScene(accountSettingsScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Xử lý lỗi nếu có (có thể hiển thị thông báo lỗi cho người dùng)
        }
    }
}
