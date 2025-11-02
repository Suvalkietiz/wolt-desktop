package com.example.programavimotechnologijosprif.fxControllers;

import com.example.programavimotechnologijosprif.Utils.FxUtils;
import com.example.programavimotechnologijosprif.hibernateControllers.CustomHibernate;
import com.example.programavimotechnologijosprif.hibernateControllers.GenericHibernate;
import com.example.programavimotechnologijosprif.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;

import java.util.List;

public class NewOrderForm {
    @FXML public ComboBox<AppUser> selectAppUserBox;
    @FXML public ComboBox<Driver> selectDriverBox;
    @FXML public ComboBox<Restaurant> selectRestaurantBox;

    @FXML public ListView<Food> restaurantFoodList;
    @FXML public ListView<Food> orderFoodList;


    private EntityManagerFactory  entityManagerFactory;
    private GenericHibernate genericHibernate;

    private User loggedUser = null;

    public void setData(EntityManagerFactory entityManagerFactory, User loggedUser){
        this.entityManagerFactory = entityManagerFactory;
        genericHibernate = new GenericHibernate(entityManagerFactory);
        this.loggedUser = loggedUser;
        initializeComboBoxes();
    }
    private void initializeComboBoxes(){
        selectAppUserBox.getItems().addAll(genericHibernate.getAllRecords(AppUser.class));
        selectDriverBox.getItems().addAll(genericHibernate.getAllRecords(Driver.class));
        if(loggedUser instanceof Restaurant){
            selectRestaurantBox.setValue((Restaurant) loggedUser);
            selectRestaurantBox.setDisable(true);
            CustomHibernate customHibernate = new CustomHibernate(entityManagerFactory);
            restaurantFoodList.getItems().addAll(customHibernate.getRestaurantFood((Restaurant) loggedUser));
        } else selectRestaurantBox.getItems().addAll(genericHibernate.getAllRecords(Restaurant.class));
    }

    @FXML public void setRestaurantFood(ActionEvent actionEvent) {
        orderFoodList.getItems().clear();
        CustomHibernate customHibernate = new CustomHibernate(entityManagerFactory);
        restaurantFoodList.getItems().clear();
        restaurantFoodList.getItems().addAll(customHibernate.getRestaurantFood(selectRestaurantBox.getSelectionModel().getSelectedItem()));
    }

    @FXML public void addFood(ActionEvent actionEvent) {
        orderFoodList.getItems().add(restaurantFoodList.getSelectionModel().getSelectedItem());
    }

    @FXML public void deleteFood(ActionEvent actionEvent) {
        orderFoodList.getItems().remove(orderFoodList.getSelectionModel().getSelectedItem());
    }

    @FXML public void createFoodOrder(ActionEvent actionEvent) {
        if(validateNewFoodOrder()) {
            FoodOrder foodOrder = new FoodOrder(
                    selectAppUserBox.getValue(),
                    selectDriverBox.getValue(),
                    selectRestaurantBox.getValue(),
                    orderFoodList.getItems());
            foodOrder.calculatePrice();
            genericHibernate.createEntity(foodOrder, "FoodOrderCreationFailed", "Food Order creation in DataBase was unsuccessful");
            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Success", "Your order was successfully created");
            clearAllFields();
        }
    }

    private boolean validateNewFoodOrder(){
        if(selectAppUserBox.getValue() == null) {
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Netinkamas klientas", "Pasirinkite klienta");
            return false;
        }else if(selectDriverBox.getValue() == null){
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Netinkamas vairutoojas", "Pasirinkite vairutoja");
            return false;
        }else if(selectRestaurantBox.getValue() == null){
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Netinkamas restoranas", "Pasirinkite restoarana");
            return false;
        } else if(orderFoodList.getItems().isEmpty()){
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Tuscias krepselis", "Pasirinkite bent viena maistuka. . .");
            return false;
        }
        return true;
    }

    private void clearAllFields(){
        selectAppUserBox.getSelectionModel().clearSelection();
        selectDriverBox.getSelectionModel().clearSelection();
        selectRestaurantBox.getSelectionModel().clearSelection();
        orderFoodList.getItems().clear();
        restaurantFoodList.getItems().clear();
    }
}
