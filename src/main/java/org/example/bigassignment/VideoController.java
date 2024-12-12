package org.example.bigassignment;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class VideoController implements Initializable {

    @FXML
    private MediaView mediaView; // Được kết nối với MediaView trong FXML

    private MediaPlayer mediaPlayer;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // Đảm bảo video của bạn có đường dẫn đúng
        String videoPath = getClass().getResource("/org/example/bigassignment/testvid3.mp4").toExternalForm(); // Đường dẫn đến video
        Media media = new Media(videoPath);
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);

        // Phát video
        mediaPlayer.play();
        // Xử lý khi video kết thúc
        mediaPlayer.setOnEndOfMedia(() -> {
            // Chuyển sang InterfaceScene.fxml sau khi video kết thúc
            mediaPlayer.dispose();
            transitionToNextScene();
        });
    }

    // Chuyển sang scene tiếp theo (InterfaceScene.fxml)
    private void transitionToNextScene() {
        try {
            // Tải FXML của InterfaceScene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
            Scene nextScene = new Scene(loader.load());

            // Lấy Stage hiện tại và thay đổi Scene
            Stage currentStage = (Stage) mediaView.getScene().getWindow();
            currentStage.setScene(nextScene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
