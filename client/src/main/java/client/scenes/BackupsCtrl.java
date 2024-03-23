package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;


public class BackupsCtrl {
    private ServerUtils server = new ServerUtils();


    @FXML
    private Button downloadAllButton;
    @FXML
    private Button downloadOneButton;
    @FXML
    private ChoiceBox<Long> events;
    @FXML
    private void initialize() {
        ObservableList<Long> choices = FXCollections.observableArrayList();
        for (Event event : server.getEvents()){
            choices.add(event.getEventId());
        }
        events.setItems(choices);

        // You can initialize UI elements or perform other setup here
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




}
