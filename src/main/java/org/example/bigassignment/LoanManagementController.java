package org.example.bigassignment;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class LoanManagementController {
    Stage stage;
    Scene scene;
    Parent root;

    @FXML
    private TableView<Loan> loanTable; // TableView để hiển thị danh sách các khoản vay

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

    // Phương thức để làm mới TableView
    public void refreshLoanTable() {
        loanTable.setItems(loanService.getAllLoans()); // Lấy lại danh sách các khoản vay từ CSDL
    }

    // Chuyển về Trang chủ
    public void switchToHome(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("home.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    // Hàm chuyển sang mục Quản lý sách
    public void switchToQuanLySach (ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("BookManagement.fxml"));
        Scene scene = new Scene(root);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    // Hàm chuyển sang mục Quản lý người mượn
    public void switchToQuanLyNguoiMuon (ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("BorrowerManagement.fxml"));
        Scene scene = new Scene(root);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    // Hàm chuyển sang mục Quản lý mượn trả sách
    public void switchToQuanLyMuonTraSach (ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("LoanManagement.fxml"));
        Scene scene = new Scene(root);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}
