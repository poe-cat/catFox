package com.example.catfox;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterController implements Initializable {

    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;

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

    @FXML
    private Label searchLabel;
    @FXML
    private TextField searchTextField;

    @FXML
    private TableView<Person> tableData;

    @FXML
    private TableColumn <Person, String> firstnameColumn;
    @FXML
    private TableColumn <Person, String> lastnameColumn;
    @FXML
    private TableColumn <Person, String> usernameColumn;
    @FXML
    private TableColumn <Person, String> passwordColumn;


    private ObservableList<Person> personObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        File catFile = new File("src/main/resources/com/example/catfox/images/horn.png");
        Image catImage = new Image(catFile.toURI().toString());
        catImageView.setImage(catImage);
        tableData.setEditable(true);

        DatabaseConnection connectNow = new DatabaseConnection();
        connection = connectNow.getConnection();

        String personViewQuery = "SELECT firstname, lastname, username, password FROM demo_db.useraccount";

        try {
            Statement statement = connection.createStatement();
            ResultSet queryOutput = statement.executeQuery(personViewQuery);

            while(queryOutput.next()) {

                String queryFirstname = queryOutput.getString("firstname");
                String queryLastname = queryOutput.getString("lastname");
                String queryUsername = queryOutput.getString("username");
                String queryPassword = queryOutput.getString("password");

                personObservableList.add(new Person(queryFirstname, queryLastname,
                        queryUsername, queryPassword));

            }

            firstnameColumn.setCellValueFactory(new PropertyValueFactory<>("firstname"));
            lastnameColumn.setCellValueFactory(new PropertyValueFactory<>("lastname"));
            usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
            passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));

            tableData.setItems(personObservableList);

            // searching database by keywords
            FilteredList<Person> filteredList =
                    new FilteredList<>(personObservableList, b -> true);


            searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredList.setPredicate(person -> {

                    if(newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }

                    String searchKeyword = newValue.toLowerCase();

                    if(person.getFirstname().toLowerCase().contains(searchKeyword)) {
                        return true;
                    } else if(person.getLastname().toLowerCase().contains(searchKeyword)) {
                        return true;
                    }else if(person.getUsername().toLowerCase().contains(searchKeyword)) {
                        return true;
                    } else if(person.getPassword().toLowerCase().contains(searchKeyword)) {
                        return true;
                    } else
                        return false;

                });
            });

            SortedList<Person> sortedList = new SortedList<>(filteredList);

            sortedList.comparatorProperty().bind(tableData.comparatorProperty());

            tableData.setItems(sortedList);


        } catch(SQLException e) {
            Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, e);
            e.printStackTrace();
        }
    }

//TODO: issue with incomplete registration
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

    public void closeButtonOnAction(ActionEvent event) {
        Alert alert = new Alert (Alert.AlertType.CONFIRMATION,"", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText("Are you sure you want to quit?");
        alert.showAndWait();

        if(alert.getResult() == ButtonType.YES) {
            Stage stage = (Stage) closeButton.getScene().getWindow();
            stage.close();
            Platform.exit();
        }
        else alert.close();
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
            refreshTable();

        } catch(Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    @FXML
    private void refreshTable() {
        try {
            personObservableList.clear();

            query = "SELECT * FROM `useraccount`";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                personObservableList.add(new Person(
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"),
                        resultSet.getString("username"),
                        resultSet.getString("password")));
                tableData.setItems(personObservableList);

            }

        } catch (SQLException ex) {
            Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}



