package com.example.programavimotechnologijosprif.fxControllers;

import com.example.programavimotechnologijosprif.Utils.FxUtils;
import com.example.programavimotechnologijosprif.hibernateControllers.GenericHibernate;
import com.example.programavimotechnologijosprif.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.List;

public class MainForm {
    @FXML public TabPane adminPane;
    @FXML public TabPane restaurantPane;
    @FXML public TableColumn idColumn;
    @FXML public ListView<User> allUsersList;
    public TextField loginField;

    private EntityManagerFactory entityManagerFactory;
    private GenericHibernate genericHibernate;

    private User user;

    private void loadAllUsers(){
        List<User> users = genericHibernate.getAllRecords(User.class);
        allUsersList.getItems().addAll(users);
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


    public void loadUserData(MouseEvent mouseEvent) {
        User user1 = allUsersList.getSelectionModel().getSelectedItem();
        loginField.setText(user1.getLogin());

    }

    public void deleteUser(ActionEvent actionEvent) {
        User user1 = allUsersList.getSelectionModel().getSelectedItem();
        //genericHibernate.deleteEntity(user1); neveikia
        genericHibernate.deleteEntityById(User.class, user1.getId());

    }
}
