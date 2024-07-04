package client.scenes;

import client.utils.ConfigUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


public class ServerPageCtrl {
    SplittyMainCtrl mainCtrl;
    ServerUtils utils;

    @FXML
    private TextField connectUrl;
    @FXML
    private Button connectButton;

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

    /**
     * Connects to the server using the specified URL.
     */
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
