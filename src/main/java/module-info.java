module com.example.catfox {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;


    opens com.example.catfox to javafx.fxml;
    exports com.example.catfox;
}