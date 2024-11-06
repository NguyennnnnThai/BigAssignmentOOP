package org.example.bigassignment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LibrarianDAO {
    public boolean validateLibrarianLogin(String username, String password) {
        String query = "SELECT * FROM librarian WHERE username = ? AND password = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            return resultSet.next();
        } catch (SQLException e) {
            System.out.println("Lỗi xác thực thủ thư: " + e.getMessage());
            return false;
        }
    }

    public void addLibrarian(String username, String password, String fullName, String email) {
        String query = "INSERT INTO librarian (username, password, full_name, email, hire_date) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, username);
            statement.setString(2, password);
            statement.setString(3, fullName);
            statement.setString(4, email);
            statement.executeUpdate();

            System.out.println("Thêm thủ thư thành công!");
        } catch (SQLException e) {
            System.out.println("Lỗi thêm thủ thư: " + e.getMessage());
        }
    }
}