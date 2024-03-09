package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import jakarta.ws.rs.WebApplicationException;
import javafx.scene.control.*;
import javafx.scene.input.TouchEvent;
import javafx.stage.Modality;
import javafx.scene.layout.*;
public class StartScreenCtrl {
    private final ServerUtils server;
    private final SplittyMainCtrl mainCtrl;

    @FXML
    private TextField eventName;
    @FXML
    private TextField inviteCode;
    @FXML
    private ListView<String> list;

    @Inject
    public StartScreenCtrl(SplittyMainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    static class Cell extends ListCell<String>{
        HBox hbox = new HBox();
        Label label = new Label("");
        Pane pane = new Pane();
        Button btn = new Button("Go");

        public Cell(){
            super();

            hbox.getChildren().addAll(label, pane, btn);
            hbox.setHgrow(pane, Priority.ALWAYS);
        }

        public void updateItem(String name, boolean empty){
            super.updateItem(name, empty);
            setText(null);
            setGraphic(null);

            if(name != null && !empty){
                label.setText(name);
                setGraphic(hbox);
            }
        }
    }

    public void initialize(){
        ObservableList<String> testItems = FXCollections.observableArrayList("Holiday", "Ski trip", "Bowling");
        list.setItems(testItems);
        GridPane pane = new GridPane();
        Label name = new Label("n");
        Button btn = new Button("ButtonIn");

        pane.add(name, 0, 0);
        pane.add(btn, 0, 1);

        list.setCellFactory(param -> new Cell());

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
        //TODO make sure this works, currently gives 500 internal server error.
        clearFields();
        mainCtrl.showOverview(); //TODO change to initalize specific overview
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
        mainCtrl.showOverview();
        //TODO needs to be finished when invite functionality is implemented, currently just goes to overview
    }

    /**
     * clears both fields of any inputted text.
     */
    public void clearFields(){
        eventName.clear();
        inviteCode.clear();
    }

    /**
     * shows an event //TODO should still be altered to show specific event
     */
    public void showEvent() {
        mainCtrl.showOverview();
    }
}
