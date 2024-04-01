package client.scenes;

import client.EventStorageManager;
import client.utils.ConfigUtils;
import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Tag;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import jakarta.ws.rs.WebApplicationException;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.ResourceBundle;
import java.util.Locale;
import javafx.scene.image.ImageView;

import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

import javafx.stage.Modality;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import static client.scenes.SplittyMainCtrl.currentLocale;

import java.util.*;

public class StartScreenCtrl {
    private final ServerUtils server;
    private final SplittyMainCtrl mainCtrl;
    private Event currentEvent;
    private String[] languages = {"English", "Dutch", "Bulgarian"};
    private ResourceBundle bundle;
    ObservableList<Event> data;

    @FXML
    private TextField eventName;
    @FXML
    private TextField inviteCode;
    @FXML
    private ListView<Event> list;
    @FXML
    private Button flagButton;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private Button adminButton;
    @FXML
    private Button refreshButton;
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
    private ConfigUtils configUtils;

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
                getListView().getItems().remove(event);
                startScreenCtrl.server.sendDeleteMsg(event);
                try {
                    storageManager.deleteEventFromFile(event.getEventId());
                } catch (WebApplicationException err) {
                    var alert = new Alert(Alert.AlertType.ERROR);
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.setContentText(err.getMessage());
                    alert.showAndWait();
                    return;
                }
            });
            btn.setOnAction(e -> startScreenCtrl.goToSpecifiedEvent(getItem()));
            // TODO make this go to the event pressed. probably with getEvent() and showEvent()
        }

        public void updateItem(Event event, boolean empty) {
            super.updateItem(event, empty);
            setText(null);
            setGraphic(null);

            if (event != null && !empty) {
                label.setText(event.getTitle());
                setGraphic(hbox);
            }
        }
    }

    /**
     * initializing the page
     * @param primaryStage The primary container of this page
     * @param startscreen  The page with its controller
     */
    public void initialize(Stage primaryStage, Scene startscreen) {
        currentLocale = new Locale(ConfigUtils.readPreferredLanguage("client/config.txt"));
        ConfigUtils.preferredLanguage = ConfigUtils.readPreferredLanguage("client/config.txt");

        bundle = ResourceBundle.getBundle("messages", currentLocale);
        updateUI();

        createEventText.setFocusTraversable(true);

        changeFlagImage();
        comboBox.setValue(currentLocale.getDisplayLanguage());
        comboBox.setItems(FXCollections.observableArrayList(languages));

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
        primaryStage.setScene(startscreen);
        primaryStage.show();
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
            Platform.runLater(new HandleDeletingEvent(deletedEvent));
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

    class HandleDeletingEvent implements Runnable {
        private final Event deletedEvent;
        public HandleDeletingEvent(Event event) {
            this.deletedEvent = event;
        }
        @Override
        public void run() {
            data.removeIf(e -> e.equals(deletedEvent));
            list.setItems(data);
            System.out.println("Websockets:\n" + deletedEvent + " has been deleted!");
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

    @FXML
    private void handleComboBoxAction(javafx.event.ActionEvent actionEvent) {
        String selectedLanguage = comboBox.getSelectionModel().getSelectedItem();
        if (selectedLanguage != null) {
            switch (selectedLanguage) {
                case "English":
                    currentLocale = new Locale("en");
                    ConfigUtils.preferredLanguage = "en";
                    break;
                case "Dutch":
                    currentLocale = new Locale("nl");
                    ConfigUtils.preferredLanguage = "nl";
                    break;
                case "Bulgarian":
                    currentLocale = new Locale("bg");
                    ConfigUtils.preferredLanguage = "bg";
                    break;
            }
            changeFlagImage();
            bundle = ResourceBundle.getBundle("messages", currentLocale);
            updateUI();
        }
    }

    /**
     * open combo box when the button is clicked
     */
    @FXML
    private void flagClick() {
        comboBox.show();
    }

    /**
     * Change the image path, call the update UI method and do the animation
     */
    private void changeFlagImage() {
        ScaleTransition shrinkTransition = new ScaleTransition(Duration.millis(100), flagButton);
        shrinkTransition.setToY(0);
        shrinkTransition.setOnFinished(event -> {
            putFlag();
            ScaleTransition restoreTransition = new
                    ScaleTransition(Duration.millis(100), flagButton);
            restoreTransition.setToY(1);
            restoreTransition.play();
        });
        shrinkTransition.play();
    }

    /**
     * Put a new Image in the button
     */
    public void putFlag() {
        String imagePath;
        String language = currentLocale.getLanguage();
        imagePath = switch (language) {
            case "bg" -> "bg_flag.png";
            case "nl" -> "nl_flag.png";
            default -> "en_flag.png";
        };
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);

        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new
                BackgroundImage(imageView.snapshot(null, null),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, backgroundSize);

        flagButton.setBackground(new Background(backgroundImage));
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
        refreshButton.setText(bundle.getString("refreshButton"));
        adminButton.setText(bundle.getString("adminButton"));
    }

    /**
     * used for the "create" button, to create a new event.
     */
    public void createEvent() {
        try {
            currentEvent = getEvent();
            Set<Tag> tags = Set.of(new Tag("food", "green"),
                    new Tag("entrance fees", "blue"),
                    new Tag("travel", "red"));
            currentEvent.setTags(tags);
            currentEvent = server.addEvent(currentEvent);
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        //TODO make sure this works, currently gives 500 internal server error.
        storageManager.saveEventIdToFile(currentEvent.getEventId());
        clearFields();
        mainCtrl.showOverview(currentEvent); //TODO change to initalize specific overview
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
        mainCtrl.showOverview(currentEvent);
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
        mainCtrl.showOverview(getEvent());
    }

    /**
     * Goes to the specific event overview, when go button clicked
     *
     * @param event - specific event to go to
     */
    public void goToSpecifiedEvent(Event event) {
        mainCtrl.showOverview(event);
    }

    /**
     * Goes to the admin login page
     */
    public void adminOption() {mainCtrl.showAdminLogin();}

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
