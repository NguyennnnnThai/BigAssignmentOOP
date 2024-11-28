package org.example.bigassignment;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class InterfaceSceneController implements Initializable {


    Stage stage;
    Scene scene;
    Parent root;

    @FXML
    private ChoiceBox<String> myChoiceBox;
    @FXML
    private Label clockLabel;
    @FXML
    private Label totalBooksLabel;

    @FXML
    private Label totalUsersLabel;

    @FXML
    private Label overdueBooksLabel;

    @FXML
    private PieChart borrowPieChart;

    @FXML
    private LineChart<String, Number> borrowLineChart;

    @FXML
    private BarChart<String, Number> bookDistributionBarChart;

    @FXML
    private CategoryAxis xAxis;

    @FXML
    private NumberAxis yAxis;

    @FXML
    private TableView<OverdueBorrow> overdueTable;

    @FXML
    private TableColumn<OverdueBorrow, Integer> borrowIdColumn;

    @FXML
    private TableColumn<OverdueBorrow, Integer> bookIdColumn;

    @FXML
    private TableColumn<OverdueBorrow, String> bookTitleColumn;


    private String[] options = {"Đăng xuất", "Cài đặt tài khoản", "Chế độ ban đêm"};
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        myChoiceBox.getItems().addAll(options);
        myChoiceBox.setOnAction(event -> handleChoiceBoxSelection());

        startClock();
        updateLabels(); // Cập nhật dữ liệu khi giao diện khởi động
        updateBorrowLineChart();
        updateBorrowPieChart();
        updateBookDistributionBarChart();
        // Cấu hình thêm cho xAxis
        xAxis.setLabel("Thể loại sách");
        xAxis.setCategories(FXCollections.observableArrayList("Khoa học", "Văn học", "Lịch sử"));

        // Cấu hình thêm cho yAxis
        yAxis.setLabel("Số lượng sách");
        yAxis.setTickUnit(10); // Khoảng cách giữa các giá trị trên trục y

        // Liên kết cột với thuộc tính của lớp OverdueBorrow
        borrowIdColumn.setCellValueFactory(new PropertyValueFactory<>("borrowId"));
        bookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
        bookTitleColumn.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));

        // Tải dữ liệu từ cơ sở dữ liệu và đưa vào TableView
        List<OverdueBorrow> overdueList = getOverdueBorrows();
        overdueTable.getItems().addAll(overdueList);
    }

    private void startClock() {
        // Tạo Timeline để cập nhật thời gian mỗi giây
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0), event -> updateClock()),
                new KeyFrame(Duration.seconds(1))
        );
        timeline.setCycleCount(Timeline.INDEFINITE); // Lặp vô hạn
        timeline.play(); // Bắt đầu
    }

    private void updateClock() {
        LocalDateTime now = LocalDateTime.now();
        String formattedTime = now.format(DATE_TIME_FORMATTER); // Định dạng thời gian
        clockLabel.setText(formattedTime); // Cập nhật thời gian lên Label
    }

    private void handleChoiceBoxSelection() {
        String selectedOption = myChoiceBox.getValue();

        if ("Đăng xuất".equals(selectedOption)) {
            logout();
        } else if ("Chế độ ban đêm".equals(selectedOption)) {
            toggleNightMode();
        } else if ("Cài đặt tài khoản".equals(selectedOption)) {
            openAccountSettings();
        }
    }

    private void logout() {
        // Tạo cảnh báo xác nhận
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Xác nhận đăng xuất");
        alert.setHeaderText(null);
        alert.setContentText("Bạn có chắc chắn muốn đăng xuất?");

        // Hiển thị cảnh báo và chờ phản hồi từ người dùng
        Optional<ButtonType> result = alert.showAndWait();

        // Kiểm tra xem người dùng đã chọn "OK" hay không
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Tải FXML của màn hình đăng nhập
                FXMLLoader loader = new FXMLLoader(getClass().getResource("LoginScene.fxml"));
                Parent loginRoot = loader.load();

                // Lấy Stage hiện tại từ một control trên giao diện hiện tại
                Stage currentStage = (Stage) myChoiceBox.getScene().getWindow();

                // Thiết lập lại cảnh với LoginScene
                Scene loginScene = new Scene(loginRoot);
                currentStage.setScene(loginScene);
                currentStage.show();
            } catch (IOException e) {
                e.printStackTrace();
                // Xử lý ngoại lệ, có thể hiển thị thông báo lỗi cho người dùng nếu cần
            }
        } else {
            // Nếu người dùng chọn "Cancel", không làm gì cả (vẫn giữ nguyên màn hình hiện tại)
        }
    }


    private void toggleNightMode() {
        // Thêm mã để bật/tắt chế độ ban đêm
    }

    private void openAccountSettings() {
        try {
            // Tải FXML của màn hình cài đặt tài khoản
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InfoScene.fxml"));
            Parent accountSettingsRoot = loader.load();

            // Lấy Stage hiện tại từ một control trên giao diện hiện tại
            Stage currentStage = (Stage) myChoiceBox.getScene().getWindow();

            // Thiết lập lại cảnh với màn hình cài đặt tài khoản
            Scene accountSettingsScene = new Scene(accountSettingsRoot);
            currentStage.setScene(accountSettingsScene);
            currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Xử lý lỗi nếu có (có thể hiển thị thông báo lỗi cho người dùng)
        }
    }

    // Phương thức để lấy tổng số sách
    private int getTotalBooks() {
        int totalBooks = 0;
        String query = "SELECT COUNT(*) AS total FROM book";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                totalBooks = resultSet.getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalBooks;
    }

    // Phương thức để lấy tổng số sinh viên
    private int getTotalUsers() {
        int totalUsers = 0;
        String query = "SELECT COUNT(*) AS total FROM user";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                totalUsers = resultSet.getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return totalUsers;
    }

    // Phương thức để lấy số sách quá hạn
    private int getOverdueBooks() {
        int overdueBooks = 0;
        String query = "SELECT COUNT(*) AS total FROM borrow WHERE returned = 0 AND return_date < CURDATE()";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            if (resultSet.next()) {
                overdueBooks = resultSet.getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return overdueBooks;
    }

    private void updateLabels() {
        totalBooksLabel.setText(String.valueOf(getTotalBooks()));
        totalUsersLabel.setText(String.valueOf(getTotalUsers()));
        overdueBooksLabel.setText(String.valueOf(getOverdueBooks()));
    }

    // Phương thức để lấy dữ liệu thống kê mượn sách
    private Map<String, Integer> getMonthlyBorrowStatistics() {
        Map<String, Integer> borrowData = new HashMap<>();
        String query = "SELECT MONTH(borrow_date) AS month, COUNT(*) AS total " +
                "FROM borrow " +
                "WHERE YEAR(borrow_date) = YEAR(CURDATE()) " +
                "GROUP BY MONTH(borrow_date)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String month = String.valueOf(resultSet.getInt("month"));
                int total = resultSet.getInt("total");
                borrowData.put(month, total);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return borrowData;
    }

    // Phương thức để cập nhật LineChart
    private void updateBorrowLineChart() {
        borrowLineChart.getData().clear(); // Xóa dữ liệu cũ nếu có

        // Tạo series mới
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Số lượng mượn sách");

        // Lấy dữ liệu thống kê và thêm vào series
        Map<String, Integer> borrowData = getMonthlyBorrowStatistics();
        for (Map.Entry<String, Integer> entry : borrowData.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        // Thêm series vào LineChart
        borrowLineChart.getData().add(series);
    }

    // Phương thức lấy dữ liệu cho PieChart
    private int[] getBorrowedAndAvailableBooks() {
        int borrowedBooks = 0;
        int totalBooks = 0;

        String query = "SELECT " +
                "  SUM(CASE WHEN is_available = 0 THEN 1 ELSE 0 END) AS borrowed_books, " +
                "  COUNT(*) AS total_books " +
                "FROM book";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            if (resultSet.next()) {
                borrowedBooks = resultSet.getInt("borrowed_books");
                totalBooks = resultSet.getInt("total_books");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new int[]{borrowedBooks, totalBooks};
    }

    // Phương thức cập nhật PieChart
    private void updateBorrowPieChart() {
        borrowPieChart.getData().clear(); // Xóa dữ liệu cũ nếu có

        // Lấy dữ liệu sách
        int[] data = getBorrowedAndAvailableBooks();
        int borrowedBooks = data[0];
        int totalBooks = data[1];
        int availableBooks = totalBooks - borrowedBooks;

        // Tạo các phần của PieChart
        PieChart.Data borrowedSlice = new PieChart.Data("Đã mượn (" + borrowedBooks + ")", borrowedBooks);
        PieChart.Data availableSlice = new PieChart.Data("Còn sẵn (" + availableBooks + ")", availableBooks);

        // Thêm các phần vào PieChart
        borrowPieChart.getData().addAll(borrowedSlice, availableSlice);

        // Tùy chỉnh tooltip hiển thị tỉ lệ phần trăm (tuỳ chọn)
        borrowPieChart.getData().forEach(dataSlice -> {
            double percentage = (dataSlice.getPieValue() / totalBooks) * 100;
            dataSlice.getNode().setOnMouseEntered(event ->
                    borrowPieChart.setTitle(String.format("%s: %.1f%%", dataSlice.getName(), percentage)));
        });
    }

    // Phương thức lấy dữ liệu phân bố sách theo thể loại
    private XYChart.Series<String, Number> getBookDistributionByGenre() {
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Phân bố sách theo thể loại");

        String query = "SELECT genre, COUNT(*) AS count FROM book GROUP BY genre";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String genre = resultSet.getString("genre");
                int count = resultSet.getInt("count");
                series.getData().add(new XYChart.Data<>(genre, count));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return series;
    }

    // Phương thức cập nhật BarChart
    private void updateBookDistributionBarChart() {
        bookDistributionBarChart.getData().clear(); // Xóa dữ liệu cũ nếu có
        bookDistributionBarChart.getData().add(getBookDistributionByGenre());
    }

    public List<OverdueBorrow> getOverdueBorrows() {
        List<OverdueBorrow> overdueList = new ArrayList<>();
        String query = """
        SELECT b.borrow_id, b.book_id, bo.title
        FROM borrow b
        JOIN book bo ON b.book_id = bo.book_id
        WHERE b.returned = 0 AND b.return_date < NOW();
        """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int borrowId = resultSet.getInt("borrow_id");
                int bookId = resultSet.getInt("book_id");
                String bookTitle = resultSet.getString("title");

                overdueList.add(new OverdueBorrow(borrowId, bookId, bookTitle));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return overdueList;
    }


    // Hàm chuyển sang trang mượn sách
    // Hàm chuyển sang mục Quản lý sách
    public void switchToBookManagement(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("BookManagement.fxml"));
        Scene scene = new Scene(root);
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

}
