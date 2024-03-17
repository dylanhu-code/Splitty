package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import jakarta.ws.rs.WebApplicationException;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.TouchEvent;
import javafx.stage.Modality;
import javafx.scene.layout.*;
import javafx.util.Pair;

import java.util.ArrayList;

public class StartScreenCtrl {
    private final ServerUtils server;
    private final SplittyMainCtrl mainCtrl;

    @FXML
    private TextField eventName;
    @FXML
    private TextField inviteCode;
    @FXML
    private ListView<Event> list;

    ObservableList<Event> data;

    @Inject
    public StartScreenCtrl(SplittyMainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    static class Cell extends ListCell<Event>{
        HBox hbox = new HBox();
        Label label = new Label("");
        Pane pane = new Pane();
        Button btn = new Button("Go");
        Button delBtn = new Button("X");

        SplittyMainCtrl mainCtrl = new SplittyMainCtrl();
        OverviewCtrl overviewCtrl = new OverviewCtrl(mainCtrl);

        public Cell(){
            super();
            hbox.getChildren().addAll(label, pane, delBtn, btn);
            hbox.setHgrow(pane, Priority.ALWAYS);

            delBtn.setOnAction(e -> getListView().getItems().remove(getItem()));
            //btn.setOnAction(e -> overviewCtrl.initialize());
            // TODO make this go to the event pressed. probably with getEvent() and showEvent()
        }

        public void updateItem(Event event, boolean empty){
            super.updateItem(event, empty);
            setText(null);
            setGraphic(null);

            if(event != null && !empty){
                label.setText(event.getTitle());
                setGraphic(hbox);
            }
        }
    }

    public void initialize(){
        Event test1 = new Event("Holiday");
        Event test2 = new Event("Ski trip");
        Event test3 = new Event("Bowling");

        data = FXCollections.observableArrayList(test1, test2, test3);

        list.setItems(data);
        GridPane pane = new GridPane();
        Label name = new Label("n");
        Button btn = new Button("goButton");
        Button delBtn = new Button("deleteButton");

        pane.add(name, 0, 0);
        pane.add(delBtn, 0, 1);
        pane.add(btn, 0, 2);

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
