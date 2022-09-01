package com.example.musical_project;

import java.io.IOException;
import java.net.Authenticator;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

public class LoginController {

    @FXML
    private Button backButton;

    @FXML
    private ImageView closeButton;

    @FXML
    private ImageView closeButton1;

    @FXML
    private TextField emailField;

    @FXML
    private ImageView horizontalLineButton;

    @FXML
    private ImageView horizontalLineButton1;

    @FXML
    private TextField loginField;

    @FXML
    private TextField loginName;

    @FXML
    private AnchorPane loginPane;

    @FXML
    private TextField nameField;

    @FXML
    private TextField passwordField;

    @FXML
    private TextField passwordName;

    @FXML
    private Button registrButton;

    @FXML
    private Button registrButton1;

    @FXML
    private AnchorPane registrPane;

    @FXML
    private TextField secondNameField;

    @FXML
    private Button signUpButton;

    @FXML
    void initialize() {
        DataBase dataBase = new DataBase();

        signUpButton.setOnAction(actionEvent -> {
            String loginAuth = loginName.getText().trim();
            String passwordAuth = passwordName.getText().trim();
            if(!loginAuth.equals("") || !passwordAuth.equals("")){
                userAuthenticator(loginAuth, passwordAuth);

            }
            else{
                Animation animationLoginField = new Animation(loginName);
                Animation animationPassField = new Animation(passwordName);
                animationLoginField.startAnimation();
                animationPassField.startAnimation();
            }
        });

        registrButton.setOnAction(actionEvent -> {
            loginPane.setVisible(false);
            registrPane.setVisible(true);
        });

        backButton.setOnAction(actionEvent -> {
            loginPane.setVisible(true);
            registrPane.setVisible(false);
        });

        registrButton1.setOnAction(event -> {
            UserData userData = new UserData(nameField.getText().trim(), secondNameField.getText().trim(), loginField.getText().trim(), passwordField.getText().trim(), emailField.getText().trim());
            dataBase.writeDownRegistration(userData);
            registrPane.setVisible(false);
            loginPane.setVisible(true);
        });

        horizontalLineButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Stage stage = null;
                stage = (Stage) ((ImageView) event.getSource()).getScene().getWindow();
                stage.setIconified(true);
            }
        });
        closeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                System.exit(0);
            }
        });
    }

    private void userAuthenticator(String loginAuth, String passwordAuth) {
        DataBase dataBaseAuth = new DataBase();
        UserData userDataAuth = new UserData();
        userDataAuth.setLogin(loginAuth);
        userDataAuth.setPassword(passwordAuth);
        ResultSet resultSet = dataBaseAuth.getUser(userDataAuth);
        try {
            if(resultSet.next()){
                dataBaseAuth.setLoginUser(userDataAuth.getLogin());
                registrButton.getScene().getWindow().hide();
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("mainMenu.fxml"));
                try {
                    fxmlLoader.load();
                } catch (IOException exception){
                    exception.printStackTrace();
                }

                Parent root = fxmlLoader.getRoot();
                Stage stage = new Stage();
                Scene scene = new Scene(root);
                stage.initStyle(StageStyle.TRANSPARENT);
                WindowSettings windowSettings = new WindowSettings();
                windowSettings.moveWindow(scene, stage);
                stage.setScene(scene);
                stage.showAndWait();
            }
            else {
                Animation animationLoginField = new Animation(loginName);
                Animation animationPassField = new Animation(passwordName);
                animationLoginField.startAnimation();
                animationPassField.startAnimation();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
