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

public class BorrowerManagementController {
    Stage stage;
    Scene scene;
    Parent root;

    @FXML
    private TextField fullNameField;
    @FXML
    private TextField ageField;
    @FXML
    private TextField phoneNumberField;

    @FXML
    private TableView<Borrower> borrowerTable;
    @FXML
    private TableColumn<Borrower, Integer> idColumn;
    @FXML
    private TableColumn<Borrower, String> fullNameColumn;
    @FXML
    private TableColumn<Borrower, Integer> ageColumn;
    @FXML
    private TableColumn<Borrower, String> phoneNumberColumn;

    private ObservableList<Borrower> borrowerList;

    public void initialize() {
        // Tải dữ liệu từ cơ sở dữ liệu và gắn vào TableView
        borrowerList = FXCollections.observableArrayList(BorrowerService.getAllBorrowers());
        borrowerTable.setItems(borrowerList);

        // Liên kết cột với các thuộc tính của lớp Borrower
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        // Lắng nghe sự kiện chọn trong bảng
        borrowerTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                showBorrowerDetails(newValue);
            }
        });
    }

    // Phương thức hiển thị chi tiết Borrower lên TextField
    private void showBorrowerDetails(Borrower borrower) {
        fullNameField.setText(borrower.getFullName());
        ageField.setText(String.valueOf(borrower.getAge()));
        phoneNumberField.setText(borrower.getPhoneNumber());
    }

    // Hàm thêm 1 người mượn
    @FXML
    private void addBorrower() {
        try {
            // Kiểm tra nếu các trường không rỗng
            if (fullNameField.getText().isEmpty() || ageField.getText().isEmpty() || phoneNumberField.getText().isEmpty()) {
                // Hiển thị cảnh báo nếu có trường nhập liệu rỗng
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText(null);
                alert.setContentText("Vui lòng nhập đầy đủ thông tin!");
                alert.showAndWait();
                return;
            }

            // Lấy dữ liệu từ TextField
            String fullName = fullNameField.getText();
            int age = Integer.parseInt(ageField.getText());
            String phoneNumber = phoneNumberField.getText();

            // Kiểm tra số điện thoại hợp lệ
            if (!isValidPhoneNumber(phoneNumber)) {
                // Hiển thị cảnh báo nếu số điện thoại không hợp lệ
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Cảnh báo");
                alert.setHeaderText(null);
                alert.setContentText("Số điện thoại phải có 10 chữ số và bắt đầu bằng số 0!");
                alert.showAndWait();
                return;
            }

            // Tạo một đối tượng Borrower mới
            Borrower newBorrower = new Borrower(fullName, age, phoneNumber);

            // Thêm người mượn vào cơ sở dữ liệu
            BorrowerService.addBorrower(newBorrower);

            // Làm mới bảng để hiển thị người mượn mới
            borrowerList.setAll(BorrowerService.getAllBorrowers());  // Lấy lại toàn bộ người mượn từ cơ sở dữ liệu
            borrowerTable.setItems(borrowerList);  // Cập nhật lại TableView

            // Xóa dữ liệu trong các trường nhập liệu
            clearFields();

            // Hiển thị thông báo thành công
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Thành công");
            successAlert.setHeaderText(null);
            successAlert.setContentText("Thêm người mượn thành công!");
            successAlert.showAndWait();

        } catch (NumberFormatException e) {
            // Hiển thị lỗi nếu nhập sai định dạng số
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng nhập đúng định dạng số cho tuổi!");
            alert.showAndWait();
        } catch (Exception e) {
            // Hiển thị lỗi nếu có lỗi bất ngờ
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText(null);
            alert.setContentText("Đã xảy ra lỗi khi thêm người mượn!");
            alert.showAndWait();
        }
    }

    // Hàm kiểm tra SDT hợp lệ
    private boolean isValidPhoneNumber(String phoneNumber) {
        // Kiểm tra số điện thoại có đúng 10 chữ số và bắt đầu bằng số 0
        return phoneNumber != null && phoneNumber.matches("^0\\d{9}$");
    }

    // Xóa dữ liệu các textField
    private void clearFields() {
        fullNameField.clear();
        ageField.clear();
        phoneNumberField.clear();
    }

    @FXML
    public void updateBorrower(ActionEvent event) {
        // Lấy đối tượng được chọn trong TableView
        Borrower selectedBorrower = borrowerTable.getSelectionModel().getSelectedItem();

        if (selectedBorrower != null) {
            try {
                // Kiểm tra các TextField có rỗng không
                if (fullNameField.getText().isEmpty() || ageField.getText().isEmpty() || phoneNumberField.getText().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Cảnh báo");
                    alert.setHeaderText(null);
                    alert.setContentText("Vui lòng điền đầy đủ thông tin trước khi sửa!");
                    alert.showAndWait();
                    return;
                }

                // Lấy dữ liệu mới từ các TextField
                String newFullName = fullNameField.getText();
                int newAge = Integer.parseInt(ageField.getText());
                String newPhoneNumber = phoneNumberField.getText();

                // Kiểm tra số điện thoại hợp lệ
                if (!isValidPhoneNumber(newPhoneNumber)) {
                    // Hiển thị cảnh báo nếu số điện thoại không hợp lệ
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Cảnh báo");
                    alert.setHeaderText(null);
                    alert.setContentText("Số điện thoại phải có 10 chữ số và bắt đầu bằng số 0!");
                    alert.showAndWait();
                    return;
                }

                // Cập nhật dữ liệu của đối tượng trong TableView
                selectedBorrower.setFullName(newFullName);
                selectedBorrower.setAge(newAge);
                selectedBorrower.setPhoneNumber(newPhoneNumber);

                // Cập nhật trong cơ sở dữ liệu
                BorrowerService.updateBorrower(selectedBorrower);

                // Xóa dữ liệu trong các trường nhập liệu
                clearFields();

                // Làm mới TableView để hiển thị dữ liệu cập nhật
                borrowerTable.refresh();

                // Thông báo thành công
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Thành công");
                alert.setHeaderText(null);
                alert.setContentText("Cập nhật người mượn thành công!");
                alert.showAndWait();

            } catch (NumberFormatException e) {
                // Hiển thị thông báo lỗi nếu nhập sai định dạng số
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi nhập liệu");
                alert.setHeaderText(null);
                alert.setContentText("Tuổi phải là số nguyên!");
                alert.showAndWait();
            } catch (Exception e) {
                // Hiển thị thông báo lỗi nếu có lỗi bất ngờ
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Lỗi");
                alert.setHeaderText(null);
                alert.setContentText("Đã xảy ra lỗi khi cập nhật người mượn!");
                alert.showAndWait();
            }
        } else {
            // Thông báo nếu không chọn người mượn nào
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Không có lựa chọn");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn một người mượn để sửa!");
            alert.showAndWait();
        }
    }


    @FXML
    public void deleteBorrower(ActionEvent event) {
        // Lấy đối tượng borrower được chọn trong TableView
        Borrower selectedBorrower = borrowerTable.getSelectionModel().getSelectedItem();

        if (selectedBorrower != null) {
            // Hiển thị thông báo xác nhận xóa
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Xác nhận xóa");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Bạn có chắc chắn muốn xóa người mượn này?");
            Optional<ButtonType> result = confirmAlert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    // Gọi phương thức xóa trong BorrowerService
                    BorrowerService.deleteBorrower(selectedBorrower.getId());

                    // Xóa borrower khỏi danh sách trong TableView
                    borrowerList.remove(selectedBorrower);

                    // Xóa dữ liệu trong các trường nhập liệu (nếu có)
                    clearFields();

                    // Làm mới lại danh sách ID trong cơ sở dữ liệu nếu cần thiết
                    BorrowerService.reorderBorrowerIds();

                    // Cập nhật lại danh sách trong TableView
                    borrowerTable.setItems(FXCollections.observableArrayList(BorrowerService.getAllBorrowers()));

                    // Thông báo xóa thành công
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Thành công");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Người mượn đã được xóa thành công!");
                    successAlert.showAndWait();

                } catch (Exception e) {
                    e.printStackTrace();
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Lỗi");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Đã xảy ra lỗi khi xóa người mượn!");
                    errorAlert.showAndWait();
                }
            }
        } else {
            // Thông báo nếu không chọn đối tượng nào
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Không có lựa chọn");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn một người mượn để xóa!");
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
    // Hàm chuyển sang mục Quản lý sách
    public void switchToQuanLySach (ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("BookManagement.fxml"));
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
