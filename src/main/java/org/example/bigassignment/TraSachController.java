package org.example.bigassignment;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.time.LocalDate;

public class TraSachController {

    @FXML
    private TextField loanIdField; // Trường nhập LoanID
    @FXML
    private Label borrowerIdLabel; // Label hiển thị Borrower ID
    @FXML
    private Label bookIdLabel; // Label hiển thị Book ID
    @FXML
    private DatePicker loanDatePicker; // DatePicker hiển thị Loan Date
    @FXML
    private DatePicker dueDatePicker; // DatePicker hiển thị Due Date
    @FXML
    private DatePicker returnedDatePicker; // DatePicker để người dùng chọn ngày trả sách
    @FXML
    private Button checkButton; // Nút Check
    @FXML
    private Button confirmReturnButton; // Nút Xác nhận trả sách
    @FXML
    private Button exitButton; // Nút Exit

    private LoanService loanService; // Dịch vụ LoanService để lấy thông tin

    private boolean checkID = false; // Kiểm tra xem ấn nút check ID chưa
    // Constructor
    public TraSachController() {
        this.loanService = new LoanService(); // Khởi tạo dịch vụ LoanService
    }


    // Phương thức xử lý sự kiện khi ấn nút Check
    @FXML
    private void handleCheckLoan() {
        String loanIdText = loanIdField.getText();
        checkID = true;
        if (loanIdText.isEmpty()) {
            showAlert(AlertType.WARNING, "Cảnh báo", "Vui lòng nhập Loan ID!");
            return;
        }

        int loanId;
        try {
            loanId = Integer.parseInt(loanIdText);
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Lỗi", "Loan ID phải là một số nguyên!");
            return;
        }

        // Lấy thông tin đơn mượn từ cơ sở dữ liệu
        Loan loan = loanService.getLoanById(loanId);

        if (loan == null) {
            showAlert(AlertType.ERROR, "Lỗi", "Loan ID không tồn tại!");
            return;
        }

        // Kiểm tra xem sách đã được trả chưa
        if (loan.getStatus().equals("RETURNED") || loan.getStatus().equals("LATE")) {
            showAlert(AlertType.INFORMATION, "Thông báo", "Sách này đã được trả rồi!");
            return;
        }

        // Cập nhật các trường trong UI
        borrowerIdLabel.setText("Borrower ID: " + loan.getBorrowerId());
        bookIdLabel.setText("Book ID: " + loan.getBookId());
        loanDatePicker.setValue(loan.getLoanDate());
        dueDatePicker.setValue(loan.getDueDate());

        // Hiển thị nút Xác nhận trả sách khi đã nhập Loan ID và nhấn Check
        confirmReturnButton.setVisible(true);
    }

    // Phương thức xử lý sự kiện khi ấn nút Xác nhận trả sách
    @FXML
    private void handleReturnBook() {
        // Lấy các giá trị từ UI
        String loanIdText = loanIdField.getText();
        LocalDate returnedDate = returnedDatePicker.getValue();
        LocalDate loanDate = loanDatePicker.getValue();
        LocalDate dueDate = dueDatePicker.getValue();

        if (loanIdText.isEmpty() || returnedDate == null) {
            showAlert(AlertType.WARNING, "Cảnh báo", "Vui lòng điền đầy đủ thông tin và chọn ngày trả sách!");
            return;
        }

        if (checkID == false) {
            showAlert(AlertType.WARNING, "Cảnh báo", "Vui lòng điền kiểm tra Loan ID");
            return;
        }
        // Kiểm tra nếu ngày trả sách phải sau ngày mượn
        if (returnedDate.isBefore(loanDate)) {
            showAlert(AlertType.WARNING, "Cảnh báo", "Ngày trả sách không thể trước ngày mượn!");
            return;
        }

        int loanId = Integer.parseInt(loanIdText);

        // Kiểm tra xem ngày trả sách có trễ không
        long fineAmount = 0;
        long daysLate = 0;
        if (returnedDate.isAfter(dueDate)) {
            daysLate = java.time.temporal.ChronoUnit.DAYS.between(dueDate, returnedDate);

            // Tính phạt
            if (daysLate > 14) {
                fineAmount = 200000; // Phạt 200k nếu trễ hơn 14 ngày
            } else {
                fineAmount = daysLate * 10000; // Phạt 10k mỗi ngày trễ
            }
        }

        // Cập nhật trạng thái trả sách (RETURNED hoặc LATE)
        String status = (fineAmount > 0) ? "LATE" : "RETURNED";

        // Cập nhật dữ liệu trong cơ sở dữ liệu
        boolean success = loanService.returnBook(loanId, returnedDate, fineAmount, status);

        if (success) {
            // Cập nhật lại số lượng sách có sẵn
            loanService.updateBookQuantityAfterReturn(loanId);

            String fineMessage = fineAmount > 0 ? "Trả muộn " + daysLate + " ngày. Phạt: " + fineAmount + " VND." : "";
            showAlert(AlertType.INFORMATION, "Thành công", "Trả sách thành công! " + fineMessage);

            // Đóng cửa sổ sau khi trả sách thành công
            Stage stage = (Stage) confirmReturnButton.getScene().getWindow();
            stage.close();

        } else {
            showAlert(AlertType.ERROR, "Lỗi", "Đã xảy ra lỗi khi trả sách!");
        }
    }

    // Phương thức hiển thị cảnh báo
    private void showAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Phương thức xử lý sự kiện khi ấn nút Exit
    @FXML
    private void handleExit() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
