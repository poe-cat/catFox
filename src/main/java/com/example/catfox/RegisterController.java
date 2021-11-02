package com.example.catfox;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class RegisterController implements Initializable {

    @FXML
    private ImageView catImageView;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        File catFile = new File("src/main/resources/com/example/catfox/images/cat.png");
        Image catImage = new Image(catFile.toURI().toString());
        catImageView.setImage(catImage);
    }
}
