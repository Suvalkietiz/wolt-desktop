package com.example.programavimotechnologijosprif.fxControllers;
//TODO
//validateAndLoad turi patikrinti ar egzistuoja toks vartotojas, ar jo loginas ir pw teisingi
// padaryk registracija
import com.example.programavimotechnologijosprif.HelloApplication;
import com.example.programavimotechnologijosprif.model.AppUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class LoginForm {
    public TextField loginField;
    public PasswordField passwordField;


    final String databasePath = "/Users/mykolastauras/intellijProjects/ProgramavimoTechnologijosPRIf/src/main/resources/com/example/programavimotechnologijosprif/database.txt"; // delete
    AppUser appUser; // delete
    private void getAvailabeUsers(){ // delete
        try(ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(databasePath))
        )){
            appUser = (AppUser) in.readObject();
            // ...
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void validateAndLoad(ActionEvent actionEvent) throws IOException {
        getAvailabeUsers();
        //UNCOMMENT AppUser appUser = new AppUser(loginField.getText(), passwordField.getText());
        //UNCOMMENT System.out.println(appUser);
        if(appUser.getLogin().equals(loginField.getText()) &&
                appUser.getPassword().equals(passwordField.getText())){
            System.out.println("You're in!\n");
        } else {
            // do not let him...
        }



        // if and only if crediantials are okay!!!!
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-form.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = (Stage) passwordField.getScene().getWindow();
        stage.setTitle("Main Form");
        stage.setScene(scene);
        stage.show();
    }

    public void registerNewUser(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user-form.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stage.setTitle("User Form");
        stage.setScene(scene);
        stage.show();
    }
}
