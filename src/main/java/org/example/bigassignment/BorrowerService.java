package org.example.bigassignment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowerService {

    // Hàm lấy tất cả người mượn
    public static List<Borrower> getAllBorrowers() {
        List<Borrower> borrowers = new ArrayList<>();
        String sql = "SELECT * FROM borrower"; // Đúng với tên bảng trong cơ sở dữ liệu

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                borrowers.add(new Borrower(
                        rs.getInt("id"), // Lấy ID từ cơ sở dữ liệu
                        rs.getString("full_name"),
                        rs.getInt("age"),
                        rs.getString("phone_number")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return borrowers;
    }

    // Hàm thêm một người mượn mới
    public static void addBorrower(Borrower borrower) {
        String sql = "INSERT INTO borrower (full_name, age, phone_number) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, borrower.getFullName());
            stmt.setInt(2, borrower.getAge());
            stmt.setString(3, borrower.getPhoneNumber());

            stmt.executeUpdate();

            // Lấy ID tự động từ cơ sở dữ liệu và gán cho Borrower
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                borrower.setId(keys.getInt(1)); // Cập nhật ID cho Borrower
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Hàm cập nhật thông tin người mượn
    public static void updateBorrower(Borrower borrower) {
        String sql = "UPDATE borrower SET full_name = ?, age = ?, phone_number = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, borrower.getFullName());
            stmt.setInt(2, borrower.getAge());
            stmt.setString(3, borrower.getPhoneNumber());
            stmt.setInt(4, borrower.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Hàm xóa người mượn khỏi cơ sở dữ liệu
    public static void deleteBorrower(int id) throws SQLException {
        String query = "DELETE FROM borrower WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Cập nhật lại giá trị ID người mượn
    public static void reorderBorrowerIds() throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // Tạo biến user-defined variable @new_id
            stmt.execute("SET @new_id = 0");

            // Cập nhật ID người mượn để liên tục
            stmt.execute("UPDATE borrower SET id = (@new_id := @new_id + 1)");

            // Đặt lại AUTO_INCREMENT cho bảng borrower
            stmt.execute("ALTER TABLE borrower AUTO_INCREMENT = 1");
        }
    }
}
