module com.example.catfox {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.catfox to javafx.fxml;
    exports com.example.catfox;
}