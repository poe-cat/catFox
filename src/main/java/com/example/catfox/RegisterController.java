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
import java.sql.Statement;
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
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField usernameTextField;


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

        String firstname = firstNameTextField.getText();
        String lastname = lastNameTextField.getText();
        String username = usernameTextField.getText();
        String password = setpasswordField.getText();

        String insertFields = "INSERT INTO demo_db.useraccount (firstname, lastname, username, password) VALUES ('";
        String insertValues = firstname + "','" + lastname + "','" + username + "','" + password + "')";
        String insertToRegister = insertFields + insertValues;


        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(insertToRegister);
            registrationMessageLabel.setText("User registered successfully!");

        } catch(Exception e) {
            e.printStackTrace();
            e.getCause();
        }


    }
}
