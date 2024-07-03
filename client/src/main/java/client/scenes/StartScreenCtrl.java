package client.scenes;

import client.EventStorageManager;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Tag;
import jakarta.ws.rs.WebApplicationException;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class StartScreenCtrl {
    private final ServerUtils server;
    private final SplittyMainCtrl mainCtrl;
    private Event currentEvent;
    private ResourceBundle bundle;
    ObservableList<Event> data;

    @FXML
    private TextField eventName;
    @FXML
    private TextField inviteCode;
    @FXML
    private ListView<Event> list;
    @FXML
    private Button createButton;
    @FXML
    private Button joinButton;
    @FXML
    private Text startScreenText;
    @FXML
    private Text createEventText;
    @FXML
    private Text joinEventText;
    @FXML
    private Text recentEventsText;

    private EventStorageManager storageManager;
    private boolean eventListenersRegistered = false;

    /**
     * Constructor
     *
     * @param mainCtrl - the main controller
     * @param server   - the server
     * @param storageManager - the event manager for the user-file
     */
    @Inject
    public StartScreenCtrl(SplittyMainCtrl mainCtrl, ServerUtils server,
                           EventStorageManager storageManager) {
        this.mainCtrl = mainCtrl;
        this.server = server;
        this.storageManager = storageManager;
    }

    static class Cell extends ListCell<Event> {
        HBox hbox = new HBox();
        Label label = new Label("");
        Pane pane = new Pane();
        Button btn = new Button("->");
        Button delBtn = new Button("X");

        SplittyMainCtrl mainCtrl = new SplittyMainCtrl();
        private final ServerUtils utils = new ServerUtils();
        private EventStorageManager storageManager = new EventStorageManager(utils);
        OverviewCtrl overviewCtrl = new OverviewCtrl(utils, mainCtrl, storageManager);
        private final StartScreenCtrl startScreenCtrl;

        public Cell(StartScreenCtrl startScreenCtrl) {
            super();
            this.startScreenCtrl = startScreenCtrl;
            hbox.getChildren().addAll(label, pane, delBtn, btn);
            hbox.setHgrow(pane, Priority.ALWAYS);

            delBtn.setOnAction(e -> {
                Event event = getItem();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Delete Event");
                alert.setContentText("Are you sure you want to delete this event?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    getListView().getItems().remove(event);
                    startScreenCtrl.server.sendDeleteMsg(event);
                    try {
                        storageManager.deleteEventFromFile(event.getEventId());
                    } catch (WebApplicationException err) {
                        var errorAlert = new Alert(Alert.AlertType.ERROR);
                        errorAlert.initModality(Modality.APPLICATION_MODAL);
                        errorAlert.setContentText(err.getMessage());
                        errorAlert.showAndWait();
                    }
                }
            });
            btn.setOnAction(e -> startScreenCtrl.goToSpecifiedEvent(getItem()));

            styleProperty().bind(Bindings.createStringBinding(() -> {
                if (getIndex() % 2 == 0) {
                    return "-fx-background-color: #b7f3ff;";
                } else {
                    return "-fx-background-color: #d2f7ff;";
                }
            }, indexProperty()));
        }

        public void updateItem(Event event, boolean empty) {
            super.updateItem(event, empty);
            setText(null);
            setGraphic(null);

            if (event != null && !empty) {
                label.setText(event.getTitle());
                label.setFont(new Font(16));
                setGraphic(hbox);
            }
        }
    }

    /**
     * initializing the page
     */
    public void initialize() {
        bundle = ResourceBundle.getBundle("messages", mainCtrl.getCurrentLocale());
        createEventText.setFocusTraversable(true);
        inviteCode.clear();
        List<Event> events = storageManager.getEventsFromDatabase();
        if (events != null) {

            GridPane pane = new GridPane();
            Label name = new Label("n");
            Button btn = new Button("goButton");
            Button delBtn = new Button("deleteButton");

            pane.add(name, 0, 0);
            pane.add(delBtn, 0, 1);
            pane.add(btn, 0, 2);

            list.setCellFactory(param -> new Cell(this));
            data = FXCollections.observableList(events);
        }
        list.setItems(data);
        if (!eventListenersRegistered) {
            registerEventListeners();
            eventListenersRegistered = true;
        }
    }

    /**
     * Registers event listeners for handling events via websockets.
     * This method is only called once during initialization to avoid duplicate registrations.
     */
    private void registerEventListeners() {
        server.registerForUpdates("/topic/events/create", Event.class, createdEvent -> {
            Platform.runLater(new HandleCreatingEvent(createdEvent));
        });
        server.registerForUpdates("/topic/events/deleteLocally", Event.class, deletedEvent -> {
            Platform.runLater(new HandleDeletingEventLocally(deletedEvent));
        });
        server.registerForUpdates("/topic/events/delete", Event.class, deletedEvent -> {
            System.out.println("Websockets:\n" + deletedEvent + " has been deleted!");
        });
        server.registerForUpdates("/topic/events/update", Event.class, updatedEvent -> {
            Platform.runLater(new HandleUpdatingEvent(updatedEvent));
        });
    }

    class HandleCreatingEvent implements Runnable {
        private final Event createdEvent;
        public HandleCreatingEvent(Event event) {
            this.createdEvent = event;
        }
        @Override
        public void run() {
            data.add(createdEvent);
            list.setItems(data);
            System.out.println("Websockets:\n" + createdEvent + " has been created!");
        }
    }

    class HandleUpdatingEvent implements Runnable {
        private final Event updatedEvent;
        public HandleUpdatingEvent(Event event) {
            this.updatedEvent = event;
        }
        @Override
        public void run() {
            data.removeIf(e -> e.getEventId() == updatedEvent.getEventId());
            data.add(updatedEvent);
            list.setItems(data);
            System.out.println("Websockets:\n" + updatedEvent + " has been updated!");
        }
    }

    class HandleDeletingEventLocally implements Runnable {
        private final Event deletedEvent;
        public HandleDeletingEventLocally(Event event) {
            this.deletedEvent = event;
        }
        @Override
        public void run() {
            data.removeIf(e -> e.equals(deletedEvent));
            list.setItems(data);
        }
    }

    /**
     * updates the locale
     * @param locale - the locale to update to
     */
    public void updateLocale(Locale locale) {
        bundle = ResourceBundle.getBundle("messages", locale);
        updateUI();
    }

    /**
     * Update the contents of the elements to the language
     */
    private void updateUI() {
        eventName.setPromptText(bundle.getString("eventName"));
        inviteCode.setPromptText(bundle.getString("inviteCode"));
        createButton.setText(bundle.getString("createButtonText"));
        joinButton.setText(bundle.getString("joinButtonText"));
        createEventText.setText(bundle.getString("createEventText"));
        joinEventText.setText(bundle.getString("joinEventText"));
        recentEventsText.setText(bundle.getString("recentEventsText"));
    }

    /**
     * used for the "create" button, to create a new event.
     */
    @FXML
    public void createEvent() {
        try {
            currentEvent = getEvent();
            currentEvent = server.addEvent(currentEvent);
            Tag tagFood = new Tag("food", "green", currentEvent.getEventId());
            Tag tagFees = new Tag("entrance fees", "blue", currentEvent.getEventId());
            Tag tagTravel = new Tag("travel", "red", currentEvent.getEventId());
            Tag debtSettlement = new Tag("debt settlement", "grey", currentEvent.getEventId());
            tagFood = server.addTag(tagFood);
            tagFees = server.addTag(tagFees);
            tagTravel = server.addTag(tagTravel);
            debtSettlement = server.addTag(debtSettlement);
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        storageManager.saveEventIdToFile(currentEvent.getEventId());
        clearFields();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Event added");
        alert.setHeaderText(null);
        alert.setContentText( "The event: " + currentEvent.getTitle() + " is added successfully");
        alert.showAndWait();
        mainCtrl.showOverview(currentEvent, "-1"); //TODO change to initalize specific overview
    }

    /**
     * Getter for the event
     *
     * @return - the event
     */
    public Event getEvent() {
        var name = eventName.getText();
        currentEvent = new Event(name);

        return currentEvent;
        //TODO will still need to add the user that created the event to the list of participants
    }

    /**
     * used for the "join" button, to join an event using an invite code.
     */
    @FXML
    public void joinEvent() {
        var code = inviteCode.getText();
        try {
            Event inviteEvent = server.getEventByInviteCode(code);
            currentEvent = inviteEvent;
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;

        }
        storageManager.saveEventIdToFile(currentEvent.getEventId());
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Event joined");
        alert.setHeaderText(null);
        alert.setContentText( "You successfully joined the event: " + currentEvent.getTitle());
        alert.showAndWait();
        mainCtrl.showOverview(currentEvent, "-1");
        //TODO currently it just goes to the event menu, the ideal case is that the event
        // should be added to the recently viewed

    }

    /**
     * clears both fields of any inputted text.
     */
    public void clearFields() {
        eventName.clear();
        inviteCode.clear();
    }

    /**
     * shows an event //TODO should still be altered to show specific event
     */
    public void showEvent() {
        mainCtrl.showOverview(getEvent(), "-1");
    }

    /**
     * Goes to the specific event overview, when go button clicked
     *
     * @param event - specific event to go to
     */
    public void goToSpecifiedEvent(Event event) {
        mainCtrl.showOverview(event, "-1");
    }

    /**
     * refreshes the start screen
     */
    public void refresh(){
            var events = storageManager.getEventsFromDatabase();
            data = FXCollections.observableList(events);
            list.setItems(data);
            //TODO should be changed to only get the events of a specific user
    }

    /**
     * takes action when common keys are pressed.
     * @param e the key event that is taken
     */
    public void keyPressed(KeyEvent e) {
        switch (e.getCode()){
            case ENTER:
                if (eventName.isFocused()) {
                    createEvent();
                    break;
                }
                if(inviteCode.isFocused()){
                    joinEvent();
                    break;
                }
            case R:
                if (e.isControlDown()){
                    refresh();
                }
                break;
            default:
                break;
        }
    }
}
