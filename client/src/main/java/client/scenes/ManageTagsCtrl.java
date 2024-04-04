package client.scenes;

import client.utils.ServerUtils;
import com.google.inject.Inject;
import commons.Event;
import commons.Expense;
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

    /**
     * Constructor
     * @param mainCtrl - splitty main ctrl
     * @param utils - server utils
     */
    @Inject
    public ManageTagsCtrl(SplittyMainCtrl mainCtrl, ServerUtils utils) {
        this.mainCtrl = mainCtrl;
        this.utils = utils;
    }

    /**
     * initializes tags scene
     * @param primaryStage - primary stage
     * @param tagsScene - scene
     */
    public void initialize(Stage primaryStage, Scene tagsScene) {
        this.primaryStage = primaryStage;
        this.tagsScene = tagsScene;
    }

    /**
     * updates the data in the scene
     * @param event - specific event
     */
    public void updateSceneData(Event event) {
        this.event = event;
        titleEvent.setText(event.getTitle() + " tags");
        setupListView();
    }

    /**
     * set ups the list view for the tags
     */
    private void setupListView() {
        List<Tag> tags = utils.getTags(event);
        ObservableList<Tag> tagsList = FXCollections.observableArrayList(tags);

        listTags.setItems(tagsList);

        listTags.setCellFactory(param -> new TagCell(event, utils));
    }

    /**
     * sets up the scene
     */
    public void initScene() {
        primaryStage.setScene(tagsScene);
        primaryStage.show();
    }

    /**
     * returns to overview page
     */

    public void goBack() {
       mainCtrl.showOverview(event);
    }

    /**
     * Displays a dialog to add a tag
     */
    public void addTag() {
        Dialog<Tag> dialog = new Dialog<>();
        dialog.setTitle("Add Tag");
        dialog.setHeaderText("Enter tag name and select color");

        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        TextField tagNameField = new TextField();
        tagNameField.setPromptText("Tag Name");

        ColorPicker colorPicker = new ColorPicker(Color.WHITE);
        colorPicker.setPromptText("Select Color");

        GridPane grid = new GridPane();
        grid.add(new Label("Tag Name:"), 0, 0);
        grid.add(tagNameField, 1, 0);
        grid.add(new Label("Tag Color:"), 0, 1);
        grid.add(colorPicker, 1, 1);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                String tagName = tagNameField.getText().trim();
                if (tagName.isEmpty()) {
                    showAlert("Tag name cannot be empty.");
                    return null;
                }
                String color = "#" + Integer.toHexString(colorPicker.getValue().hashCode());
                return new Tag(tagName, color, event.getEventId());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(newTag -> {
            try {
                utils.addTag(newTag);
                updateSceneData(event);
            } catch (WebApplicationException e) {
                showAlert("Error occurred while adding the tag: " + e.getMessage());
            }
        });
    }

    /**
     * Displays an alert message
     * @param message - particular message to display
     */
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

        /**
         * Contructor for the tag cell in the list view
         * @param specificEvent - specific event
         * @param utils - server utils
         */
        public TagCell(Event specificEvent,ServerUtils utils ) {
            this.currentEvent = specificEvent;
            this.utils = utils;
            editButton.setOnAction(e -> {
                Tag tagToEdit = getItem();
                if (tagToEdit != null) {
                    editTag(tagToEdit);
                }});
            deleteButton.setOnAction(e1 -> {
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

        /**
         * Deletes the tag selected
         * @param tag - tag selected
         */
        public void deleteTag(Tag tag) {
            long id = tag.getId();
            try {
                boolean tagAssignedToExpense = currentEvent.getExpenses().stream()
                        .anyMatch(expense -> expense.getTag().equals(tag));

                if (tagAssignedToExpense) {
                    var alert = new Alert(Alert.AlertType.ERROR);
                    alert.initModality(Modality.APPLICATION_MODAL);
                    alert.setContentText("This tag is assigned to an expense in the event. " +
                            "It cannot be deleted.");
                    alert.showAndWait();
                    return;
                }
                utils.deleteTag(id);
            } catch (WebApplicationException e) {
                var alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setContentText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    /**
     * Edits the selected tag
     * @param tag - tag that is selected
     */
    private void editTag(Tag tag) {
        Dialog<Tag> dialog = new Dialog<>();
        dialog.setTitle("Edit Tag");
        dialog.setHeaderText("Edit tag name and color");

        ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        TextField tagNameField = new TextField(tag.getName());
        ColorPicker colorPicker = new ColorPicker(Color.web(tag.getColor()));

        GridPane grid = new GridPane();
        grid.add(new Label("Tag Name:"), 0, 0);
        grid.add(tagNameField, 1, 0);
        grid.add(new Label("Tag Color:"), 0, 1);
        grid.add(colorPicker, 1, 1);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == saveButton) {
                String newName = tagNameField.getText().trim();
                if (newName.isEmpty()) {
                    showAlert("Tag name cannot be empty.");
                    return null;
                }
                String newColor = "#" + Integer.toHexString(colorPicker.getValue().hashCode());
                Tag tag1 =  new Tag(newName, newColor, tag.getEvent());
                tag1.setId(tag.getId());
                return tag1;
            }
            return null;
        });

        dialog.showAndWait().ifPresent(editedTag -> {
            try {
                utils.updateTags(editedTag.getId(), editedTag);
                for (Expense expense : event.getExpenses()) {
                    if (expense.getTag().getId() == tag.getId()) {
                        expense.getTag().setName(editedTag.getName());
                        expense.getTag().setColor(editedTag.getColor());
                    }
                }
                updateSceneData(event);
            } catch (WebApplicationException e) {
                showAlert("Error occurred while updating the tag: " + e.getMessage());
            }
        });
    }

}
