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
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

public class HelloApplication extends Application {
    final String databasePath = "/Users/mykolastauras/intellijProjects/ProgramavimoTechnologijosPRIf/src/main/resources/com/example/programavimotechnologijosprif/database.txt"; // delete
    private AppUser createDefaultAppUser(){ // delete
        AppUser appUser = new AppUser("appUser", "appUser", "appUser", "appUser", "appUser", "appUser");
        return appUser;
    }
    private Driver createDefaultDriver(){
        Driver driver = new Driver("driver", "driver", "driver", "driver", "driver", "driver", "driver", LocalDate.of(2003, Month.SEPTEMBER, 20));
        return driver;
    }
    private Restaurant createDefaultRestaurant(){
        Restaurant restaurant = new Restaurant("restaurant", "restaurant", "restaurant", "restaurant", "restaurant", "restaurant", "restaurant", new ArrayList<>());
        return restaurant;
    }
    private Admin createDefaultAdmin(){
        Admin admin = new Admin("admin", "admin", "admin", "admin", "admin");
        return admin;
    }
    private void writeObjectsToFile(AppUser appUser, Driver driver, Restaurant restaurant, Admin admin) { // delete
        //if (!(appUser instanceof Serializable))throw new IllegalArgumentException("Object must implement Serializable interface");
        try (ObjectOutputStream out = new ObjectOutputStream(
                new BufferedOutputStream(new FileOutputStream(databasePath)))){
            out.writeObject(appUser);
            out.writeObject(driver);
            out.writeObject(restaurant);
            out.writeObject(admin);
            System.out.println("Object successfully written to: " + databasePath);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public void start(Stage stage) throws IOException {
        AppUser appUser = createDefaultAppUser(); // delete
        Driver driver = createDefaultDriver();
        Restaurant restaurant = createDefaultRestaurant();
        Admin admin = createDefaultAdmin();
        writeObjectsToFile(appUser, driver, restaurant, admin); // delete

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-form.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}
