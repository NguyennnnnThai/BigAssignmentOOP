package org.example.bigassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML
    private TextField usernameTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private TextField fullNameTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private Label registerMessageLabel;

    @FXML
    public void registerButtonAction(ActionEvent event) {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String fullName = fullNameTextField.getText();
        String email = emailTextField.getText();

        if (!username.isBlank() && !password.isBlank() && !fullName.isBlank() && !email.isBlank()) {
            LibrarianDAO userDAO = new LibrarianDAO();
            userDAO.addLibrarian(username, password, fullName, email);
            registerMessageLabel.setText("Đăng ký thành công!");
        } else {
            registerMessageLabel.setText("Vui lòng điền đầy đủ thông tin.");
        }
    }

    @FXML
    private void backToLoginScene(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/bigassignment/LoginScene.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((    Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Không thể chuyển về màn hình đăng nhập.");
        }
    }
}
