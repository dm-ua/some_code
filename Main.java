package sample;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import sample.jobScene.JobController;
import sample.logInWindow.LoginController;

public class Main extends Application {
    private Stage stage;
    private Scene menuBarScene;

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.stage = primaryStage;
        primaryStage.setTitle("Name of company");
        initRootPane();
    }

    public static void main(String[] args) {

        launch(args);
    }

    public void authenticationOK() {
        stage.setScene(menuBarScene);
        stage.show();
    }

    private void initRootPane() throws Exception {
        FXMLLoader loginLoader = new FXMLLoader();
        FXMLLoader menuBarLoader = new FXMLLoader();
        loginLoader.setLocation(Main.class.getResource("logInWindow/LogInWindow.fxml"));
        menuBarLoader.setLocation(Main.class.getResource("jobScene/MainWindow.fxml"));
        AnchorPane loginPane = (AnchorPane) loginLoader.load();
        AnchorPane rootPane = (AnchorPane) menuBarLoader.load();
        LoginController loginController = loginLoader.<LoginController>getController();
        JobController menubarController = menuBarLoader.getController();
        menubarController.setMain(this);
        loginController.setMain(this);
        Scene loginScene = new Scene(loginPane);
        menuBarScene = new Scene(rootPane, 720, 400);
        stage.setScene(loginScene);
        loginController.handleLogin();
        stage.show();
    }

}