package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.util.ResourceBundle;

public class EventsOverviewCtrl {

    private final ServerUtils server;
    private final SplittyMainCtrl mainCtrl;
    private Stage primaryStage;
    private Scene scene;
    private ResourceBundle bundle;
    @FXML
    private ComboBox<String> comboBox;
    private String[] filters = {"title", "creation date", "last activity"};



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
        comboBox.setItems(FXCollections.observableArrayList(filters));
    }

    /**
     * Goes back to the start screen
     */
    public void back(){
        mainCtrl.showStartScreen();
    }
}
