package com.example.programavimotechnologijosprif;

import com.example.programavimotechnologijosprif.model.Admin;
import com.example.programavimotechnologijosprif.model.AppUser;
import com.example.programavimotechnologijosprif.model.Driver;
import com.example.programavimotechnologijosprif.model.Restaurant;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;


public class HelloApplication extends Application {


    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-form.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Login Form");
        stage.setScene(scene);
        stage.show();
    }
}
