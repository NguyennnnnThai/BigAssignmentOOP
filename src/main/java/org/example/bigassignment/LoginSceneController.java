package org.example.bigassignment;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
//import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.io.IOException;
import java.util.ResourceBundle;


public class LoginSceneController implements Initializable {

    @FXML
    TextField usernameTextField;

    @FXML
    PasswordField passwordTextField;

    @FXML
    private Button exitButton;

    @FXML
    private Label loginMessageLabel;

    @FXML
    private TextField passwordVisibleTextField;

    @FXML
    private FontAwesomeIconView iconView;

    @FXML
    private ImageView animationImageView;

    @FXML
    private AnchorPane rootPane;

    private boolean isPasswordVisible = false;
    private int frameIndex = 0;
    private int frameCounter = 0;
    private boolean isPlayingForward = true;
    private int frameDelay = 1;
    private AnimationTimer animationTimer;
    private Image[] firstSetImages;
    private Image[] secondSetImages;

    //  private boolean isPasswordVisible = false;

    private Stage stage;
    private Scene scene;
    private Parent root;

    private void loadImages() {
        String basePath = "/org/example/bigassignment/Image/";
        firstSetImages = new Image[]{
                new Image(getClass().getResourceAsStream(basePath + "loginIMG_000030.jpg")),
                new Image(getClass().getResourceAsStream(basePath + "loginIMG_000031.jpg")),
                new Image(getClass().getResourceAsStream(basePath + "loginIMG_000032.jpg")),
                new Image(getClass().getResourceAsStream(basePath + "loginIMG_000033.jpg")),
                new Image(getClass().getResourceAsStream(basePath + "loginIMG_000034.jpg")),
                new Image(getClass().getResourceAsStream(basePath + "loginIMG_000035.jpg")),
                new Image(getClass().getResourceAsStream(basePath + "loginIMG_000036.jpg")),
                new Image(getClass().getResourceAsStream(basePath + "loginIMG_000037.jpg")),
                new Image(getClass().getResourceAsStream(basePath + "loginIMG_000038.jpg")),
                new Image(getClass().getResourceAsStream(basePath + "loginIMG_000039.jpg")),
                new Image(getClass().getResourceAsStream(basePath + "loginIMG_000040.jpg")),
                new Image(getClass().getResourceAsStream(basePath + "loginIMG_000041.jpg")),
                // Tiếp tục thêm các ảnh cho nhóm đầu tiên (15-20 ảnh đầu tiên)

        };
    }

    private void setupListeners() {
        passwordTextField.setOnMouseClicked(event -> onPasswordFieldClick());
//        iconView.setOnMouseClicked(event -> onShowPasswordClick());

        rootPane.setOnMouseClicked(event -> {
            if (!passwordTextField.isFocused() && animationTimer != null) {
                reverseAnimation(); // Đảo ngược khi nhấp ra ngoài vùng password
            }
        });

        passwordTextField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused && animationTimer != null) {
                reverseAnimation();
            }
        });
    }

    private void startAnimation(Image[] images) {
        if (animationTimer != null) {
            animationTimer.stop();
        }

        frameIndex = 0;
        frameCounter = 0;
        isPlayingForward = true;

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (frameCounter >= frameDelay) {
                    animationImageView.setImage(images[frameIndex]);
                    frameIndex += isPlayingForward ? 1 : -1;

                    if (frameIndex >= images.length) {
                        frameIndex = images.length - 1;
                        isPlayingForward = false;
                        stop();
                    } else if (frameIndex < 0) {
                        frameIndex = 0;
                        isPlayingForward = true;
                        stop(); // Dừng khi quay ngược về đầu
                    }
                    frameCounter = 0;
                } else {
                    frameCounter++;
                }
            }
        };
        animationTimer.start();
    }

    private void reverseAnimation() {
        //isPlayingForward = false;
        if (animationTimer != null) {
            animationTimer.start();
        }
    }

    @FXML
    private void onPasswordFieldClick() {
        if (animationTimer != null && frameIndex != 0 && (isPlayingForward || !isPlayingForward)) {
            return; // Không khởi động lại animation nếu nó đã chạy
        }
        startAnimation(firstSetImages);
    }

//    @FXML
//    private void onShowPasswordClick() {
//        startAnimation(secondSetImages);
//        togglePasswordVisibility();
//    }

    @FXML
    public void loginButtonAction(ActionEvent event) {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        if (!username.isBlank() && !password.isBlank()) {
            LibrarianDAO userDAO = new LibrarianDAO(); // Hoặc LibrarianDAO nếu dùng cho thủ thư

            boolean isValidUser = LibrarianDAO.validateLibrarianLogin(username, password);
            if (isValidUser) {
                loginMessageLabel.setText("Đăng nhập thành công!");
                loadInterfaceScene(event, username);
            } else {
                loginMessageLabel.setText("Tên đăng nhập hoặc mật khẩu không đúng.");
            }
        } else {
            loginMessageLabel.setText("Vui lòng nhập tên đăng nhập và mật khẩu.");
        }
    }

    @FXML
    public void openRegisterScene(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Register.fxml"));
            root = loader.load();

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            loginMessageLabel.setText("Không thể chuyển sang màn hình đăng ký.");
        }
    }

    @FXML
    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            // Chuyển về ẩn mật khẩu
            passwordTextField.setText(passwordVisibleTextField.getText());
            passwordTextField.setVisible(true);
            passwordVisibleTextField.setVisible(false);
            iconView.setIcon(FontAwesomeIcon.valueOf("EYE_SLASH"));       // Đổi biểu tượng thành "mắt nhắm"
            isPasswordVisible = false;
        } else {
            // Chuyển sang hiển thị mật khẩu
            passwordVisibleTextField.setText(passwordTextField.getText());
            passwordVisibleTextField.setVisible(true);
            passwordTextField.setVisible(false);
            iconView.setIcon(FontAwesomeIcon.valueOf("EYE"));
            isPasswordVisible = true;
        }
    }


    private void loadInterfaceScene(ActionEvent event, String username) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("VideoScene.fxml"));
            root = loader.load();

            VideoController videoController = loader.getController();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            loginMessageLabel.setText("Không thể chuyển sang màn hình chính.");
        }
    }

    public void exit(ActionEvent event) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit");
        alert.setHeaderText("cbi thoat");
        alert.setContentText("muon thoat khong?");

        if (alert.showAndWait().get() == ButtonType.OK) {

            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            System.out.println("Da dang xuat");
            stage.close();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadImages();

        setupListeners();
        String basePath = "/org/example/bigassignment/Image/";

        animationImageView.setImage(new Image(getClass().getResourceAsStream(basePath + "loginIMG_000030.jpg")));
    }
}
