package org.example.bigassignment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataService {

    // Phương thức để lấy tổng số sách
    public int getTotalBooks() {
        String query = "SELECT COUNT(id) FROM book";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Phương thức để lấy tổng số sinh viên
    public int getTotalUsers() {
        String query = "SELECT COUNT(*) FROM borrower";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Phương thức để lấy số sách đang được mượn (ON_LOAN)
    public int getBooksOnLoan() {
        String query = "SELECT COUNT(book_id) FROM loan WHERE status = 'ON_LOAN'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Phương thức để lấy số sách có sẵn (available)
    public int getAvailableBooks() {
        int totalBooks = getTotalBooks();
        int booksOnLoan = getBooksOnLoan();
        return totalBooks - booksOnLoan;
    }
}
