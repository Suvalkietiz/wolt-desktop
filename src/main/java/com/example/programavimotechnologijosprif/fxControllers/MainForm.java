package com.example.programavimotechnologijosprif.fxControllers;

import com.example.programavimotechnologijosprif.Utils.FxUtils;
import com.example.programavimotechnologijosprif.hibernateControllers.GenericHibernate;
import com.example.programavimotechnologijosprif.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;

import java.util.List;

public class MainForm {
    @FXML public TabPane adminPane;
    @FXML public TabPane restaurantPane;
    @FXML public TableColumn idColumn;

    private EntityManagerFactory entityManagerFactory;
    private GenericHibernate genericHibernate;

    private User user;

    private void loadAllUsers(){
        List<User> users = genericHibernate.getAllRecords(User.class);
        for(User user : users)System.out.println(user.getName()); // pratesk mane...
    }





    public void setData(EntityManagerFactory entityManagerFactory){
        this.entityManagerFactory = entityManagerFactory;
        genericHibernate = new GenericHibernate(entityManagerFactory);
    }
    public void setRole(User user){
        if(user != null) {
            this.user = user;
            if (user.isAdmin()) {
                adminPane.setVisible(true);
                restaurantPane.setVisible(false);
                loadAllUsers();
            } else {
                adminPane.setVisible(false);
                restaurantPane.setVisible(true);
            }
        } else {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Null user", "In MainForm.java method setRole(User user) you passed a null user");
        }
    }








}
