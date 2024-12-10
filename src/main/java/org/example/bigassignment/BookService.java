package org.example.bigassignment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookService {

    public static List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM book"; // Đúng với tên bảng trong cơ sở dữ liệu

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                books.add(new Book(
                        rs.getInt("id"), // Đúng với tên cột trong cơ sở dữ liệu
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("category"),
                        rs.getInt("available_quantity"),
                        rs.getInt("total_quantity")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    // Hàm thêm 1 cuốn sách
    public static void addBook(Book book) {
        String sql = "INSERT INTO book (title, author, category, available_quantity, total_quantity) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getCategory());
            stmt.setInt(4, book.getAvailableQuantity());
            stmt.setInt(5, book.getTotalQuantity());

            stmt.executeUpdate();

            // Lấy ID tự động từ cơ sở dữ liệu và gán cho Book
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                book.setId(keys.getInt(1)); // Không còn gây lỗi null
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Hàm cập nhật 1 cuốn sách
    public static void updateBook(Book book) {
        String sql = "UPDATE book SET title = ?, author = ?, category = ?, available_quantity = ?, total_quantity = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getCategory());
            stmt.setInt(4, book.getAvailableQuantity());
            stmt.setInt(5, book.getTotalQuantity());
            stmt.setInt(6, book.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Xóa cuốn sách khỏi DB
    public static void deleteBook(int id) throws SQLException {
        String query = "DELETE FROM book WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    // Cập nhật laị giá trị bảng
    public static void reorderBookIds() throws SQLException {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {

            // Tạo biến user-defined variable @new_id
            statement.execute("SET @new_id = 0");

            // Cập nhật ID để liên tục
            statement.execute("UPDATE book SET id = (@new_id := @new_id + 1)");

            // Đặt lại AUTO_INCREMENT cho đúng giá trị lớn nhất hiện tại
            statement.execute("ALTER TABLE book AUTO_INCREMENT = 1");
        }
    }



}
