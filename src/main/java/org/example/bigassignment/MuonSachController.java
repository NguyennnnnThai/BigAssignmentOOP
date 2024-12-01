package org.example.bigassignment;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.sql.*;

public class MuonSachController {

    @FXML
    private TextField bookIdField; // Trường nhập ID sách
    @FXML
    private TextField borrowerIdField; // Trường nhập ID người mượn
    @FXML
    private DatePicker loanDatePicker; // Ngày mượn
    @FXML
    private DatePicker dueDatePicker; // Ngày trả
    @FXML
    private Button exitButton; // Nút Exit

    private LoanService loanService;
    private LoanManagementController loanManagementController; // Tham chiếu đến LoanManagementController

    // Constructor mặc định để tương thích với FXML
    public MuonSachController() {
        this.loanService = new LoanService();
    }

    // Constructor nhận tham số LoanManagementController
    public MuonSachController(LoanManagementController loanManagementController) {
        this.loanManagementController = loanManagementController;
        this.loanService = new LoanService(); // Khởi tạo LoanService
    }

    // Phương thức xử lý sự kiện khi nhấn nút "Đăng ký mượn sách"
    @FXML
    private void handleRegisterLoan() {
        // Lấy thông tin từ các trường nhập liệu
        String bookId = bookIdField.getText();
        String borrowerId = borrowerIdField.getText();
        LocalDate loanDate = loanDatePicker.getValue();
        LocalDate dueDate = dueDatePicker.getValue();

        // Kiểm tra điều kiện: tất cả thông tin phải được điền
        if (bookId.isEmpty() || borrowerId.isEmpty() || loanDate == null || dueDate == null) {
            showAlert(AlertType.ERROR, "Lỗi", "Vui lòng điền đầy đủ thông tin.");
            return;
        }

        // Kiểm tra ID sách và số lượng sách
        if (!isAvailableBookQuantity(bookId)) {
            showAlert(AlertType.ERROR, "Lỗi", "Số lượng sách không đủ để mượn.");
            return;
        }
        if (!isValidBookId(bookId)) {
            showAlert(AlertType.ERROR, "Lỗi", "ID sách không tồn tại.");
            return;
        }



        // Kiểm tra ID người mượn
        if (!isValidBorrowerId(borrowerId)) {
            showAlert(AlertType.ERROR, "Lỗi", "ID người mượn không tồn tại.");
            return;
        }

        // Kiểm tra ngày trả sách phải sau ngày mượn và không mượn quá 1 tháng
        if (dueDate.isBefore(loanDate)) {
            showAlert(AlertType.ERROR, "Lỗi", "Ngày trả sách phải sau ngày mượn.");
            return;
        }

        if (dueDate.isAfter(loanDate.plusMonths(1))) {
            showAlert(AlertType.ERROR, "Lỗi", "Không thể mượn sách quá 1 tháng.");
            return;
        }

        // Nếu mọi điều kiện đều hợp lệ, tiến hành đăng ký mượn sách
        Loan loan = new Loan(Integer.parseInt(bookId), Integer.parseInt(borrowerId), loanDate, dueDate);
        loanService.addLoan(loan);  // Cập nhật cơ sở dữ liệu

        // Đóng cửa sổ MuonSach
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();

        // Hiển thị thông báo thành công
        showAlert(AlertType.INFORMATION, "Thành công", "Đăng ký mượn sách thành công!");

        // Gọi phương thức refreshLoanTable() để cập nhật lại TableView trong LoanManagementController
        if (loanManagementController != null) {
            loanManagementController.refreshLoanTable(); // Cập nhật lại TableView
        }
        System.out.println("Đăng ký mượn sách thành công!");
    }

    // Kiểm tra ID sách có tồn tại trong cơ sở dữ liệu và có đủ số lượng để mượn không
    private boolean isValidBookId(String bookId) {
        String query = "SELECT available_quantity FROM book WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, Integer.parseInt(bookId));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int availableQuantity = rs.getInt("available_quantity");
                return availableQuantity > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra ID người mượn có tồn tại trong cơ sở dữ liệu không
    private boolean isValidBorrowerId(String borrowerId) {
        String query = "SELECT id FROM borrower WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, Integer.parseInt(borrowerId));
            ResultSet rs = stmt.executeQuery();

            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra số lượng sách có đủ để mượn không (availableQuantity > 0)
    private boolean isAvailableBookQuantity(String bookId) {
        String query = "SELECT available_quantity FROM book WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, Integer.parseInt(bookId));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int availableQuantity = rs.getInt("available_quantity");
                return availableQuantity > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Phương thức xử lý sự kiện khi nhấn nút "Exit" (đóng cửa sổ MuonSach)
    @FXML
    private void handleExit() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    // Phương thức để hiển thị thông báo lỗi
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
