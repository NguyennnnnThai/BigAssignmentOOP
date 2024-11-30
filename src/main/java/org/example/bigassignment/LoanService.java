package org.example.bigassignment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

public class LoanService {

    // Phương thức để lấy tất cả các Loan từ cơ sở dữ liệu
    public ObservableList<Loan> getAllLoans() {
        ObservableList<Loan> loans = FXCollections.observableArrayList();
        String sql = "SELECT * FROM loan"; // Truy vấn lấy tất cả dữ liệu từ bảng loan

        try (Connection conn = DatabaseConnection.getConnection(); // Sử dụng kết nối từ DatabaseConnection
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            // Duyệt qua tất cả các bản ghi trong kết quả trả về
            while (rs.next()) {
                int id = rs.getInt("id");
                int bookId = rs.getInt("book_id");
                int borrowerId = rs.getInt("borrower_id");
                LocalDate loanDate = rs.getDate("borrow_date").toLocalDate();
                LocalDate dueDate = rs.getDate("due_date").toLocalDate();
                LocalDate returnDate = rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null;
                String status = rs.getString("status");
                int fine = rs.getInt("fine");

                // Tạo đối tượng Loan và thêm vào danh sách
                loans.add(new Loan(id, bookId, borrowerId, loanDate, dueDate, returnDate, status, fine));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loans;
    }

    // Phương thức để thêm thông tin mượn sách vào cơ sở dữ liệu
    public void addLoan(Loan loan) {
        // SQL để thêm thông tin mượn sách vào bảng loan
        String insertLoanSql = "INSERT INTO loan (book_id, borrower_id, borrow_date, due_date, status, fine) VALUES (?, ?, ?, ?, 'ON_LOAN', 0)";

        // SQL để cập nhật số lượng sách sau khi mượn
        String updateBookQuantitySql = "UPDATE book SET available_quantity = available_quantity - 1 WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection()) {
            // Bắt đầu transaction để đảm bảo tính toàn vẹn dữ liệu
            conn.setAutoCommit(false); // Tắt tự động commit để thực hiện transaction

            try (PreparedStatement loanStmt = conn.prepareStatement(insertLoanSql);
                 PreparedStatement updateBookStmt = conn.prepareStatement(updateBookQuantitySql)) {

                // Insert thông tin mượn sách vào bảng loan
                loanStmt.setInt(1, loan.getBookId());
                loanStmt.setInt(2, loan.getBorrowerId());
                loanStmt.setDate(3, Date.valueOf(loan.getLoanDate())); // Ngày mượn
                loanStmt.setDate(4, Date.valueOf(loan.getDueDate())); // Ngày phải trả
                loanStmt.executeUpdate(); // Thực thi câu lệnh insert

                // Cập nhật số lượng sách trong bảng book
                updateBookStmt.setInt(1, loan.getBookId());
                int rowsUpdated = updateBookStmt.executeUpdate(); // Thực thi câu lệnh update

                // Kiểm tra nếu không có sách nào được cập nhật
                if (rowsUpdated == 0) {
                    throw new SQLException("Không thể cập nhật số lượng sách, ID sách không tồn tại.");
                }

                // Commit transaction nếu mọi thứ thành công
                conn.commit();

            } catch (SQLException e) {
                // Nếu có lỗi, rollback transaction
                conn.rollback();
                throw e; // Ném lại lỗi để xử lý bên ngoài
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
