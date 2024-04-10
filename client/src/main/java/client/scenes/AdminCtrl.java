package client.scenes;

import client.utils.ServerUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.Inject;
import commons.Event;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class AdminCtrl {

    private ServerUtils server;
    private SplittyMainCtrl mainCtrl;
    private Stage primaryStage;
    private boolean eventListenersRegistered = false;
    private final FileChooser fileChooser = new FileChooser();
    private Scene admin;
    @FXML
    private ListView<Event> listView;
    private ObservableList<Event> events;
    private ResourceBundle bundle;

    private Locale currentLocale;

    @FXML
    public ComboBox<String> sortComboBox;
    @FXML
    private Button backButton;
    @FXML
    private Button lastActivityButton;
    @FXML
    private Button creationDateButton;
    @FXML
    private Button titleButton;
    @FXML
    public Text adminText;
    @FXML
    public Button importButton;
    @FXML
    public Button downloadSelectedButton;
    @FXML
    public Button selectAllButton;


    /**
     * Constructs an instance of EventsOverviewCtrl with the specified dependencies.
     *
     * @param server   The ServerUtils instance.
     * @param mainCtrl The MainCtrl instance.
     */
    @Inject
    public AdminCtrl(ServerUtils server, SplittyMainCtrl mainCtrl) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    /**
     * Initializes the page
     *
     * @param primaryStage The primary container of this page
     * @param admin     The page with its controller
     */
    public void initialize(Stage primaryStage, Scene admin) {
        fileChooser.setTitle("Save JSON File");
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"));

        this.primaryStage = primaryStage;
        this.admin = admin;

        primaryStage.setScene(admin);
        primaryStage.show();

        bundle = ResourceBundle.getBundle("messages", currentLocale);
        sortComboBox.getItems().clear();

        sortComboBox.getItems().addAll(
                bundle.getString("sortByTitle"),
                bundle.getString("sortByCreationDate"),
                bundle.getString("sortByLastActivity")
        );

        sortComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.equals(bundle.getString("sortByTitle"))) {
                sortByTitle();
            } else if (newVal.equals(bundle.getString("sortByCreationDate"))) {
                sortByCreationDate();
            } else if (newVal.equals(bundle.getString("sortByLastActivity"))) {
                sortByLastActivity();
            }
        });

        updateUI();
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listView.setCellFactory(listView -> new CustomListCell(server, mainCtrl));
        displayAllEvents();
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
            events.add(createdEvent);
            listView.setItems(events);
        }
    }

    class HandleUpdatingEvent implements Runnable {
        private final Event updatedEvent;
        public HandleUpdatingEvent(Event event) {
            this.updatedEvent = event;
        }
        @Override
        public void run() {
            events.removeIf(e -> e.getEventId() == updatedEvent.getEventId());
            events.add(updatedEvent);
            listView.setItems(events);
        }
    }

    class HandleDeletingEvent implements Runnable {
        private final Event deletedEvent;
        public HandleDeletingEvent(Event event) {
            this.deletedEvent = event;
        }
        @Override
        public void run() {
            events.removeIf(e -> e.equals(deletedEvent));
            listView.setItems(events);
        }
    }

    private class CustomListCell extends ListCell<Event> {
        @FXML
        private final Button deleteButton;
        @FXML
        private final Button goToButton;

        public CustomListCell(ServerUtils server, SplittyMainCtrl mainCtrl) {
            deleteButton = new Button("Delete");
            deleteButton.setOnAction(event -> {
                Event item = getItem();
                if (item != null) {
                    listView.getItems().remove(item);
                    server.deleteEvent(item.getEventId());
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Event deleted");
                    alert.setHeaderText(null);
                    alert.setContentText( "The event: " + event + " is deleted successfully");
                }
            });
            goToButton = new Button("->");
            goToButton.setOnAction(event -> {
                Event item = getItem();
                if (item != null) {
                    mainCtrl.showOverview(item);
                }
            });
        }


        @Override
        protected void updateItem(Event item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setText(null);
                setGraphic(null);
            } else {
                HBox container = new HBox(deleteButton, goToButton);
                setText(item.toString());
                setGraphic(container);
            }
        }
    }

    /**
     * sets the current locale
     * @param locale - the locale to set
     */
    public void setCurrentLocale(Locale locale) {
        this.currentLocale = locale;
    }

    /**
     * updates the locale
     * @param locale - the locale to update to
     */
    public void updateLocale(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle("messages", currentLocale);
        updateUI();
    }

    /**
     * Update UI to language setting
     */
    private void updateUI() {
        backButton.setText(bundle.getString("backButton"));
        adminText.setText(bundle.getString("adminText"));
        importButton.setText(bundle.getString("importButton"));
        downloadSelectedButton.setText(bundle.getString("downloadSelectedButton"));
        selectAllButton.setText(bundle.getString("selectAllButton"));
        sortComboBox.setPromptText(bundle.getString("sortPrompt"));
    }


    /**
     * displays all events from the server in the list view
     */
    public void displayAllEvents() {
        List<Event> serverEvents = server.getEvents();
        events = FXCollections.observableArrayList(serverEvents);
        listView.setItems(events);
    }

    /**
     * Goes back to the start screen
     */
    public void back(){
        mainCtrl.showStartScreen();
    }

    /**
     * getter for the list of events
     * @return the list of events
     */
    public ListView<Event> getListView() {
        return listView;
    }

    /**
     * setter for the list of events
     * @param listView the new list
     */
    public void setListView(ListView<Event> listView) {
        this.listView = listView;
    }

    /**
     * sorts the events by title
     */
    public void sortByTitle() {
        events.sort(Comparator.comparing(Event::getTitle));
        listView.setItems(events);
    }

    /**
     *sorts the events by creation date
     */
    public void sortByCreationDate() {
        events.sort(Comparator.comparing(Event::getCreationDate));
        listView.setItems(events);
    }

    /**
     * sort the events by last activity
     */
    public void sortByLastActivity() {
        events.sort(Comparator.comparing(Event::getLastActivity));
        listView.setItems(events);
    }

    /**
     * Downloads selected item
     */
    public void downloadSelected(){
        if (listView.getSelectionModel().getSelectedItems().isEmpty()) return;

       List<Long> ids = new ArrayList<>();
        String userHome = System.getProperty("user.home");
        File downloadsDir = new File(userHome, "Downloads");
        fileChooser.setInitialDirectory(downloadsDir);


        if (listView.getSelectionModel().getSelectedItems().size() == 1) {
            ids.add(listView.getSelectionModel().getSelectedItem().getEventId());
            fileChooser.setInitialFileName("event_" + String.valueOf(listView.getSelectionModel()
                    .getSelectedItem().getEventId()));
            File file = fileChooser.showSaveDialog(new Stage());
            server.downloadJSONFile(file, ids);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Event downloaded");
            alert.setHeaderText(null);
            alert.setContentText( "The selected event is downloaded successfully to " + file.getPath());
        }
        else{
            for (int i = 0; i< listView.getSelectionModel().getSelectedItems().size(); i++){
                ids.add(listView.getSelectionModel().getSelectedItems().get(i).getEventId());
            }
            fileChooser.setInitialFileName("events");
            File file = fileChooser.showSaveDialog(new Stage());
            server.downloadJSONFile(file, ids);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Events downloaded");
            alert.setHeaderText(null);
            alert.setContentText( "The selected events are downloaded successfully to " + file.getPath());
        }
    };

    /**
     * handles importing either one or a List of events
     * updates events with matching ID, and adds events without or with
     * distinct id
     * @throws IOException file reading error
     */
    public void importBackup() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        FileChooser fileChooser = new FileChooser();
        String userHome = System.getProperty("user.home");
        File downloadsDir = new File(userHome, "Downloads");
        fileChooser.setInitialDirectory(downloadsDir);
        File file = fileChooser.showOpenDialog(new Stage());

        if (file != null) {
            // Check if the file is empty
            if (file.length() == 0) {
                System.out.println("File is empty.");
                return; // Exit method
            }

            // Read the JSON content
            List<Event> eventList = new ArrayList<>();
            try {
                eventList = objectMapper.readValue(file, new TypeReference<List<Event>>() {});
            }
            catch (IOException e){
                eventList.add(objectMapper.readValue(file, Event.class)) ;
            }


            warning(eventList);


        }
    }

    /**
     * warning for overwriting and the overwriting itself
     * @param eventList
     */
    private void warning(List<Event> eventList) {
        // warning
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setContentText("Are you sure you want to add/overwrite all events " +
                "mentioned in your file?");
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        alert.getButtonTypes().setAll(noButton, yesButton);

        // Show the dialog and wait for a response
        Optional<ButtonType> result = alert.showAndWait();

        // Process the user's response
        if (result.isPresent() && result.get() == yesButton) {
            for (Event event : eventList) {
                if (server.getEventById(event.getEventId()) == null) {
                    server.addEvent(event);
                } else {
                    server.updateEvent(event.getEventId(), event);
                }
            }
            Alert alert2 = new Alert(Alert.AlertType.CONFIRMATION);
            alert2.setTitle("Events imported");
            alert2.setHeaderText(null);
            alert2.setContentText( "The events are imported successfully");
        } else {
            return;
        }
        listView.setCellFactory(listView -> new CustomListCell(server, mainCtrl));
        displayAllEvents();
    }

    /**
     *
     */
    public void selectAll(){
    listView.getSelectionModel().selectAll();
    }

    /**
     * handles common keys being pressed
     * @param e the key pressed
     */
    public void keyPressed(KeyEvent e) {
        switch(e.getCode()){
            case ESCAPE:
                back();
                break;
            case ENTER:
                downloadSelected();
                break;
            default:
                break;
        }
    }

}
