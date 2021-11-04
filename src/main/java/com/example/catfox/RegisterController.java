package com.example.catfox;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterController implements Initializable {

    @FXML
    private ImageView catImageView;
    @FXML
    private Button closeButton;

    @FXML
    private Button deleteButton;

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

    @FXML
    private Label searchLabel;
    @FXML
    private TextField searchTextField;

    @FXML
    private TableView tableData;

    //TODO
    @FXML
    private TableColumn idTableColumn, firstnameTableColumn, lastnameTableColumn, usernameTableColumn, passwordTableColumn;

    private ObservableList<ObservableList> data;
    String SQL = "SELECT * from demo_db.useraccount";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File catFile = new File("src/main/resources/com/example/catfox/images/horn.png");
        Image catImage = new Image(catFile.toURI().toString());
        catImageView.setImage(catImage);
        fetColumnList();
        fetRowList();

        tableData.setEditable(true);
    }


    public void registerButtonOnAction(ActionEvent event) {

        //check if both passwords are the same
        if(setpasswordField.getText().equals(confirmpasswordField.getText())) {
            registerUser();
            confirmPasswordLabel.setText("");

        } else {
            confirmPasswordLabel.setText("Password doesn't match!");
        }

        //check if not empty
        if (firstNameTextField.getText().isEmpty() || lastNameTextField.getText().isEmpty()
                || usernameTextField.getText().isEmpty() || setpasswordField.getText().isEmpty()
                || confirmpasswordField.getText().isEmpty()) {

            registrationMessageLabel.setTextFill(Color.TOMATO);
            registrationMessageLabel.setText("Enter all details");

        } else {
            registerUser();
        }
    }

    public void deleteButtonOnAction(ActionEvent event) {
        deleteUser();
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
            registrationMessageLabel.setTextFill(Color.GREEN);
            registrationMessageLabel.setText("User registered successfully!");
            fetRowList();

        } catch(Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void deleteUser() {

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String insertFields = "DELETE FROM demo_db.useraccount WHERE username='ffsd'";

        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(insertFields);
            registrationMessageLabel.setTextFill(Color.GREEN);
            registrationMessageLabel.setText("User deleted successfully!");
            fetRowList();

        } catch(Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    private void fetColumnList() {

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        try {
            ResultSet rs = connectDB.createStatement().executeQuery(SQL);

            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {

                final int j = i;

                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1).toUpperCase());

                col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>,
                        ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString()));

                tableData.getColumns().removeAll(col);
                tableData.getColumns().addAll(col);

                System.out.println("Column [" + i + "] ");

            }

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    private void fetRowList() {

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        data = FXCollections.observableArrayList();
        ResultSet rs;

        try {
            rs = connectDB.createStatement().executeQuery(SQL);

            while (rs.next()) {
                //iterate row
                ObservableList row = FXCollections.observableArrayList();

                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //iterate column
                    row.add(rs.getString(i));
                }

                System.out.println("Row [1] added " + row);
                data.add(row);
            }
            tableData.setItems(data);

        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
}



