package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;

public class BackupsCtrl {
    private ServerUtils server = new ServerUtils();

    @FXML
    private Button downloadButton;

    @FXML
    private void initialize() {
        // You can initialize UI elements or perform other setup here
    }

    @FXML
    private void handleDownloadButtonClick() {
        downloadJSONFile();
        downloadButton.setText("downloading...");
    }

    @FXML
    private void downloadJSONFile() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save JSON File");
            fileChooser.getExtensionFilters()
                    .add(new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json"));
            fileChooser.setInitialFileName("events");
            File file = fileChooser.showSaveDialog(new Stage());
            if (file != null) {
                String url = "http://localhost:8080/api/JSON/all"; // Your backend service URL
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

    }

}
