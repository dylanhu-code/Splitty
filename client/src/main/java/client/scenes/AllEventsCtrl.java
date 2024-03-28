package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.ResourceBundle;

import static client.scenes.SplittyMainCtrl.currentLocale;

public class AllEventsCtrl {

    private ServerUtils server;
    private SplittyMainCtrl mainCtrl;
    private Stage primaryStage;
    private Scene scene;
    private ResourceBundle bundle;

    @FXML
    private Button backButton;
    @FXML
    private Button lastActivityButton;
    @FXML
    private Button creationDateButton;
    @FXML
    private Button titleButton;
    @FXML
    private Button backupsButton;

    /**
     * Constructs an instance of EventsOverviewCtrl with the specified dependencies.
     *
     * @param server   The ServerUtils instance.
     * @param mainCtrl The MainCtrl instance.
     */
    @Inject
    public AllEventsCtrl(ServerUtils server, SplittyMainCtrl mainCtrl) {
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

        primaryStage.setScene(scene);
        primaryStage.show();

        bundle = ResourceBundle.getBundle("messages", currentLocale);
        updateUI();

    }

    /**
     * Update UI to language setting
     */
    private void updateUI() {
        backButton.setText(bundle.getString("backButton"));
        lastActivityButton.setText(bundle.getString("lastActivityButton"));
        creationDateButton.setText(bundle.getString("creationDateButton"));
        titleButton.setText(bundle.getString("titleButton"));
        backupsButton.setText(bundle.getString("backupsButton"));
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
