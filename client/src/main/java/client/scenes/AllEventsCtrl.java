package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class EventsOverviewCtrl {

    private final ServerUtils server;
    private final SplittyMainCtrl mainCtrl;
    private Stage primaryStage;
    private Scene scene;
    private ResourceBundle bundle;

    @FXML
    private Button back;
    @FXML
    private Button lastActivity;
    @FXML
    private Button creationDate;
    @FXML
    private Button title;
    @FXML
    private Button download;

    /**
     * Constructs an instance of EventsOverviewCtrl with the specified dependencies.
     *
     * @param server   The ServerUtils instance.
     * @param mainCtrl The MainCtrl instance.
     */
    @Inject
    public EventsOverviewCtrl(ServerUtils server, SplittyMainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Initializes the page
     *
     * @param primaryStage The primary container of this page
     * @param overview     The page with its controller
     */
    public void initialize(Stage primaryStage, Scene overview) {
        this.primaryStage = primaryStage;
        this.scene = overview;
    }

    /**
     * Goes back to the start screen
     */
    public void back(){
        mainCtrl.showStartScreen();
    }

    /**
     * Goes to the backups page
     */
    public void backups(){
        mainCtrl.showBackups();
    }
}
