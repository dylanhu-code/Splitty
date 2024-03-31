package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import static client.scenes.SplittyMainCtrl.currentLocale;
import static javafx.scene.input.KeyCode.ESCAPE;

import java.util.ResourceBundle;

import java.io.*;


public class BackupsCtrl {
    private ServerUtils server;
    private SplittyMainCtrl mainCtrl;
    private Stage primaryStage;
    private Scene backupsScene;
    private ResourceBundle bundle;

    @FXML
    private Button downloadAllButton;
    @FXML
    private Button downloadOneButton;
    @FXML
    private ChoiceBox<Long> events;
    @FXML
    public Button backButton;

    /**
     * Constructs an instance of AdminCtrl with the specified dependencies.
     *
     * @param server   The ServerUtils instance.
     * @param mainCtrl The MainCtrl instance.
     */
    @Inject
    public BackupsCtrl(ServerUtils server, SplittyMainCtrl mainCtrl) {
        this.server = server;
        this.mainCtrl = mainCtrl;
    }

    /**
     * Initializes the page
     *
     * @param primaryStage The primary container of this page.
     * @param backupsScene     The page with its controller.
     */
    public void initialize(Stage primaryStage, Scene backupsScene) {
        this.primaryStage = primaryStage;
        this.backupsScene = backupsScene;

        primaryStage.setScene(backupsScene);
        primaryStage.show();

        ObservableList<Long> choices = FXCollections.observableArrayList();

        bundle = ResourceBundle.getBundle("messages", currentLocale);
        updateUI();

        for (Event event : server.getEvents()){
            choices.add(event.getEventId());
        }
        events.setItems(choices);

        // You can initialize UI elements or perform other setup here
    }

    /**
     * Update UI to language setting
     */
    public void updateUI() {
        downloadAllButton.setText(bundle.getString("downloadAllButton"));
        downloadOneButton.setText(bundle.getString("downloadOneButton"));
        backButton.setText(bundle.getString("abortBackupsButton"));
    }
    /**
     * Downloads all events
     */
    @FXML
    public void downloadAll() {
        downloadAllButton.setText("...");
        server.downloadJSONFile(downloadJSONFile("all"), "all");
        downloadAllButton.setText("Download all events");

    }

    /**
     * Downloads one event that you can select in the ui
     */
    @FXML
    public void downloadOne() {
        downloadOneButton.setText("...");
        server.downloadJSONFile(downloadJSONFile(String.valueOf(events.getValue())),
                String.valueOf(events.getValue()));
        downloadOneButton.setText("Download");

    }

    /**
     * method that determines a file destination and name
     * @param event event id in String format (to name the file)
     * @return File
     */
    @FXML
    private File downloadJSONFile(String event) {


        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save JSON File");
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"));
        if (event == "all") {
            fileChooser.setInitialFileName("events");
        } else {
            fileChooser.setInitialFileName("event_" + event);
        }
        String userHome = System.getProperty("user.home");
        File downloadsDir = new File(userHome, "Downloads");
        fileChooser.setInitialDirectory(downloadsDir);
        File file = fileChooser.showSaveDialog(new Stage());

        return file;
    }

    /**
     * Goes back to the start screen
     */
    public void back() {
        mainCtrl.showStartScreen();
    }

    public void keyPressed(KeyEvent e) {
        if (e.getCode() == ESCAPE){
            back();
        }
    }
}
