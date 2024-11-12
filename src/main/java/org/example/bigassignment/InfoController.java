package org.example.bigassignment;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class InfoController implements Initializable {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField fullNameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private Button backButton;
    @FXML
    private Button saveButton;

    private int librarianId = 1; // ID của librarian có thể thay đổi tùy theo phiên đăng nhập

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadLibrarianInfo(librarianId);
    }

    // Phương thức lấy thông tin librarian từ cơ sở dữ liệu
    private void loadLibrarianInfo(int librarianId) {
        String query = "SELECT username, password, full_name, email, phone_number FROM librarian WHERE librarian_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, librarianId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Hiển thị thông tin lên giao diện
                usernameField.setText(rs.getString("username"));
                passwordField.setText(rs.getString("password"));
                fullNameField.setText(rs.getString("full_name"));
                emailField.setText(rs.getString("email"));
                phoneNumberField.setText(rs.getString("phone_number"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Phương thức cập nhật thông tin librarian trong cơ sở dữ liệu
    @FXML
    private void handleSaveChanges() {
        String newUsername = usernameField.getText();
        String newPassword = passwordField.getText();
        String newFullName = fullNameField.getText();
        String newEmail = emailField.getText();
        String newPhoneNumber = phoneNumberField.getText();

        String updateQuery = "UPDATE librarian SET username = ?, password = ?, full_name = ?, email = ?, phone_number = ? WHERE librarian_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {

            pstmt.setString(1, newUsername);
            pstmt.setString(2, newPassword);
            pstmt.setString(3, newFullName);
            pstmt.setString(4, newEmail);
            pstmt.setString(5, newPhoneNumber);
            pstmt.setInt(6, librarianId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Thông tin đã được cập nhật thành công!");
            } else {
                System.out.println("Cập nhật thông tin thất bại.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void backToInterface() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InterfaceScene.fxml"));
            Parent interfaceRoot = loader.load();

            Stage currentStage = (Stage) backButton.getScene().getWindow();

            Scene interfaceScene = new Scene(interfaceRoot);
            currentStage.setScene(interfaceScene);
            currentStage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
