package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.fxml.FXML;
import jakarta.ws.rs.WebApplicationException;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;

public class StartScreenCtrl {
    private final ServerUtils server;
    private final MainCtrl mainCtrl;

    @FXML
    private TextField eventName;
    @FXML
    private TextField inviteCode;
    @FXML
    private ListView<Event> list;

    @Inject
    public StartScreenCtrl(MainCtrl mainCtrl, ServerUtils server){
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * used for the "create" button, to create a new event.
     */
    public void createEvent(){

    }

    /**
     * used for the "join" button, to join an event using an invite code.
     */
    public void joinEvent(){

    }

}
