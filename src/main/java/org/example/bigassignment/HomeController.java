package org.example.bigassignment;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class HomeController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private Label totalBooksLabel; // Hiển thị tổng số sách
    @FXML
    private Label totalUsersLabel; // Hiển thị tổng số sinh viên
    @FXML
    private Label timeLabel; // Hiển thị thời gian hiện tại
    @FXML
    private PieChart booksPieChart; // PieChart hiển thị sách đang mượn và sách còn lại

    private final DataService dataService = new DataService();

    @FXML
    public void initialize() {
        // Cập nhật số liệu cho các Label
        totalBooksLabel.setText(String.valueOf(dataService.getTotalBooks()));
        totalUsersLabel.setText(String.valueOf(dataService.getTotalUsers()));

        // Cập nhật PieChart
        updatePieChart();

        // Cập nhật thời gian hiện tại
        updateTimeLabel();

        // Lên lịch để cập nhật thời gian mỗi giây
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), e -> updateTimeLabel())
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void updateTimeLabel() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        timeLabel.setText(LocalTime.now().format(formatter));
    }

    private void updatePieChart() {
        int booksOnLoan = dataService.getBooksOnLoan();
        int availableBooks = dataService.getAvailableBooks();

        PieChart.Data onLoanData = new PieChart.Data("Sách đang mượn", booksOnLoan);
        PieChart.Data availableData = new PieChart.Data("Sách có sẵn", availableBooks);

        booksPieChart.getData().clear();
        booksPieChart.getData().addAll(onLoanData, availableData);
    }

    // Chuyển sang mục Quản lý sách
    public void switchToQuanLySach(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("BookManagement.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Chuyển sang mục Quản lý người mượn
    public void switchToQuanLyNguoiMuon(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("BorrowerManagement.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Chuyển sang mục Quản lý mượn trả sách
    public void switchToQuanLyMuonTraSach(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("LoanManagement.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Hàm đăng xuất
    public void logOut(ActionEvent event) throws IOException {
        // Hiển thị hộp thoại xác nhận đăng xuất
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Xác nhận đăng xuất");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Bạn có chắc chắn muốn đăng xuất không?");

        // Nếu người dùng chọn "OK", thực hiện đăng xuất
        if (confirmationAlert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            root = FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
}
