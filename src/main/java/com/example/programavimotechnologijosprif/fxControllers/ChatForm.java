package com.example.programavimotechnologijosprif.fxControllers;

import com.example.programavimotechnologijosprif.Utils.FxUtils;
import com.example.programavimotechnologijosprif.hibernateControllers.CustomHibernate;
import com.example.programavimotechnologijosprif.hibernateControllers.GenericHibernate;
import com.example.programavimotechnologijosprif.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public class ChatForm {
    private EntityManagerFactory entityManagerFactory;
    private GenericHibernate genericHibernate;
    private User loggedUser;
    private Chat orderChat;
    private User sender;

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
    @FXML public TextField rateField;

    @FXML public HBox autorRow;
    @FXML public HBox rateRow;
    @FXML public HBox senderRow;

    public void setData(EntityManagerFactory entityManagerFactory, User loggedUser){
        this.entityManagerFactory = entityManagerFactory;
        genericHibernate = new GenericHibernate(entityManagerFactory);
        this.loggedUser = loggedUser;
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

        for (FoodOrder foodOrder : allOrders) {
            Chat chat = foodOrder.getChat();
            if (chat == null) continue;

            User driver = chat.getDriver();
            User restaurant = chat.getRestaurant();
            User appUser = chat.getAppUser();
            if(user instanceof AppUser) {
                if (appUser != null) allUserChats.add(chat);
            } else if(user instanceof Driver) {
                if (driver != null) allUserChats.add(chat);
            } else if(user instanceof Restaurant){
                if(restaurant != null)allUserChats.add(chat);
            }

        }


        return allUserChats;
    }


    // admin only
    @FXML public void selectUser() {
        User selectedUser = (User) userSelectBox.getSelectionModel().getSelectedItem();
        setChatSelectBox(selectedUser);
        messagesList.getItems().clear();
        clearChatFields();
        deselectMessage();

        if(isReviewMode()){
            renderReviews(selectedUser);
        }
    }

    private boolean isReviewMode(){
        if(rateRow.isVisible()){
            return true;
        }
        return false;
    }




    //==============================================================================
    //$$$$$$$$$$$$$$$$$$$$$$$$$ UI INITILIZATION $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    //==============================================================================
    public void initializeReviewUI(User currentUser){
        loggedUser = currentUser;
        if(loggedUser.isAdmin()){
            initializeReviewAdminUI();
        } else {
            initializeReviewRestaurantUI();
        }
        // bendras ui
        disableTextFields();
        chatSelectBox.setVisible(false);
        sendMessageButton.setVisible(false);
        senderRow.setVisible(false);
        rateRow.setVisible(true);
    }

    private void generateReview(){
        // pls kviesk tik ten kur restoranas jungiasi
        Review review = new Review();
        review.setTimestamp(LocalDateTime.now());
        review.setText("Isbandau review");
        review.setAutor(genericHibernate.getEntityById(User.class, 22));
        review.setRestaurant((Restaurant) loggedUser);
        review.setRate(7);
        review.setFoodOrder(genericHibernate.getEntityById(FoodOrder.class, 1));
        genericHibernate.createEntity(review, "Review failed", "Review generator failed");
    }




    private void initializeReviewRestaurantUI(){
        //generateReview();
        initializeRestaurantUI();
        renderReviews(loggedUser);
    }
    private void initializeReviewAdminUI(){
        initializeAdminUI();
        deleteChatButton.setVisible(false);
        deleteMessageButton.setText("Delete Review");
    }

    private void renderReviews(User user){
        List<Review> reviews = null;
        CustomHibernate customHibernate = new CustomHibernate(entityManagerFactory);
        if(user instanceof Restaurant){
            reviews = customHibernate.getRestaurantReviews((Restaurant) user);
        } else if(user instanceof Driver){
           reviews = customHibernate.getDriverReviews((Driver) user);
        } else {
            // not sure if appUser and admin will ever have reviews
            reviews = new ArrayList<>();
        }
        messagesList.getItems().clear();
        messagesList.getItems().addAll(reviews);
    }

    private void renderReviewFields(Review review){
        if(review.getRestaurant() != null){
            receiverField.setText(String.valueOf(review.getRestaurant()));
        } else if(review.getDriver() != null){
            receiverField.setText(String.valueOf(review.getDriver()));
        }
        orderIdField.setText(String.valueOf(review.getFoodOrder()));
    }






    private void initializeRestaurantUI(){
        userSelectBox.setVisible(false);
        deleteChatButton.setVisible(false);
        deleteMessageButton.setVisible(false);
        rateRow.setVisible(false);
        setChatSelectBox(loggedUser);
    }

    private void initializeAdminUI(){
        userSelectBox.setVisible(true);
        deleteChatButton.setVisible(true);
        deleteMessageButton.setVisible(true);
        rateRow.setVisible(false);
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
        rateField.setDisable(true);
    }


    public void initializeWriteUI(FoodOrder foodOrder){
        initializeRestaurantUI();
        disableTextFields();
        messageField.setDisable(false);
        chatSelectBox.setDisable(true);
        autorRow.setVisible(false);
        sendMessageButton.setVisible(true);

        senderField.setText(String.valueOf(foodOrder.getRestaurant()));
        receiverField.setText(String.valueOf(foodOrder.getDriver()));
        orderIdField.setText(String.valueOf(foodOrder.getId()));

        orderChat = foodOrder.getChat();
        if(orderChat == null){
            orderChat = new Chat();
            orderChat.setRestaurant(foodOrder.getRestaurant());
            orderChat.setDriver(foodOrder.getDriver());
            orderChat.setFoodOrder(foodOrder);
        } else renderMessages(orderChat);
        chatSelectBox.setValue(orderChat);


    }

    public void initializeReadUI(){
        if(loggedUser == null){
            System.out.println("User is null. Don't know what UI to initialize");
        } else {
            // bendras UI
            sendMessageButton.setVisible(false);
            chatSelectBox.setDisable(false);

            disableTextFields();

            if(loggedUser.isAdmin()){
                initializeAdminUI();
            } else {
                initializeRestaurantUI();
            }
        }
    }
    //------=========------=========------=========------=========------=========
    //------=========
    //------=========------=========------=========------=========------=========
    @FXML public void sendMessage() {
        if(orderChat == null){
            FxUtils.generateAlert(Alert.AlertType.ERROR, "OrderChat", "For some reason your orderChat is null");
            return;
        }
        CustomHibernate customHibernate = new CustomHibernate(entityManagerFactory);

        Message message = new Message();
        message.setText(messageField.getText());
        message.setAutor(loggedUser);
        message.setTimestamp(LocalDateTime.now());
        message.setChat(orderChat);

        orderChat.setMessages(customHibernate.getChatMessages(orderChat));
        if(orderChat.isEmpty(entityManagerFactory)){
            //create new chat and link it with foodOrder
            genericHibernate.createEntity(orderChat, "DB Klaida", "Nepasiseke sukurti nauja chata tavo restoranui..");

            FoodOrder foodOrder = orderChat.getFoodOrder();
            foodOrder.setChat(orderChat);
            genericHibernate.updateEntity(foodOrder);
        }
        genericHibernate.createEntity(message, "DB klaida", "Nepavyko irasyti zinutes i db");
        messageField.clear();
        renderMessages(orderChat);
    }
    private void renderMessages(Chat chat){
        CustomHibernate customHibernate = new CustomHibernate(entityManagerFactory);
        List<Message> messages = customHibernate.getChatMessages(chat);
        sender = messages.getFirst().getAutor();
        messagesList.getItems().clear();
        messagesList.getItems().addAll(messages);
    }

    @FXML public void renderMessageFields() {
        Message selectedMessage = messagesList.getSelectionModel().getSelectedItem();
        if(selectedMessage != null){
            timeSendField.setText(String.valueOf(selectedMessage.getTimestamp()));
            messageField.setText(selectedMessage.getText());
            autorField.setText(String.valueOf(selectedMessage.getAutor()));
        }
        if(selectedMessage instanceof Review){
            rateField.setText(String.valueOf(((Review) selectedMessage).getRate()));
            renderReviewFields((Review)selectedMessage);
        }
    }

    @FXML public void deselectMessage() {
        messagesList.getSelectionModel().clearSelection();
        timeSendField.clear();
        messageField.clear();
        autorField.clear();
        if(isReviewMode()){
            rateField.clear();
            orderIdField.clear();
        }
    }

    @FXML public void selectChat() {
        deselectMessage();
        orderChat = chatSelectBox.getValue();
        if(orderChat != null){
            renderMessages(orderChat);
            renderChatFields(orderChat);
        }

    }

    private void renderChatFields(Chat chat) {
        if(chat.getAppUser() == null){
            // chat between restaurant and driver
            if(sender == null){
                System.out.println("WUTURUWDDUWJUDWUJDW");
                return;
            }
            System.out.println("SENDER = " + sender + "\t" + chat.getRestaurant().toString());
            if(sender.getId() == chat.getRestaurant().getId()){
                senderField.setText(String.valueOf(chat.getRestaurant()));
                receiverField.setText(String.valueOf(chat.getDriver()));
            } else {
                senderField.setText(String.valueOf(chat.getDriver()));
                receiverField.setText(String.valueOf(chat.getRestaurant()));
            }
        } else if(chat.getDriver() == null){
            // chat between restaurant and appUser
            //...
        } else if(chat.getRestaurant() == null){
            // chat between appUser and driver
            //...
        }
        orderIdField.setText(String.valueOf(chat.getFoodOrder().getId()));


    }


    @FXML public void deleteMessage() {
        Message selectedMessage = messagesList.getSelectionModel().getSelectedItem();
        Chat selectedMessageChat = selectedMessage.getChat();
        genericHibernate.deleteEntityById(Message.class, selectedMessage.getId());

        if(isReviewMode()){
            renderReviews(userSelectBox.getValue());

        } else {
            if (selectedMessageChat.isEmpty(entityManagerFactory))
                deleteChat();
            renderMessages(orderChat);
        }
        deselectMessage();
    }

    @FXML public void deleteChat() {
        if(FxUtils.generateConfirmationAlert(Alert.AlertType.INFORMATION, "U sure", "Do you really want to delete this chat?")) {
            CustomHibernate customHibernate = new CustomHibernate(entityManagerFactory);
            // get the foreign key off
            FoodOrder foodOrder = orderChat.getFoodOrder();
            foodOrder.setChat(null);
            genericHibernate.updateEntity(foodOrder);

            customHibernate.deleteMessagesByChat(orderChat);
            genericHibernate.deleteEntityById(Chat.class, orderChat.getId());

            orderChat = null;
            sender = null;
            deselectMessage();
            chatSelectBox.getSelectionModel().clearSelection();
            clearChatFields();
            messagesList.getItems().clear();
            setChatSelectBox(userSelectBox.getSelectionModel().getSelectedItem());
        }
    }

    private void clearChatFields(){
        senderField.clear();
        receiverField.clear();
        orderIdField.clear();
    }


}

// truksta delete mygtuko pas admina
// padaryk vbox instead of list?
