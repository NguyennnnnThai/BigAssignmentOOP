package org.example.bigassignment;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class InterfaceSceneController {
    @FXML
    Label nameLabel;

    public void displayName(String username) {
        nameLabel = new Label();
        nameLabel.setText("Welcome: " + username);
    }
}
