package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Tag;
import jakarta.ws.rs.WebApplicationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;
import java.util.Set;

public class ManageTagsCtrl {
    @FXML
    private Button goBackButton;
    @FXML
    private Button addTagButton;
    @FXML
    private Label titleEvent;
    @FXML
    private ListView<Tag> listTags;

    private SplittyMainCtrl mainCtrl;
    private ServerUtils utils;
    private Scene tagsScene;
    private Stage primaryStage;
    private Event event;

    @Inject
    public ManageTagsCtrl(SplittyMainCtrl mainCtrl, ServerUtils utils) {
        this.mainCtrl = mainCtrl;
        this.utils = utils;
    }

    public void initialize(Stage primaryStage, Scene tagsScene) {
        this.primaryStage = primaryStage;
        this.tagsScene = tagsScene;
    }
    public void updateSceneData(Event event) {
        this.event = event;
        titleEvent.setText(event.getTitle() + " tags");
        setupListView();
    }

    private void setupListView() {
        Set<Tag> tags = event.getTags();
        ObservableList<Tag> tagsList = FXCollections.observableArrayList(tags);

        listTags.setItems(tagsList);

        listTags.setCellFactory(param -> new TagCell(event, utils));
    }

    public void initScene() {
        primaryStage.setScene(tagsScene);
        primaryStage.show();
    }

    public void goBack() {
       mainCtrl.showOverview(event);
    }
    public void addTag() {
        // Create a dialog for adding tags
        Dialog<Tag> dialog = new Dialog<>();
        dialog.setTitle("Add Tag");
        dialog.setHeaderText("Enter tag name and select color");

        // Set the button types
        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // Create and configure the name field
        TextField tagNameField = new TextField();
        tagNameField.setPromptText("Tag Name");

        // Create and configure the color picker
        ColorPicker colorPicker = new ColorPicker(Color.WHITE);
        colorPicker.setPromptText("Select Color");

        // Create layout for the dialog content
        GridPane grid = new GridPane();
        grid.add(new Label("Tag Name:"), 0, 0);
        grid.add(tagNameField, 1, 0);
        grid.add(new Label("Tag Color:"), 0, 1);
        grid.add(colorPicker, 1, 1);
        dialog.getDialogPane().setContent(grid);

        // Convert the result to a tag when the add button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                String tagName = tagNameField.getText().trim();
                if (tagName.isEmpty()) {
                    showAlert("Tag name cannot be empty.");
                    return null;
                }
                String color = "#" + Integer.toHexString(colorPicker.getValue().hashCode());
                return new Tag(tagName, color);
            }
            return null;
        });

        // Show the dialog and process the result
        dialog.showAndWait().ifPresent(newTag -> {
            try {
                Tag savedTag = utils.addTag(newTag);
                Set<Tag> eventTags = event.getTags();
                eventTags.add(savedTag);
                event.setTags(eventTags);
                // Add the new tag to the database
                event = utils.updateEvent(event.getEventId(), event);
                // Update the event with the new tag
                // Update the scene data
                updateSceneData(event);
            } catch (WebApplicationException e) {
                showAlert("Error occurred while adding the tag: " + e.getMessage());
            }
        });
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public class TagCell extends ListCell<Tag> {
        private final Button editButton = new Button("Edit");
        private final Button deleteButton = new Button("Delete");
        private final HBox hbox = new HBox();
        private final Label label = new Label();
        private final StackPane stackPane = new StackPane();
        private final Rectangle colorRectangle = new Rectangle(100, 30);
        private Event currentEvent;
        private ServerUtils utils;

        public TagCell(Event specificEvent,ServerUtils utils ) {
            this.currentEvent = specificEvent;
            this.utils = utils;
            editButton.setOnAction(event -> {
                // Handle edit action
            });

            deleteButton.setOnAction(event -> {
                Tag tagToDelete = getItem();
                if (tagToDelete != null) {
                    deleteTag(tagToDelete);
                    updateSceneData(currentEvent);
                }
            });

            hbox.getChildren().addAll(stackPane, editButton, deleteButton);
            HBox.setHgrow(stackPane, Priority.ALWAYS);
            HBox.setHgrow(editButton, Priority.NEVER);
            HBox.setHgrow(deleteButton, Priority.NEVER);
        }

        @Override
        protected void updateItem(Tag item, boolean empty) {
            super.updateItem(item, empty);

            if (empty || item == null) {
                setGraphic(null);
                setText(null);
            } else {
                label.setText(item.getName());
                label.setStyle("-fx-text-fill: white; -fx-alignment: center;");

                colorRectangle.setFill(Color.web(item.getColor()));
                colorRectangle.setArcWidth(20);
                colorRectangle.setArcHeight(20);

                stackPane.getChildren().setAll(colorRectangle, label);

                setGraphic(hbox);
            }
        }
        public void deleteTag(Tag tag) {
            long id = tag.getId();
            try {

                currentEvent.removeTag(tag);
                currentEvent = utils.updateEvent(currentEvent.getEventId(), currentEvent);
                utils.deleteTag(id);

            }catch (WebApplicationException e) {
                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
                return;
            }
        }
    }

}
