package com.example.catfox;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private ImageView catImageView;
    @FXML
    private Button closeButton;
    @FXML
    private Label registrationMessageLabel;
    @FXML
    private PasswordField setpasswordField;
    @FXML
    private PasswordField confirmpasswordField;
    @FXML
    private Label confirmPasswordLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File catFile = new File("src/main/resources/com/example/catfox/images/cat.png");
        Image catImage = new Image(catFile.toURI().toString());
        catImageView.setImage(catImage);
    }

    public void registerButtonOnAction(ActionEvent event) {

        if(setpasswordField.getText().equals(confirmpasswordField.getText())) {
            registerUser();
            confirmPasswordLabel.setText("");
            registrationMessageLabel.setText("User registered successfully!");
        } else {
            confirmPasswordLabel.setText("Password doesn't match!");
        }
    }


    public void closeButtonOnAction(ActionEvent event) {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
        Platform.exit();
    }

    public void registerUser() {

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();



    }
}
