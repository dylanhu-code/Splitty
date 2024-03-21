package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import jakarta.ws.rs.WebApplicationException;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;


public class StartScreenCtrl {
    private final ServerUtils server;
    private final SplittyMainCtrl mainCtrl;

    @FXML
    private TextField eventName;
    @FXML
    private TextField inviteCode;
    @FXML
    private ListView<Event> list;
    @FXML
    private Button flagButton;
    private String[] flagImageNames = {"/en_flag.png", "/bg_flag.png", "/nl_flag.png"};
    private int currentFlagIndex = 0;
    private ResourceBundle bundle;

    ObservableList<Event> data;

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
    private Event currentEvent;

    /**
     * Constructor
     *
     * @param mainCtrl - the main controller
     * @param server   - the server
     */
    @Inject
    public StartScreenCtrl(SplittyMainCtrl mainCtrl, ServerUtils server) {
        this.mainCtrl = mainCtrl;
        this.server = server;
    }

    static class Cell extends ListCell<Event> {
        HBox hbox = new HBox();
        Label label = new Label("");
        Pane pane = new Pane();
        Button btn = new Button("Go");
        Button delBtn = new Button("X");

        SplittyMainCtrl mainCtrl = new SplittyMainCtrl();
        OverviewCtrl overviewCtrl = new OverviewCtrl(mainCtrl);
        ServerUtils utils = new ServerUtils();
        private final StartScreenCtrl startScreenCtrl;

        public Cell(StartScreenCtrl startScreenCtrl) {

            super();
            this.startScreenCtrl = startScreenCtrl;
            hbox.getChildren().addAll(label, pane, delBtn, btn);
            hbox.setHgrow(pane, Priority.ALWAYS);

            delBtn.setOnAction(e -> {
                Event event = getItem();
                getListView().getItems().remove(event);
                try {
                    utils.deleteEvent(event.getEventId());
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
     */
    public void initialize() {
        putFlag("/en_flag.png");
        flagButton.setOnAction(event -> changeFlagImage());

        List<Event> events = server.getEvents();
        if (events != null) {
            bundle = ResourceBundle.getBundle("messages");
            ObservableList<Event> data = FXCollections.observableArrayList(events);
            list.setItems(data);

            list.setItems(data);
            GridPane pane = new GridPane();
            Label name = new Label("n");
            Button btn = new Button("goButton");
            Button delBtn = new Button("deleteButton");

            pane.add(name, 0, 0);
            pane.add(delBtn, 0, 1);
            pane.add(btn, 0, 2);

            list.setCellFactory(param -> new Cell(this));
        }

    }

    /**
     * Change the image path, call the update UI method and do the animation
     */
    private void changeFlagImage() {
        ScaleTransition shrinkTransition = new ScaleTransition(Duration.millis(150), flagButton);
        shrinkTransition.setToY(0);
        shrinkTransition.setOnFinished(event -> {
            currentFlagIndex = (currentFlagIndex + 1) % flagImageNames.length;
            putFlag(flagImageNames[currentFlagIndex]);

            switchResourceBundle();
            ScaleTransition restoreTransition = new
                    ScaleTransition(Duration.millis(150), flagButton);
            restoreTransition.setToY(1);
            restoreTransition.setOnFinished(e -> {
                updateUI();
            });
            restoreTransition.play();
        });
        shrinkTransition.play();
    }

    /**
     * Change the properties file based on the image change of the bundle
     */
    private void switchResourceBundle() {
        switch (currentFlagIndex) {
            case 0:
                bundle = ResourceBundle.getBundle("messages");
                break;
            case 1:
                bundle = ResourceBundle.getBundle("messages_bg", new Locale("bg"));
                break;
            case 2:
                bundle = ResourceBundle.getBundle("messages_nl", new Locale("nl"));
                break;
            default:
                bundle = ResourceBundle.getBundle("messages");
        }
    }

    /**
     * Update the contents of the elements to the language
     */
    private void updateUI() {
        eventName.setPromptText(bundle.getString("eventName"));
        inviteCode.setPromptText(bundle.getString("inviteCode"));
        createButton.setText(bundle.getString("createButtonText"));
        joinButton.setText(bundle.getString("joinButtonText"));
        startScreenText.setText(bundle.getString("startScreenText"));
        createEventText.setText(bundle.getString("createEventText"));
        joinEventText.setText(bundle.getString("joinEventText"));
        recentEventsText.setText(bundle.getString("recentEventsText"));
    }

    /**
     * Put a new Image in the button
     * @param path of the image
     */
    public void putFlag(String path) {
        Image image = new Image(path);
        ImageView imageView = new ImageView(image);

        BackgroundSize backgroundSize = new BackgroundSize(100, 100, true, true, true, false);
        BackgroundImage backgroundImage = new
                BackgroundImage(imageView.snapshot(null, null),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER, backgroundSize);

        flagButton.setBackground(new Background(backgroundImage));
    }

    /**
     * used for the "create" button, to create a new event.
     */
    public void createEvent() {
        try {

            currentEvent = server.addEvent(getEvent());
        } catch (WebApplicationException e) {
            var alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return;
        }
        //TODO make sure this works, currently gives 500 internal server error.
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
        mainCtrl.showOverview(getEvent());
        //TODO needs to be finished when invite functionality is implemented,
        // currently just goes to overview
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
     * @param event - specific event to go to
     */
    public  void goToSpecifiedEvent(Event event) {
        mainCtrl.showOverview(event);
    }
}
