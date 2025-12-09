package com.example.programavimotechnologijosprif.fxControllers;

import com.example.programavimotechnologijosprif.HelloApplication;
import com.example.programavimotechnologijosprif.Utils.DateValidation;
import com.example.programavimotechnologijosprif.Utils.FxUtils;
import com.example.programavimotechnologijosprif.hibernateControllers.CustomHibernate;
import com.example.programavimotechnologijosprif.hibernateControllers.GenericHibernate;
import com.example.programavimotechnologijosprif.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainForm implements Initializable {

    //<editor-fold desc="User Management Tab elements">
    @FXML public Tab userManagementTab;
    @FXML public TableView<UserTableParamaters> userTable;
    @FXML public TableColumn<UserTableParamaters, String> idColumn;
    @FXML public TableColumn<UserTableParamaters, String> loginCol;
    @FXML public TableColumn<UserTableParamaters, String> passwordCol;
    @FXML public TableColumn<UserTableParamaters, String> userTypeCol;
    @FXML public TableColumn<UserTableParamaters, String> nameCol;
    @FXML public TableColumn<UserTableParamaters, String> surnameCol;
    @FXML public TableColumn<UserTableParamaters, String> phoneCol;
    @FXML public TableColumn<UserTableParamaters, String> addressCol;
    @FXML public TableColumn<UserTableParamaters, String> createdOnCol;
    @FXML public ComboBox<String> userTypeFilterBox;
    //</editor-fold>

    //<editor-fold desc="Restaurant Management Tab elements">
    @FXML public ComboBox<Restaurant> selectRestaurantBox;
    @FXML public TextField dishNameField;
    @FXML public ListView<Ingredients> availableIngredientsList;
    @FXML public ListView<Ingredients> selectedIngredientList;
    @FXML public TextField foodPriceField;
    @FXML public ListView<Food> restaurantFoodList;
    //</editor-fold>

    //<editor-fold desc="Order Management Tab elements">
    @FXML public ComboBox<User> selectUserBox;
    @FXML public TextField orderAppUserField;
    @FXML public TextField orderDriverField;
    @FXML public TextField orderRestaurantField;
    @FXML public TextField orderPriceField;
    @FXML public TextField orderCreatedOnField;
    @FXML public TextField orderCompletedField;
    @FXML public ListView<Food> orderItemsList;
    @FXML public ListView<FoodOrder> userOrderList;
    @FXML public Button writeChatButton;
    @FXML public TextField orderStatusField;
    @FXML public ComboBox<Status> selectStatusBox;
    @FXML public TextField selectFromField;
    @FXML public TextField selectToField;
    @FXML public Button statusUpdateButton;
    //</editor-fold>

    @FXML public TabPane managementTabPane;
    @FXML public AnchorPane adminUserManagement;


    private EntityManagerFactory entityManagerFactory;
    private GenericHibernate genericHibernate;

    private User loggedUser;

    private Restaurant selectedRestaurant = null;
    private User orderUser = null;

    private int run = 0;


    //<editor-fold desc="Initializing methods">
    @Override
    public void initialize(URL location, ResourceBundle resources){
        System.out.println("Initializing MainForm");
        initializeUserTab();
        //initializeRestaurantTab();
    }

    private void initializeUserTab(){
        userTable.setEditable(true);
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        userTypeCol.setCellValueFactory(new PropertyValueFactory<>("userType"));
        loginCol.setCellValueFactory(new PropertyValueFactory<>("login"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameCol.setCellValueFactory(new PropertyValueFactory<>("surname"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        createdOnCol.setCellValueFactory(new PropertyValueFactory<>("dateCreated"));


        userTypeFilterBox.setItems(FXCollections.observableArrayList("All", "Admin", "Restaurant", "Driver", "AppUser"));
        userTypeFilterBox.setValue("All");
    }

    public void setRole(User currUser){
        if(currUser != null){
            this.loggedUser = currUser;
            SingleSelectionModel<Tab> selectionModel = managementTabPane.getSelectionModel();
            if(loggedUser.isAdmin()){
                System.out.println("User is Admin");
                selectionModel.select(0);
                adminUserManagement.setVisible(true);
                writeChatButton.setVisible(false);
            } else if(loggedUser instanceof Restaurant) {
                System.out.println("User is Restaurant");
                selectionModel.select(1);
                adminUserManagement.setVisible(false);

                //Restaurant Management Tab:
                selectRestaurantBox.setDisable(true);
                selectRestaurantBox.setValue((Restaurant) loggedUser);
                renderRestaurantFood();

                //Order Management Tab:
                orderUser = loggedUser;
                selectUserBox.setDisable(true);
                selectUserBox.setValue(loggedUser);
                writeChatButton.setVisible(true);
            } else {
                System.out.println("Unkown User Type");
                FxUtils.generateAlert(Alert.AlertType.ERROR, "Get lost", "Logged in user is neither 'Admin' nor 'Restaurant'.");
                System.exit(0);
            }
        }
    }

    public void setData(EntityManagerFactory entityManagerFactory){
        this.entityManagerFactory = entityManagerFactory;
        genericHibernate = new GenericHibernate(entityManagerFactory);
        reloadTableData();
    }
    //</editor-fold>



    @FXML public void loadRestaurantManagementData() {
        // set combo box values
        selectRestaurantBox.getItems().clear();
        System.out.println("Reloading Restaurant Management");
        List<User> allUsers = genericHibernate.getAllRecords(User.class);
        List<Restaurant> allRestaurants = new ArrayList<>();
        for(User user : allUsers)if(user instanceof Restaurant)allRestaurants.add((Restaurant) user);
        selectRestaurantBox.getItems().addAll(allRestaurants);
        // set available ingredient list
        availableIngredientsList.getItems().addAll(Ingredients.values());

        if(loggedUser.isAdmin()){
            selectedRestaurant = null;
        } else {
            selectedRestaurant = (Restaurant) loggedUser;
        }

    }


    @FXML public void reloadTableData() {
        if(loggedUser instanceof Restaurant){
            // shitload of mistakes here
            // problema - restorantUserOpenUserForm dublikuoja openUserForm . reloadTableData metodas kvieciamas ++ kartu negu reikia
            // ivykdzius update duomenu baze atnaujinama, bet pati user-form kai kvieciama is update lieka tokia pati
            System.out.println("Restorant is detected");
            if(run%2 == 1) { // XDDDD
                try {
                    restorantUserOpenUserForm();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            run++;
        }
        ObservableList<UserTableParamaters> data = FXCollections.observableArrayList();
        if(userManagementTab.isSelected()){
            userTable.getItems().clear();
            List<User> users = genericHibernate.getAllRecords(User.class); // find why generichibernate could be null
            for(User user : users){
                UserTableParamaters userTableParamaters = new UserTableParamaters();
                userTableParamaters.setId(user.getId());
                userTableParamaters.setUserType(user.getClass().getSimpleName());
                userTableParamaters.setLogin(user.getLogin());
                userTableParamaters.setPassword(user.getPassword());
                userTableParamaters.setName(user.getName());
                userTableParamaters.setSurname(user.getSurname());
                userTableParamaters.setPhoneNumber(user.getPhoneNumber());
                userTableParamaters.setDateCreated(user.getDateCreated().toString());
                String userType = "Admin";
                if(user instanceof AppUser){
                    userTableParamaters.setAddress(((AppUser) user).getAddress());
                    userType = "AppUser";
                }
                if(user instanceof Restaurant){
                    userTableParamaters.setAddress(((Restaurant) user).getAddress());
                    userType = "Restaurant";
                }
                if(user instanceof Driver){
                    userTableParamaters.setAddress(((Driver) user).getAddress());
                    userType = "Driver";
                }

                if(applyFilter(userType))
                    data.add(userTableParamaters);
            }
            userTable.getItems().addAll(data);
            data.clear();
        }
    }

    private boolean applyFilter(String userType){
        String typeFilter = userTypeFilterBox.getValue();
        switch (typeFilter){
            case "All":
                return true;
            case "Admin":
                if(userType.equals("Admin"))return true;
                return false;
            case "Restaurant":
                if(userType.equals("Restaurant"))return true;
                return false;
            case "Driver":
                if(userType.equals("Driver"))return true;
                return false;
            case "AppUser":
                if(userType.equals("AppUser"))return true;
                return false;
        }
        return false;
    }



    //============================================
    //######    User Management BUTTONS     ######
    //============================================


    @FXML public void deleteUser(ActionEvent actionEvent) {
        UserTableParamaters seletectedParameterUser = userTable.getSelectionModel().getSelectedItem();
        if(seletectedParameterUser != null) {
            if(seletectedParameterUser.getId() != loggedUser.getId()) {
                if (FxUtils.generateConfirmationAlert(Alert.AlertType.INFORMATION, "Confirmation",
                        String.format(
                                "Do you really want to delete user '%s' of type '%s'?", seletectedParameterUser.getName(), seletectedParameterUser.getUserType())
                )) {
                    genericHibernate.deleteEntityById(User.class, seletectedParameterUser.getId());
                    reloadTableData();
                } else {
                    System.out.println("Delete user aborted");
                }
            } else FxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "You are not allowed to delete yourself");
        } else System.out.println("Select user first to delete");
    }


    @FXML public void openUserForm() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user-form.fxml"));
        Parent parent = fxmlLoader.load();


        UserTableParamaters seletectedParameterUser = userTable.getSelectionModel().getSelectedItem();
        User selectedUser = null; //updating -> selectedUser NOT NULL
        if(seletectedParameterUser != null)selectedUser = castParameterUserToUser(seletectedParameterUser);
        UserForm userForm = fxmlLoader.getController();
        userForm.setData(entityManagerFactory, selectedUser);


        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Update User");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        System.out.println("closed");
        reloadTableData();
    }
    // ----------- code dublication -----------
    private void restorantUserOpenUserForm() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user-form.fxml"));
        Parent parent = fxmlLoader.load();

        UserForm userForm = fxmlLoader.getController();
        userForm.setData(entityManagerFactory, loggedUser);

        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Update User");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        System.out.println("closed");
    }


    @FXML public void selectedFilter(ActionEvent actionEvent) {
        reloadTableData();
    }
    //================================================================
    //====================FUNCTIONAL METHODS==========================
    //================================================================
    private User castParameterUserToUser(UserTableParamaters parameterUser){
        if(parameterUser == null) return null;
        User seletedUser = null;

        switch (parameterUser.getUserType()) {
            case "AppUser":
                AppUser appUser = new AppUser();
                appUser.setAddress(parameterUser.getAddress());
                seletedUser = appUser;
                break;

            case "Restaurant":
                Restaurant restaurant = new Restaurant();
                restaurant.setAddress(parameterUser.getAddress());

                Restaurant rtemp = genericHibernate.getEntityById(Restaurant.class, parameterUser.getId());
                restaurant.setWorkHours(rtemp.getWorkHours());

                seletedUser = restaurant;
                break;

            case "Driver":
                Driver driver = new Driver();
                driver.setAddress(parameterUser.getAddress());

                Driver dtemp = genericHibernate.getEntityById(Driver.class, parameterUser.getId());
                driver.setBirthDate(dtemp.getBirthDate());
                driver.setDriverLicense(dtemp.getDriverLicense());

                seletedUser = driver;
                break;

            default:
                seletedUser = new User();
                break;
        }
        seletedUser.setId(parameterUser.getId());
        seletedUser.setLogin(parameterUser.getLogin());
        seletedUser.setPassword(parameterUser.getPassword());
        seletedUser.setName(parameterUser.getName());
        seletedUser.setSurname(parameterUser.getSurname());
        seletedUser.setPhoneNumber(parameterUser.getPhoneNumber());
        seletedUser.setDateCreated(LocalDate.parse(parameterUser.getDateCreated()));

        return seletedUser;
    }





    //[==================================]

    private void clearFoodInputFields(){
        dishNameField.clear();
        selectedIngredientList.getItems().clear();
        foodPriceField.clear();
    }

    @FXML public void deselectFood(ActionEvent actionEvent) {
        if(selectedRestaurant == null) return;
        restaurantFoodList.getSelectionModel().clearSelection();
        clearFoodInputFields();
    }




    @FXML public void setRestaurant(ActionEvent actionEvent) {
        selectedRestaurant = selectRestaurantBox.getValue();
        renderRestaurantFood();
        clearFoodInputFields();
    }

    @FXML public void setFood(MouseEvent mouseEvent) {
        if(selectedRestaurant == null) return;
        clearFoodInputFields();

        dishNameField.setText(restaurantFoodList.getSelectionModel().getSelectedItem().getName());
        selectedIngredientList.getItems().addAll(restaurantFoodList.getSelectionModel().getSelectedItem().getIngredients());
        foodPriceField.setText(String.valueOf(restaurantFoodList.getSelectionModel().getSelectedItem().getPrice()));
    }

    @FXML public void deleteFood(ActionEvent actionEvent) {
        if(selectedRestaurant == null) return;
        if(restaurantFoodList.getSelectionModel().getSelectedItem() == null){
            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Ka tu cia", "Select food which you want to delete");
            return;
        }
        genericHibernate.deleteEntityById(Food.class, restaurantFoodList.getSelectionModel().getSelectedItem().getId());
        clearFoodInputFields();
        renderRestaurantFood();
    }


    @FXML public void saveFood(ActionEvent actionEvent) {
        if(selectedRestaurant == null) return;
        if(!validateNewFoodFields())return;

        Food food = new Food();
        food.setRestaurant(selectedRestaurant);
        food.setName(dishNameField.getText());
        food.setIngredients(selectedIngredientList.getItems());
        //food.setAllergens("None");
        food.setPrice(Double.parseDouble(foodPriceField.getText()));

        if(restaurantFoodList.getSelectionModel().getSelectedItem() == null) {
            // create new food
            genericHibernate.createEntity(food, "Insert Food", "Nepavyko prideti patiekalo i duomenu baze");
            renderRestaurantFood();
            clearFoodInputFields();
        } else {
            // update food
            food.setId(restaurantFoodList.getSelectionModel().getSelectedItem().getId());
            genericHibernate.updateEntity(food);
            renderRestaurantFood();
            clearFoodInputFields();
        }
    }
    @FXML public void addIngredient(ActionEvent actionEvent) {
        if(selectedRestaurant == null) return;
        if(availableIngredientsList.getSelectionModel().getSelectedItem() == null) return;
        selectedIngredientList.getItems().add(availableIngredientsList.getSelectionModel().getSelectedItem());
    }

    @FXML public void deleteIngredient(ActionEvent actionEvent) {
        if(selectedRestaurant == null) return;
        if(selectedIngredientList.getSelectionModel().getSelectedItem() == null) return;
        selectedIngredientList.getItems().remove(selectedIngredientList.getSelectionModel().getSelectedItem());
    }

    private boolean validateNewFoodFields(){
        if(dishNameField.getText().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Please enter a dish name");
            return false;
        }
        if(selectedIngredientList.getItems().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Please select at least 1 ingredient");
            return false;
        }
        if(foodPriceField.getText().isEmpty()) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Please enter your food price");
            return false;
        }

        try {
            Double.parseDouble(foodPriceField.getText());
        } catch(NumberFormatException e){
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Price must be a number");
            return false;
        }

        return true;
    }


    private void renderRestaurantFood(){
        CustomHibernate customHibernate = new CustomHibernate(entityManagerFactory);
        restaurantFoodList.getItems().clear();
        restaurantFoodList.getItems().addAll(customHibernate.getRestaurantFood(selectedRestaurant));
    }


    //[==================================]

    private void clearOrderFields(){
        orderAppUserField.clear();
        orderDriverField.clear();
        orderRestaurantField.clear();
        orderPriceField.clear();
        orderCreatedOnField.clear();
        orderCompletedField.clear();
        orderItemsList.getItems().clear();
        orderStatusField.clear();
    }
    private void disableOrderFields() {
        clearOrderFields();
        orderAppUserField.setDisable(true);
        orderDriverField.setDisable(true);
        orderRestaurantField.setDisable(true);
        orderPriceField.setDisable(true);
        orderCreatedOnField.setDisable(true);
        orderCompletedField.setDisable(true);
        orderItemsList.setDisable(true);
        orderStatusField.setDisable(true);
    }
    @FXML public void loadOrderData(Event event) {
        selectUserBox.getItems().clear();
        selectUserBox.getItems().addAll(genericHibernate.getAllRecords(User.class));
        disableOrderFields();

        selectStatusBox.getItems().clear();
        selectStatusBox.getItems().addAll(Status.values());

        userOrderList.getItems().clear();
        fillUserOrders();
    }

    @FXML public void setUserOrders(ActionEvent actionEvent) {
        clearOrderFields();
        orderUser = selectUserBox.getSelectionModel().getSelectedItem();
        fillUserOrders();
    }
    private void fillUserOrders(){
        if(orderUser == null) return;
        CustomHibernate customHibernate = new CustomHibernate(entityManagerFactory);
        userOrderList.getItems().clear();

        List<FoodOrder> userOrders;
        if(orderUser instanceof Restaurant){
            userOrders = customHibernate.getRestaurantOrders((Restaurant) orderUser);
            //userOrderList.getItems().addAll(customHibernate.getRestaurantOrders((Restaurant) orderUser));
        } else if(orderUser instanceof Driver){
            userOrders = customHibernate.getDriverOrders((Driver) orderUser);
            //userOrderList.getItems().addAll(customHibernate.getDriverOrders((Driver) orderUser));
        } else if (orderUser instanceof AppUser) {
            userOrders = customHibernate.getAppUserOrders((AppUser) orderUser);
            //userOrderList.getItems().addAll(customHibernate.getAppUserOrders((AppUser) orderUser));
        } else userOrders = new ArrayList<>();


        // apply status filter
        if(selectStatusBox.getSelectionModel().getSelectedItem() != null){
            var selectedStatus = selectStatusBox.getSelectionModel().getSelectedItem();
            userOrders = userOrders.stream()
                    .filter(c-> selectedStatus.equals(c.getStatus()))
                    .toList();
        }

        // apply from filter
        if(!selectFromField.getText().isEmpty()){
            DateValidation dateValidation = new DateValidation();
            if(dateValidation.isValid(selectFromField.getText())){
                System.out.println("Valid date");
                LocalDate fromDate = LocalDate.parse(selectFromField.getText());
                userOrders = userOrders.stream()
                        .filter(c->c.getTimeCreated().isAfter(fromDate.atStartOfDay()))
                        .collect(Collectors.toList());
            } else FxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Invalid date format. Please enter a valid date, format = " + dateValidation.getDateFormat());
        }
        // apply to filter
        if(!selectToField.getText().isEmpty()){
            DateValidation dateValidation = new DateValidation();
            if(dateValidation.isValid(selectToField.getText())){
                LocalDate toDate = LocalDate.parse(selectToField.getText());
                userOrders = userOrders.stream()
                        .filter(c->c.getTimeCreated().isBefore(toDate.atStartOfDay()))
                        .collect(Collectors.toList());
            } else FxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Invalid date format. Please enter a valid date, format = " + dateValidation.getDateFormat());
        }


        userOrderList.getItems().addAll(userOrders);

    }

    @FXML public void createOrder(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("newOrder-view.fxml"));
        Parent parent = fxmlLoader.load();

        NewOrderForm newOrderForm = fxmlLoader.getController();
        newOrderForm.setData(entityManagerFactory, loggedUser);

        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Create New Order");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        System.out.println("closed");
        fillUserOrders();
    }

    @FXML public void deleteOrder(ActionEvent actionEvent) {
        if(orderUser == null) return;
        if(userOrderList.getSelectionModel().getSelectedItem() == null){
            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Ka tu cia", "Select food order to delete");
            return;
        }
        if(FxUtils.generateConfirmationAlert(Alert.AlertType.INFORMATION, "Are you sure?", "Do you really want to delete selected food order?")){
            userOrderList.getSelectionModel().getSelectedItem();
            genericHibernate.deleteEntityById(FoodOrder.class, userOrderList.getSelectionModel().getSelectedItem().getId());
            clearOrderFields();
            userOrderList.getItems().clear();
            fillUserOrders();
        }
    }

    @FXML public void fillOrderItems(MouseEvent mouseEvent) {
        clearOrderFields();
        FoodOrder selectedFoodOrder = userOrderList.getSelectionModel().getSelectedItem();
        if(selectedFoodOrder == null) return; // dead code?
        orderAppUserField.setText(String.valueOf(selectedFoodOrder.getAppUser()));
        orderDriverField.setText(String.valueOf(selectedFoodOrder.getDriver()));
        orderRestaurantField.setText(String.valueOf(selectedFoodOrder.getRestaurant()));
        orderPriceField.setText(String.valueOf(selectedFoodOrder.getPrice()));
        orderCreatedOnField.setText(selectedFoodOrder.getTimeCreated().toString());
        if(selectedFoodOrder.getStatus().equals(Status.COMPLETED))
            orderCompletedField.setText(selectedFoodOrder.getTimeCompleted().toString());
        orderItemsList.getItems().addAll(selectedFoodOrder.getItems());
        orderStatusField.setText(selectedFoodOrder.getStatus().toString());

        if(selectedFoodOrder.getStatus().equals(Status.PENDING))
            statusUpdateButton.setText("Approve order and start making");
        else if(selectedFoodOrder.getStatus().equals(Status.PREPAIRING_FOOD))
            statusUpdateButton.setText("Order is prepared");
        else statusUpdateButton.setText("Update");
    }

    @FXML public void updateStatus(ActionEvent actionEvent) {
        FoodOrder selectedFoodOrder = userOrderList.getSelectionModel().getSelectedItem();
        if(selectedFoodOrder == null) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "You need to select a food order to update it's status");
            return;
        }
        if(!selectedFoodOrder.getStatus().equals(Status.PENDING) && !selectedFoodOrder.getStatus().equals(Status.PREPAIRING_FOOD)){
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "There is nothing to update");
            return;
        }
        if(selectedFoodOrder.getStatus().equals(Status.PENDING)){
            CustomHibernate customHibernate = new CustomHibernate(entityManagerFactory);
            customHibernate.updateOrderStatus(selectedFoodOrder, Status.PREPAIRING_FOOD);
            fillUserOrders();
            deselectOrder();
            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Success", "You accepted order ID " + selectedFoodOrder.getId() + ". Start making " + selectedFoodOrder.getItems());
            System.out.println("new status is preparing");
        }
        if(selectedFoodOrder.getStatus().equals(Status.PREPAIRING_FOOD)){
            System.out.println("new status is searching driver");
            CustomHibernate customHibernate = new CustomHibernate(entityManagerFactory);
            customHibernate.updateOrderStatus(selectedFoodOrder, Status.SEARCHING_DRIVER);
            fillUserOrders();
            deselectOrder();
            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Success", "You completed order ID " + selectedFoodOrder.getId());
        }
    }


    @FXML public void openChatRead(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("chat-form.fxml"));

        Parent parent = fxmlLoader.load();
        ChatForm chatForm = (ChatForm) fxmlLoader.getController();
        chatForm.setData(entityManagerFactory, loggedUser);
        chatForm.initializeReadUI();

        Scene scene = new Scene(parent);
        Stage stage = (Stage) new Stage();
        stage.setTitle("Chats");
        stage.setScene(scene);
        stage.show();
    }

    @FXML public void openWriteChat(ActionEvent actionEvent) throws IOException {
        FoodOrder selectedFoodOrder = userOrderList.getSelectionModel().getSelectedItem();
        if(selectedFoodOrder == null){
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Koks orderis?", "Pasirink orderi jeigu nori rasyti zinute");
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("chat-form.fxml"));

            Parent parent = fxmlLoader.load();
            ChatForm chatForm = (ChatForm) fxmlLoader.getController();
            chatForm.setData(entityManagerFactory, loggedUser);
            chatForm.initializeWriteUI(selectedFoodOrder);

            Scene scene = new Scene(parent);
            Stage stage = (Stage) new Stage();
            stage.setTitle("Chats");
            stage.setScene(scene);
            stage.show();
        }
    }

    @FXML public void openReviews() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("chat-form.fxml"));

        Parent parent = fxmlLoader.load();
        ChatForm chatForm = (ChatForm) fxmlLoader.getController();
        chatForm.setData(entityManagerFactory, loggedUser);
        chatForm.initializeReviewUI(loggedUser);

        Scene scene = new Scene(parent);
        Stage stage = (Stage) new Stage();
        stage.setTitle("Reviews");
        stage.setScene(scene);
        stage.show();
    }

    @FXML public void deselectOrder() {
        userOrderList.getSelectionModel().clearSelection();

        orderStatusField.clear();
        orderAppUserField.clear();
        orderDriverField.clear();
        orderRestaurantField.clear();
        orderPriceField.clear();
        orderCreatedOnField.clear();
        orderCompletedField.clear();
        orderItemsList.getItems().clear();
        statusUpdateButton.setText("Update");
    }
}
