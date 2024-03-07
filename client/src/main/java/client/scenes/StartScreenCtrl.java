package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.fxml.FXML;
import jakarta.ws.rs.WebApplicationException;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Modality;

public class StartScreenCtrl {
    private final ServerUtils server;
    private final SplittyMainCtrl mainCtrl;

    @FXML
    private TextField eventName;
    @FXML
    private TextField inviteCode;
    @FXML
    private ListView<Event> list;

    @Inject
    public StartScreenCtrl(SplittyMainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * used for the "create" button, to create a new event.
     */
    public void createEvent() {
        try {
            server.addEvent(getEvent());
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }

        clearFields();
        mainCtrl.showOverview();
    }

    public Event getEvent() {
        var name = eventName.getText();
        return new Event(name);
        //TODO will still need to add the user that created the event to the list of participants
    }

    /**
     * used for the "join" button, to join an event using an invite code.
     */
    public void joinEvent() {
        var code = inviteCode.getText();
        //TODO needs to be finished when invite functionality is implemented
    }

    /**
     * clears both fields of any inputted text.
     */
    public void clearFields(){
        eventName.clear();
        inviteCode.clear();
    }

}
