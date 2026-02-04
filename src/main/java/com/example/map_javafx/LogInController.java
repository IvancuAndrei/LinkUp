package com.example.map_javafx;

import com.example.domain.User;
import com.example.service.FriendshipRequestService;
import com.example.service.FriendshipService;
import com.example.service.MessageService;
import com.example.service.UserService;
import com.example.utils.password.PasswordHashing;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LogInController {

    // LOGIN
    @FXML private TextField textFieldUsernameLogIn;
    @FXML private PasswordField passwordFieldPasswordLogIn;
    @FXML private Label loginErrorLabel;

    // REGISTER
    @FXML private TextField textFieldFirstNameSignUp;
    @FXML private TextField textFieldLastNameSignUp;
    @FXML private TextField textFieldUsernameSignUp;
    @FXML private PasswordField passwordFieldPasswordSignUp;
    @FXML private Label registerErrorLabel;

    // BOXES
    @FXML private VBox loginBox;
    @FXML private VBox registerBox;

    private UserService userService;
    private FriendshipService friendshipService;
    private FriendshipRequestService friendshipRequestService;
    private MessageService messageService;

    public void setService(UserService userService,
                           FriendshipService friendshipService,
                           FriendshipRequestService friendshipRequestService,
                           MessageService messageService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
        this.friendshipRequestService = friendshipRequestService;
        this.messageService = messageService;
    }

    // SWITCH UI
    @FXML
    private void showRegister() {
        loginBox.setVisible(false);
        loginBox.setManaged(false);
        registerBox.setVisible(true);
        registerBox.setManaged(true);
    }

    @FXML
    private void showLogin() {
        registerBox.setVisible(false);
        registerBox.setManaged(false);
        loginBox.setVisible(true);
        loginBox.setManaged(true);
    }

    // LOGIN
    @FXML
    private void handleLogIn() {
        try {
            User user = userService.checkUser(
                    textFieldUsernameLogIn.getText(),
                    PasswordHashing.hashPassword(passwordFieldPasswordLogIn.getText())
            );
            openUserView(user);
        } catch (Exception e) {
            loginErrorLabel.setVisible(true);
        }
    }

    // REGISTER
    @FXML
    private void handleSignUp() {
        try {
            User newUser = new User(
                    textFieldFirstNameSignUp.getText(),
                    textFieldLastNameSignUp.getText(),
                    textFieldUsernameSignUp.getText(),
                    PasswordHashing.hashPassword(passwordFieldPasswordSignUp.getText())
            );

            userService.registerUser(newUser);
            User savedUser = userService.getUserByUsername(newUser.getUsername());
            openUserView(savedUser);

        } catch (Exception e) {
            registerErrorLabel.setVisible(true);
        }
    }

    // OPEN MAIN APP
    private void openUserView(User user) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("user-view.fxml"));
        AnchorPane root = loader.load();

        UserController controller = loader.getController();
        controller.setService(user, userService, friendshipService,
                friendshipRequestService, messageService);

        Stage stage = (Stage) loginBox.getScene().getWindow();
        stage.setScene(new Scene(root));
    }
}
