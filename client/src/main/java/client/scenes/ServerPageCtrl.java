package client.scenes;

import client.utils.ConfigUtils;
import client.utils.ServerUtils;
import client.utils.StatisticsUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class ServerPageCtrl {
    SplittyMainCtrl mainCtrl;
    ServerUtils utils;

    @FXML
    private TextField connectUrl;
    @FXML
    private Button connectButton;
    private Stage primaryStage;
    private Scene serverPageScene;

    /**
     * Constructor -
     * @param mainCtrl - Splitty Main controller
     * @param utils - the server utils
     */
    @Inject
    public ServerPageCtrl(SplittyMainCtrl mainCtrl, ServerUtils utils) {
        this.mainCtrl = mainCtrl;
        this.utils = utils;
    }

    public void initialize(Stage primaryStage, Scene serverPageScene) {
        this.primaryStage = primaryStage;
        this.serverPageScene = serverPageScene;
    }

    public void connect(){
        String url = getUrl();
        ConfigUtils.serverUrl = url;

    }

    /**
     * gets the url from the text field
     * @return string of url
     */
    public String getUrl(){
        return connectUrl.getText();
    }
}
