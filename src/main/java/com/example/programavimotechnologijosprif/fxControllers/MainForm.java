package com.example.programavimotechnologijosprif.fxControllers;

import com.example.programavimotechnologijosprif.HelloApplication;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainForm implements Initializable {
    @FXML public TabPane adminPane;
    @FXML public TabPane restaurantPane;

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
    //</editor-fold>


    private EntityManagerFactory entityManagerFactory;
    private GenericHibernate genericHibernate;

    private User loggedUser;

    private Restaurant selectedRestaurant = null;
    private User orderUser = null;


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
            if(loggedUser.isAdmin()){
                System.out.println("User is Admin");
                adminPane.setVisible(true);
                restaurantPane.setVisible(false);
            } else if(loggedUser instanceof Restaurant) {
                System.out.println("User is Restaurant");
                adminPane.setVisible(false);
                restaurantPane.setVisible(true);
            } else {
                System.out.println("Unkown User Type");
                adminPane.setVisible(false);
                restaurantPane.setVisible(false);
                FxUtils.generateAlert(Alert.AlertType.ERROR, "User Error", "Logged in user is neither 'Admin' nor 'Restaurant'.");
            }
        }
    }

    public void setData(EntityManagerFactory entityManagerFactory){
        this.entityManagerFactory = entityManagerFactory;
        genericHibernate = new GenericHibernate(entityManagerFactory);
        reloadTableData();
    }
    //</editor-fold>



    @FXML public void loadRestaurantManagementData(Event event) {
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
            // cia jeigu prisijunges yra restoranas, tai priskirti restorana
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        }

    }


    @FXML public void reloadTableData() {
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


    @FXML public void openUserForm(ActionEvent actionEvent) throws IOException {
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
    }
    @FXML public void loadOrderData(Event event) {
        selectUserBox.getItems().clear();
        selectUserBox.getItems().addAll(genericHibernate.getAllRecords(User.class));
        disableOrderFields();
    }

    @FXML public void setUserOrders(ActionEvent actionEvent) {
        // show user orders in list view....
        //change order user to not null
    }

    @FXML public void createOrder(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("newOrder-view.fxml"));
        Parent parent = fxmlLoader.load();

        NewOrderForm newOrderForm = fxmlLoader.getController();
        newOrderForm.setData(entityManagerFactory);

        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle("Create New Order");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        System.out.println("closed");
        // if smth is selected then reload list data
    }
}
