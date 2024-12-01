package org.example.bigassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class LoanManagementController {
    Stage stage;
    Scene scene;
    Parent root;

    @FXML
    private TableView<Loan> loanTable; // TableView để hiển thị danh sách

    @FXML
    private TableColumn<Loan, Integer> idColumn; // Cột ID
    @FXML
    private TableColumn<Loan, Integer> bookIdColumn; // Cột ID sách
    @FXML
    private TableColumn<Loan, Integer> borrowerIdColumn; // Cột ID người mượn
    @FXML
    private TableColumn<Loan, LocalDate> loanDateColumn; // Cột ngày mượn
    @FXML
    private TableColumn<Loan, LocalDate> dueDateColumn; // Cột ngày hạn trả
    @FXML
    private TableColumn<Loan, LocalDate> returnDateColumn; // Cột ngày trả thực tế
    @FXML
    private TableColumn<Loan, String> statusColumn; // Cột trạng thái
    @FXML
    private TableColumn<Loan, Integer> fineColumn; // Cột tiền phạt

    private LoanService loanService; // Đối tượng LoanService

    public LoanManagementController() {
        loanService = new LoanService(); // Khởi tạo LoanService
    }

    @FXML
    public void initialize() {
        // Khởi tạo các cột trong TableView
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        borrowerIdColumn.setCellValueFactory(new PropertyValueFactory<>("borrowerId"));
        loanDateColumn.setCellValueFactory(new PropertyValueFactory<>("loanDate"));
        dueDateColumn.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        fineColumn.setCellValueFactory(new PropertyValueFactory<>("fine"));

        // Lấy dữ liệu và hiển thị vào TableView
        loanTable.setItems(loanService.getAllLoans());
    }

    // Phương thức xử lý sự kiện khi nhấn nút "Đăng ký mượn sách"
    @FXML
    private void openRegisterLoanScene(ActionEvent event) {
        try {
            // Lấy Stage hiện tại của cửa sổ LoanManagement
            Stage currentStage = (Stage) loanTable.getScene().getWindow();

            // Tải file FXML MuonSach.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MuonSach.fxml"));
            AnchorPane root = loader.load(); // MuonSach.fxml là root của Scene mới

            // Tạo một cửa sổ (Stage) mới để hiển thị MuonSach.fxml
            Stage registerLoanStage = new Stage();
            registerLoanStage.setScene(new Scene(root));
            registerLoanStage.setTitle("Đăng ký mượn sách");

            // Đặt cửa sổ này làm cửa sổ con (overlay) trên cửa sổ chính
            registerLoanStage.initModality(Modality.APPLICATION_MODAL); // Cửa sổ con sẽ không thể tương tác với cửa sổ chính khi mở
            registerLoanStage.initOwner(currentStage); // Đặt cửa sổ chính là "owner" của cửa sổ con

            // Hiển thị cửa sổ con
            registerLoanStage.showAndWait(); // Sử dụng showAndWait để chặn sự kiện cho đến khi cửa sổ con đóng

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void openReturnedLoanScene(ActionEvent event) {
        try {
            // Lấy Stage hiện tại của cửa sổ LoanManagement
            Stage currentStage = (Stage) loanTable.getScene().getWindow();

            // Tải file FXML MuonSach.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("TraSach.fxml"));
            AnchorPane root = loader.load(); // TraSach.fxml là root của Scene mới

            // Tạo một cửa sổ (Stage) mới để hiển thị MuonSach.fxml
            Stage registerLoanStage = new Stage();
            registerLoanStage.setScene(new Scene(root));
            registerLoanStage.setTitle("Đăng ký trả sách");

            // Đặt cửa sổ này làm cửa sổ con (overlay) trên cửa sổ chính
            registerLoanStage.initModality(Modality.APPLICATION_MODAL); // Cửa sổ con sẽ không thể tương tác với cửa sổ chính khi mở
            registerLoanStage.initOwner(currentStage); // Đặt cửa sổ chính là "owner" của cửa sổ con

            // Hiển thị cửa sổ con
            registerLoanStage.showAndWait(); // Sử dụng showAndWait để chặn sự kiện cho đến khi cửa sổ con đóng

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Phương thức để làm mới TableView
    public void refreshLoanTable() {
        loanTable.setItems(loanService.getAllLoans()); // Lấy lại danh sách các bản mươn sách từ CSDL
    }

    // Lấy ra 1 đối tượng được chọn trong bảng
    public Loan getSelectedLoan() {
        // Kiểm tra nếu không có đối tượng nào được chọn trong bảng
        Loan selectedLoan = loanTable.getSelectionModel().getSelectedItem();
        if (selectedLoan == null) {
            // Nếu không có đối tượng được chọn, trả về null
            System.out.println("Không có đơn mượn nào được chọn.");
        }
        return selectedLoan;
    }

    @FXML
    private void handleDeleteLoan() {
        // Lấy đối tượng được chọn trong bảng
        Loan selectedLoan = loanTable.getSelectionModel().getSelectedItem();

        if (selectedLoan == null) {
            // Hiển thị cảnh báo nếu không có đối tượng nào được chọn
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cảnh báo");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn một đơn mượn để xóa.");
            alert.showAndWait();
            return;
        }

        // Hiển thị hộp thoại xác nhận xóa
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Xác nhận");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Bạn có chắc chắn muốn xóa đơn mượn này?");
        if (confirmationAlert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return; // Nếu người dùng không xác nhận xóa, thoát ra
        }

        // Xóa đơn mượn khỏi cơ sở dữ liệu
        if (loanService.deleteLoan(selectedLoan.getId())) {
            // Reset AUTO_INCREMENT nếu cần
            loanService.resetAutoIncrement();

            // Hiển thị thông báo thành công
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Thành công");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Đơn mượn đã được xóa thành công.");
            successAlert.showAndWait();

            // Làm mới bảng
            refreshLoanTable();
        } else {
            // Hiển thị thông báo lỗi
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Lỗi");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Không thể xóa đơn mượn. Vui lòng thử lại.");
            errorAlert.showAndWait();
        }
    }

    // Phương thức chuyển cảnh về Trang chủ
    public void switchToHome(ActionEvent event) throws IOException {
        switchScene("home.fxml", event);
    }

    // Phương thức chuyển cảnh sang Quản lý Sách
    public void switchToQuanLySach(ActionEvent event) throws IOException {
        switchScene("BookManagement.fxml", event);
    }

    // Phương thức chuyển cảnh sang Quản lý Người mượn
    public void switchToQuanLyNguoiMuon(ActionEvent event) throws IOException {
        switchScene("BorrowerManagement.fxml", event);
    }

    // Phương thức chuyển cảnh sang Quản lý Mượn Trả Sách
    public void switchToQuanLyMuonTraSach(ActionEvent event) throws IOException {
        switchScene("LoanManagement.fxml", event);
    }

    // Phương thức chuyển cảnh chung
    private void switchScene(String fxmlFile, ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlFile)); // Tải file FXML
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Lấy cửa sổ hiện tại
        scene = new Scene(root); // Tạo scene mới
        stage.setScene(scene); // Cập nhật cửa sổ hiện tại với scene mới
        stage.show(); // Hiển thị cửa sổ mới
    }
}
