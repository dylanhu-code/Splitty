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
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;

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

    @FXML
    private void downloadAll() {
        downloadAllButton.setText("downloading...");
        downloadJSONFile("all");

    }
    @FXML
    private void downloadOne() {
        downloadOneButton.setText("downloading...");
        downloadJSONFile(String.valueOf(events.getValue()));

    }

    /**
     *
     * @param event event id to download. "all" for all events
     */
    @FXML
    private void downloadJSONFile(String event) {

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save JSON File");
            fileChooser.getExtensionFilters()
                    .add(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"));
            if(event == "all") {
                fileChooser.setInitialFileName("events");
            }
            else{
                fileChooser.setInitialFileName("event_"+ event);
            }
            String userHome = System.getProperty("user.home");
            File downloadsDir = new File(userHome, "Downloads");
            fileChooser.setInitialDirectory(downloadsDir);
            File file = fileChooser.showSaveDialog(new Stage());
            if (file != null) {
                String url = "http://localhost:8080/api/JSON/" + event; // Your backend service URL
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setRequestMethod("GET");
                try (InputStream inputStream = connection.getInputStream();
                     FileOutputStream outputStream = new FileOutputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(event == "all") {
                downloadAllButton.setText("Download all events");
            }
            else{
                downloadOneButton.setText("Download");
            }

        }

    }

}
