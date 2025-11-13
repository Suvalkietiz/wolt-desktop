package com.example.programavimotechnologijosprif.fxControllers;

import com.example.programavimotechnologijosprif.hibernateControllers.CustomHibernate;
import com.example.programavimotechnologijosprif.hibernateControllers.GenericHibernate;
import com.example.programavimotechnologijosprif.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ChatForm {
    private EntityManagerFactory entityManagerFactory;
    private GenericHibernate genericHibernate;
    private User loggedUser;

    @FXML public ListView<Message> messagesList;

    @FXML public ComboBox<User> userSelectBox;
    @FXML public ComboBox<Chat> chatSelectBox;

    @FXML public Button deleteChatButton;
    @FXML public Button deleteMessageButton;
    @FXML public Button sendMessageButton;

    @FXML public TextField senderField;
    @FXML public TextField receiverField;
    @FXML public TextField orderIdField;
    @FXML public TextField timeSendField;
    @FXML public TextField messageField;
    @FXML public TextField autorField;

    @FXML public HBox autorRow;
    @FXML public HBox timeSendRow;

    public void setData(EntityManagerFactory entityManagerFactory, User loggedUser){
        this.entityManagerFactory = entityManagerFactory;
        genericHibernate = new GenericHibernate(entityManagerFactory);
        this.loggedUser = loggedUser;
    }

    public void initializeReadUI(){
        if(loggedUser == null){
            System.out.println("User is null. Don't know what UI to initialize");
        } else {
            // bendras UI
            sendMessageButton.setVisible(false);

            disableTextFields();

            if(loggedUser.isAdmin()){
                initializeAdminUI();
            } else {
                initializeRestaurantUI();
            }
        }
    }




    private List<Chat> getUserListChat(User user){
        List<Chat> allUserChats = new ArrayList<>();
        List<FoodOrder> allOrders = null;
        CustomHibernate customHibernate = new CustomHibernate(entityManagerFactory);

        if(user == null){
            return allUserChats;
        } else if(user instanceof AppUser){
            allOrders = customHibernate.getAppUserOrders((AppUser) user);
        } else if(user instanceof Driver){
            allOrders = customHibernate.getDriverOrders((Driver) user);
        } else if(user instanceof Restaurant){
            allOrders = customHibernate.getRestaurantOrders((Restaurant) user);
        }

        if(allOrders == null){
            return allUserChats;
        }
        for(FoodOrder foodOrder : allOrders)
            if(foodOrder.getChat() != null)allUserChats.add(foodOrder.getChat());
        return allUserChats;
    }


    // admin only
    @FXML public void selectUser() {
        User selectedUser = (User) userSelectBox.getSelectionModel().getSelectedItem();
        setChatSelectBox(selectedUser);

    }




    //==============================================================================
    //$$$$$$$$$$$$$$$$$$$$$$$$$ UI INITILIZATION $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    //==============================================================================

    private void initializeRestaurantUI(){
        userSelectBox.setVisible(false);
        deleteChatButton.setVisible(false);
        deleteMessageButton.setVisible(false);
        setChatSelectBox(loggedUser);
    }

    private void initializeAdminUI(){
        userSelectBox.setVisible(true);
        deleteChatButton.setVisible(true);
        deleteMessageButton.setVisible(true);
        setUserSelectBox();
    }


    private void setUserSelectBox(){
        List<User> allUsers = genericHibernate.getAllRecords(User.class);

        userSelectBox.getItems().clear();
        chatSelectBox.getItems().clear();
        userSelectBox.getItems().addAll(allUsers);

    }

    private void setChatSelectBox(User user){
        if(user == null){
            chatSelectBox.getItems().clear();
        }
        List<Chat> allChats = getUserListChat(user);
        chatSelectBox.getItems().clear();
        chatSelectBox.getItems().addAll(allChats);
    }


    private void disableTextFields(){
        senderField.setDisable(true);
        receiverField.setDisable(true);
        orderIdField.setDisable(true);
        timeSendField.setDisable(true);
        messageField.setDisable(true);
        autorField.setDisable(true);
    }


    public void initializeWriteUI(FoodOrder foodOrder){
        initializeRestaurantUI();
        disableTextFields();
        messageField.setDisable(false);
        chatSelectBox.setVisible(false);
        timeSendRow.setVisible(false);
        autorRow.setVisible(false);
        sendMessageButton.setVisible(true);

        senderField.setText(String.valueOf(foodOrder.getRestaurant()));
        receiverField.setText(String.valueOf(foodOrder.getDriver()));
        orderIdField.setText(String.valueOf(foodOrder.getId()));


    }
}
