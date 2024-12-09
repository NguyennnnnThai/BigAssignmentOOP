package org.example.bigassignment;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class BookManagementController {
    Stage stage;
    Scene scene;
    Parent root;

    @FXML
    private TableView<Book> bookTable;
    @FXML
    private TableColumn<Book, Integer> idColumn;
    @FXML
    private TableColumn<Book, String> titleColumn;
    @FXML
    private TableColumn<Book, String> categoryColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, Integer> availableQuantityColumn;
    @FXML
    private TableColumn<Book, Integer> totalQuantityColumn;

    @FXML
    private TextField titleField;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField availableQuantityField;
    @FXML
    private TextField totalQuantityField;

    private ObservableList<Book> bookList;

    public void initialize() {
        // Tải dữ liệu từ cơ sở dữ liệu và gắn vào TableView
        bookList = FXCollections.observableArrayList(BookService.getAllBooks());
        bookTable.setItems(bookList);

        // Liên kết cột với các thuộc tính của lớp Book
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        availableQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("availableQuantity"));
        totalQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("totalQuantity"));

        // Lắng nghe sự kiện chọn trong bảng
        bookTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                showBookDetails(newValue);
            }
        });
    }

    // Phương thức hiển thị chi tiết Book lên TextField
    private void showBookDetails(Book book) {
        titleField.setText(book.getTitle());
        authorField.setText(book.getAuthor());
        categoryField.setText(book.getCategory());
        availableQuantityField.setText(String.valueOf(book.getAvailableQuantity()));
        totalQuantityField.setText(String.valueOf(book.getTotalQuantity()));
    }

    @FXML
    private void addBook() {
        try {
            // Kiểm tra nếu các trường không rỗng
            if (titleField.getText().isEmpty() || categoryField.getText().isEmpty() || authorField.getText().isEmpty() ||
                    availableQuantityField.getText().isEmpty() || totalQuantityField.getText().isEmpty()) {

                // Hiển thị cảnh báo nếu có trường nhập liệu rỗng
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng nhập đầy đủ thông tin!");
                alert.showAndWait();
                return;
            }

            // Lấy dữ liệu từ TextField
            String title = titleField.getText();
            String category = categoryField.getText();
            String author = authorField.getText();
            int availableQuantity = Integer.parseInt(availableQuantityField.getText());
            int totalQuantity = Integer.parseInt(totalQuantityField.getText());

            // Kiểm tra nếu availableQuantity hoặc totalQuantity <= 0
            if (availableQuantity < 0 || totalQuantity < 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Số lượng sách phải không được nhỏ hơn 0!");
                alert.showAndWait();
                return;
            }

            // Kiểm tra nếu availableQuantity > totalQuantity
            if (availableQuantity > totalQuantity) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Số lượng có sẵn không được lớn hơn tổng số lượng!");
                alert.showAndWait();
                return;
            }

            // Tạo một đối tượng Book mới
            Book newBook = new Book(title, author, category, availableQuantity, totalQuantity);

            // Thêm sách vào cơ sở dữ liệu
            BookService.addBook(newBook);

            // Làm mới bảng để hiển thị sách mới
            bookList.setAll(BookService.getAllBooks());  // Lấy lại toàn bộ sách từ cơ sở dữ liệu
            bookTable.setItems(bookList);  // Cập nhật lại TableView

            // Xóa dữ liệu trong các trường nhập liệu
            clearFields();

            // Hiển thị thông báo thành công
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Thành công");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Thêm sách thành công!");
            successAlert.showAndWait();

        } catch (NumberFormatException e) {
            // Hiển thị lỗi nếu nhập sai định dạng số
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng nhập đúng định dạng số cho số lượng!");
            alert.showAndWait();
        } catch (Exception e) {
            // Hiển thị lỗi nếu có lỗi bất ngờ
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Đã xảy ra lỗi khi thêm sách!");
            alert.showAndWait();
        }
    }





    private void clearFields() {
        titleField.clear();
        categoryField.clear();
        authorField.clear();
        availableQuantityField.clear();
        totalQuantityField.clear();
    }

    @FXML
    public void updateBook(ActionEvent event) {
        // Lấy đối tượng được chọn trong TableView
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();

        if (selectedBook != null) {
            try {
                // Kiểm tra các TextField có rỗng không
                if (titleField.getText().isEmpty() || authorField.getText().isEmpty() ||
                        categoryField.getText().isEmpty() || availableQuantityField.getText().isEmpty() ||
                        totalQuantityField.getText().isEmpty()) {

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Cảnh báo");
                    alert.setHeaderText(null);
                    alert.setContentText("Vui lòng điền đầy đủ thông tin trước khi sửa!");
                    alert.showAndWait();
                    return;
                }

                // Lấy dữ liệu mới từ các TextField
                String newTitle = titleField.getText();
                String newAuthor = authorField.getText();
                String newCategory = categoryField.getText();
                int newAvailableQuantity = Integer.parseInt(availableQuantityField.getText());
                int newTotalQuantity = Integer.parseInt(totalQuantityField.getText());

                // Kiểm tra nếu availableQuantity hoặc totalQuantity < 0
                if (newAvailableQuantity < 0 || newTotalQuantity < 0) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Số lượng sách không được nhỏ hơn 0!");
                    alert.showAndWait();
                    return;
                }

                // Kiểm tra nếu availableQuantity > totalQuantity
                if (newAvailableQuantity > newTotalQuantity) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Lỗi");
                    alert.setHeaderText(null);
                    alert.setContentText("Số lượng có sẵn không được lớn hơn tổng số lượng!");
                    alert.showAndWait();
                    return;
                }

                // Cập nhật dữ liệu của đối tượng trong TableView
                selectedBook.setTitle(newTitle);
                selectedBook.setAuthor(newAuthor);
                selectedBook.setCategory(newCategory);
                selectedBook.setAvailableQuantity(newAvailableQuantity);
                selectedBook.setTotalQuantity(newTotalQuantity);

                // Cập nhật trong cơ sở dữ liệu
                BookService.updateBook(selectedBook);

                // Xóa dữ liệu trong các trường nhập liệu
                clearFields();

                // Làm mới TableView để hiển thị dữ liệu cập nhật
                bookTable.refresh();

                // Thông báo thành công
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thành công");
                alert.setHeaderText(null);
                alert.setContentText("Cập nhật sách thành công!");
                alert.showAndWait();

            } catch (NumberFormatException e) {
                // Hiển thị thông báo lỗi nếu nhập sai định dạng số
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi nhập liệu");
                alert.setHeaderText(null);
                alert.setContentText("Số lượng và Tổng số lượng phải là số nguyên!");
                alert.showAndWait();
            } catch (Exception e) {
                // Hiển thị thông báo lỗi nếu có lỗi bất ngờ
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Đã xảy ra lỗi khi cập nhật sách!");
                alert.showAndWait();
            }
        } else {
            // Thông báo nếu không chọn sách nào
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Không có lựa chọn");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn một cuốn sách để sửa!");
            alert.showAndWait();
        }
    }


    @FXML
    public void deleteBook(ActionEvent event) {
        // Lấy đối tượng được chọn trong TableView
        Book selectedBook = bookTable.getSelectionModel().getSelectedItem();

        if (selectedBook != null) {
            // Hiển thị thông báo xác nhận xóa
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Xác nhận xóa");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Bạn có chắc chắn muốn xóa sách này?");
            Optional<ButtonType> result = confirmAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    // Gọi phương thức xóa trong BookService
                    BookService.deleteBook(selectedBook.getId());

                    // Xóa đối tượng khỏi danh sách trong TableView
                    bookList.remove(selectedBook);

                    // Xóa dữ liệu trong các trường nhập liệu
                    clearFields();

                    // Làm mới lại danh sách ID trong cơ sở dữ liệu
                    BookService.reorderBookIds();

                    // Cập nhật lại danh sách trong TableView
                    bookTable.setItems(FXCollections.observableArrayList(BookService.getAllBooks()));

                    // Thông báo xóa thành công
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Thành công");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Sách đã được xóa thành công!");
                    successAlert.showAndWait();

                } catch (Exception e) {
                    e.printStackTrace();
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Lỗi");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Đã xảy ra lỗi khi xóa sách!");
                    errorAlert.showAndWait();
                }
            }
        } else {
            // Thông báo nếu không chọn đối tượng nào
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Không có lựa chọn");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn một cuốn sách để xóa!");
            alert.showAndWait();
        }
    }

    // Chuyển về Trang chủ
    public void switchToHome(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("home.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // Hàm chuyển sang mục Quản lý nguoi muon
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
            Scene scene = new Scene(root);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

}
