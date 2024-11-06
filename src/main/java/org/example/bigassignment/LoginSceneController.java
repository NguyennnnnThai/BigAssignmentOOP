package org.example.bigassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;


import java.io.IOException;
import java.util.ResourceBundle;


public class LoginSceneController implements Initializable {

    @FXML
    TextField usernameTextField;

    @FXML
    PasswordField passwordTextField;

    @FXML
    private Button exitButton;

    @FXML
    private Label loginMessageLabel;

    @FXML
    private TextField passwordVisibleTextField;

    private boolean isPasswordVisible = false;

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void loginButtonAction(ActionEvent event) {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        if (!username.isBlank() && !password.isBlank()) {
            LibrarianDAO userDAO = new LibrarianDAO(); // Hoặc LibrarianDAO nếu dùng cho thủ thư

            boolean isValidUser = userDAO.validateLibrarianLogin(username, password);
            if (isValidUser) {
                loginMessageLabel.setText("Đăng nhập thành công!");
                loadInterfaceScene(event, username);
            } else {
                loginMessageLabel.setText("Tên đăng nhập hoặc mật khẩu không đúng.");
            }
        } else {
            loginMessageLabel.setText("Vui lòng nhập tên đăng nhập và mật khẩu.");
        }
    }

    @FXML
    public void openRegisterScene(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Register.fxml"));
            root = loader.load();

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            loginMessageLabel.setText("Không thể chuyển sang màn hình đăng ký.");
        }
    }

    @FXML
    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Chuyển về ẩn mật khẩu
            passwordTextField.setText(passwordVisibleTextField.getText());
            passwordTextField.setVisible(true);
            passwordVisibleTextField.setVisible(false);
            isPasswordVisible = false;
        } else {
            // Chuyển sang hiển thị mật khẩu
            passwordVisibleTextField.setText(passwordTextField.getText());
            passwordVisibleTextField.setVisible(true);
            passwordTextField.setVisible(false);
            isPasswordVisible = true;
        }
    }


    private void loadInterfaceScene(ActionEvent event, String username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InterfaceScene.fxml"));
            root = loader.load();

            InterfaceSceneController interfaceSceneController = loader.getController();
            interfaceSceneController.displayName(username);

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            loginMessageLabel.setText("Không thể chuyển sang màn hình chính.");
        }
    }

    public void exit(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("cbi thoat");
        alert.setContentText("muon thoat khong?");

        if (alert.showAndWait().get() == ButtonType.OK) {

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            System.out.println("Da dang xuat");
            stage.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
