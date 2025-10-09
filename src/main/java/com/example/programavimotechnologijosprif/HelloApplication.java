package com.example.programavimotechnologijosprif;

import com.example.programavimotechnologijosprif.model.AppUser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;

public class HelloApplication extends Application {
    final String databasePath = "/Users/mykolastauras/intellijProjects/ProgramavimoTechnologijosPRIf/src/main/resources/com/example/programavimotechnologijosprif/database.txt"; // delete
    private AppUser createDefaultAppUser(){ // delete
        AppUser appUser = new AppUser("appUser", "appUser", "appUser", "appUser", "appUser", "appUser");
        return appUser;
    }
    private void writeObjectToFile(Object object) { // delete
        if (!(object instanceof Serializable)) {
            throw new IllegalArgumentException("Object must implement Serializable interface");
        }
        try (ObjectOutputStream out = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(databasePath)))){
            out.writeObject(object);
            System.out.println("Object successfully written to: " + databasePath);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public void start(Stage stage) throws IOException {
        AppUser appUser = createDefaultAppUser(); // delete
        writeObjectToFile(appUser); // delete


        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-form.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
