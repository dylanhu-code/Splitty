package client.scenes;

import client.utils.ServerUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

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
        server.backupAll();
    }
}
