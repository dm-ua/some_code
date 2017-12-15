package sample.logInWindow;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.Main;

/**
 * Created by dm_ua on 03.09.2017.
 */
public class LogInController {
    private String loginName = "root";
    private String password  = "password";
    private Scene scene;
    private Stage stage;
    private Main main;

    public Main getMain(){return main;}

    public void setMain(Main main) {
        this.main = main;
    }

    public LoginController() {
    }

    @FXML
    Button loginButton;

    @FXML
    Button cancelButton;

    @FXML
    TextField loginField;

    @FXML
    PasswordField passwordField;

    @FXML
    public void handleLogin() {
        if(loginField.getText().trim().equals(loginName) && passwordField.getText().equals(password)) {
            main.authenticationOK();
        }else {
            System.out.println("Не правильні данні");
        }
    }

    @FXML
    public void handleCancel() {
        System.exit(0);
    }




}
