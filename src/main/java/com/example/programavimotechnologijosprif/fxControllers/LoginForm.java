package com.example.programavimotechnologijosprif.fxControllers;
//TODO
//validateAndLoad turi patikrinti ar egzistuoja toks vartotojas, ar jo loginas ir pw teisingi
// padaryk registracija
import com.example.programavimotechnologijosprif.HelloApplication;
import com.example.programavimotechnologijosprif.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

public class LoginForm {
    public TextField loginField;
    public PasswordField passwordField;
    public Text messageField;

    final String databasePath = "/Users/mykolastauras/intellijProjects/ProgramavimoTechnologijosPRIf/src/main/resources/com/example/programavimotechnologijosprif/database.txt"; // delete
    AppUser appUser; // delete
    Driver driver;
    Restaurant restaurant;
    Admin admin;
    ArrayList<User> allUsers = new ArrayList<>();
    private void getAvailabeUsers(){ // delete
        try(ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(new FileInputStream(databasePath))
        )){
            /*
            appUser = (AppUser) in.readObject();
            driver = (Driver) in.readObject();
            restaurant = (Restaurant) in.readObject();
            admin = (Admin) in.readObject();
            */
            try{
                int i=0;
                while(true){
                    allUsers.add((User)in.readObject());
                    System.out.println("Read user: " + allUsers.get(i).getName());
                    i++;
                }
            } catch(EOFException e){
                System.out.println("Done reading");
            }
            //for(int i=0;i<4;i++))allUsers.add((User)in.readObject());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void validateAndLoad(ActionEvent actionEvent) throws IOException {
        getAvailabeUsers();
        for(User user : allUsers){
            if(user.getLogin().equals(loginField.getText()) &&
                    user.getPassword().equals(passwordField.getText())){
                System.out.println("You're in!\n");

                // if and only if crediantials are okay!!!!
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-form.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Stage stage = (Stage) passwordField.getScene().getWindow();
                stage.setTitle("Main Form");
                stage.setScene(scene);
                stage.show();
            }
        }
        //failed
        messageField.setVisible(true);
        //UNCOMMENT AppUser appUser = new AppUser(loginField.getText(), passwordField.getText());
        //UNCOMMENT System.out.println(appUser);
        /*
        if(appUser.getLogin().equals(loginField.getText()) &&
                appUser.getPassword().equals(passwordField.getText())){
            System.out.println("You're in!\n");

            // if and only if crediantials are okay!!!!
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-form.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) passwordField.getScene().getWindow();
            stage.setTitle("Main Form");
            stage.setScene(scene);
            stage.show();
        } else {
            // show smth like your password or username was incorrect...
            messageField.setVisible(true);
        }
         */
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
