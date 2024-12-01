package org.example.bigassignment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.time.LocalDate;

public class LoanService {

    /**
     * Lấy tất cả các đơn mượn sách từ cơ sở dữ liệu.
     * @return ObservableList chứa danh sách tất cả các đơn mượn sách.
     */
    public ObservableList<Loan> getAllLoans() {
        ObservableList<Loan> loans = FXCollections.observableArrayList();
        String sql = "SELECT * FROM loan ORDER BY id ASC"; // Truy vấn dữ liệu, sắp xếp theo ID

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                int bookId = rs.getInt("book_id");
                int borrowerId = rs.getInt("borrower_id");
                LocalDate loanDate = rs.getDate("borrow_date").toLocalDate();
                LocalDate dueDate = rs.getDate("due_date").toLocalDate();
                LocalDate returnDate = (rs.getDate("return_date") != null) ? rs.getDate("return_date").toLocalDate() : null;
                String status = rs.getString("status");
                int fine = rs.getInt("fine");

                // Thêm vào danh sách đơn mượn
                loans.add(new Loan(id, bookId, borrowerId, loanDate, dueDate, returnDate, status, fine));
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy danh sách đơn mượn: " + e.getMessage());
        }
        return loans;
    }

    /**
     * Thêm đơn mượn sách mới vào cơ sở dữ liệu.
     * @param loan Loan đối tượng chứa thông tin đơn mượn.
     * @return boolean true nếu thêm thành công, false nếu thất bại.
     */
    public boolean addLoan(Loan loan) {
        String insertLoanSql = "INSERT INTO loan (book_id, borrower_id, borrow_date, due_date, status, fine) VALUES (?, ?, ?, ?, 'ON_LOAN', 0)";
        String updateBookQuantitySql = "UPDATE book SET available_quantity = available_quantity - 1 WHERE id = ? AND available_quantity > 0";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); // Bắt đầu transaction

            try (PreparedStatement loanStmt = conn.prepareStatement(insertLoanSql);
                 PreparedStatement updateBookStmt = conn.prepareStatement(updateBookQuantitySql)) {

                // Chèn đơn mượn mới vào bảng loan
                loanStmt.setInt(1, loan.getBookId());
                loanStmt.setInt(2, loan.getBorrowerId());
                loanStmt.setDate(3, Date.valueOf(loan.getLoanDate()));
                loanStmt.setDate(4, Date.valueOf(loan.getDueDate()));
                loanStmt.executeUpdate();

                // Cập nhật số lượng sách trong bảng book
                updateBookStmt.setInt(1, loan.getBookId());
                int rowsUpdated = updateBookStmt.executeUpdate();

                if (rowsUpdated == 0) {
                    throw new SQLException("Không thể cập nhật số lượng sách. Sách không tồn tại hoặc hết sách.");
                }

                conn.commit(); // Hoàn thành transaction
                return true;

            } catch (SQLException e) {
                conn.rollback(); // Quay lại trạng thái trước transaction
                System.err.println("Lỗi khi thêm đơn mượn: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi kết nối cơ sở dữ liệu: " + e.getMessage());
            return false;
        }
    }

    // cập nhật lại ID
    public void resetAutoIncrement() {
        String query = "ALTER TABLE loan AUTO_INCREMENT = 1"; // Hoặc một giá trị ID cụ thể nếu cần

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.err.println("Lỗi khi reset AUTO_INCREMENT: " + e.getMessage());
        }
    }
    /**
     * Xóa đơn mượn sách khỏi cơ sở dữ liệu.
     * @param loanId ID của đơn mượn cần xóa.
     * @return boolean true nếu xóa thành công, false nếu thất bại.
     */
    public boolean deleteLoan(int loanId) {
        String deleteLoanSql = "DELETE FROM loan WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(deleteLoanSql)) {

            stmt.setInt(1, loanId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Lỗi khi xóa đơn mượn: " + e.getMessage());
            return false;
        }
    }


    // Phương thức cập nhật số lượng sách khi trả lại sách
    public void updateBookQuantityAfterReturn(int loanId) {
        String updateBookQuantitySql = "UPDATE book SET available_quantity = available_quantity + 1 " +
                "WHERE id = (SELECT book_id FROM loan WHERE id = ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateBookQuantitySql)) {

            stmt.setInt(1, loanId);  // Sử dụng loanId để lấy ID sách
            stmt.executeUpdate(); // Cập nhật số lượng sách có sẵn

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Phương thức trả sách
    public boolean returnBook(int loanId, LocalDate returnedDate, long fineAmount, String status) {
        String updateLoanSql = "UPDATE loan SET return_date = ?, fine = ?, status = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateLoanSql)) {

            stmt.setDate(1, Date.valueOf(returnedDate));
            stmt.setLong(2, fineAmount);
            stmt.setString(3, status);
            stmt.setInt(4, loanId);
            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0; // Trả về true nếu cập nhật thành công

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Trả về false nếu có lỗi xảy ra
    }

    // Xử lý khi ấn nút check
    public Loan getLoanById(int loanId) {
        String query = "SELECT * FROM loan WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, loanId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                int bookId = rs.getInt("book_id");
                int borrowerId = rs.getInt("borrower_id");
                LocalDate loanDate = rs.getDate("borrow_date").toLocalDate();
                LocalDate dueDate = rs.getDate("due_date").toLocalDate();
                LocalDate returnDate = (rs.getDate("return_date") != null) ? rs.getDate("return_date").toLocalDate() : null;
                String status = rs.getString("status");
                int fine = rs.getInt("fine");

                return new Loan(id, bookId, borrowerId, loanDate, dueDate, returnDate, status, fine);
            }

        } catch (SQLException e) {
            System.err.println("Lỗi khi lấy thông tin loan: " + e.getMessage());
        }
        return null;
    }

}
