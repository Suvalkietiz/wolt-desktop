package com.example.programavimotechnologijosprif.fxControllers;

import com.example.programavimotechnologijosprif.HelloApplication;
import com.example.programavimotechnologijosprif.Utils.FxUtils;
import com.example.programavimotechnologijosprif.hibernateControllers.GenericHibernate;
import com.example.programavimotechnologijosprif.model.AppUser;
import com.example.programavimotechnologijosprif.model.Driver;
import com.example.programavimotechnologijosprif.model.Restaurant;
import com.example.programavimotechnologijosprif.model.User;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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



    //private ObservableList<UserTableParamaters> data = FXCollections.observableArrayList();

    private EntityManagerFactory entityManagerFactory;
    private GenericHibernate genericHibernate;

    private User loggedUser;

    public void setData(EntityManagerFactory entityManagerFactory){
        this.entityManagerFactory = entityManagerFactory;
        genericHibernate = new GenericHibernate(entityManagerFactory);
        reloadTableData();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        System.out.println("Initializing MainForm");
        initializeUserTab();

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






    public void reloadTableData() {
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


    public void deleteUser(ActionEvent actionEvent) {
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


    public void openUserForm(ActionEvent actionEvent) throws IOException {
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

    public void selectedFilter(ActionEvent actionEvent) {
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
                seletedUser = restaurant;
                break;

            case "Driver":
                Driver driver = new Driver();
                driver.setAddress(parameterUser.getAddress());
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

        return seletedUser;
    }

}
