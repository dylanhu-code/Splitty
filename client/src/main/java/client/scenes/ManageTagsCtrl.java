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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.*;

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
    private ResourceBundle bundle;
    private Locale currentLocale;


    /**
     * Constructor
     *
     * @param mainCtrl - splitty main ctrl
     * @param utils    - server utils
     */
    @Inject
    public ManageTagsCtrl(SplittyMainCtrl mainCtrl, ServerUtils utils) {
        this.mainCtrl = mainCtrl;
        this.utils = utils;
    }

    /**
     * initializes tags scene
     *
     * @param primaryStage - primary stage
     * @param tagsScene    - scene
     */
    public void initialize(Stage primaryStage, Scene tagsScene) {
        this.primaryStage = primaryStage;
        this.tagsScene = tagsScene;
    }

    /**
     * updates the data in the scene
     *
     * @param event - specific event
     */
    public void updateSceneData(Event event) {
        this.event = event;
        titleEvent.setText(event.getTitle() + " tags");
        setupListView();
    }

    /**
     * sets up the list view for the tags
     */
    private void setupListView() {
        List<Tag> tags = utils.getTags(event);

        Iterator<Tag> iterator = tags.iterator();
        while (iterator.hasNext()) {
            Tag tag = iterator.next();
            if ("debt settlement".equals(tag.getName())) {
                iterator.remove(); // Remove the debt settlement tag from the list, as
                // this tag is only meant for settling debts.
                break;
            }
        }

        ObservableList<Tag> tagsList = FXCollections.observableArrayList(tags);

        listTags.setItems(tagsList);

        listTags.setCellFactory(param -> new TagCell(event, utils, currentLocale));
    }

    /**
     * sets up the scene
     */
    public void initScene() {
        bundle = ResourceBundle.getBundle("messages", currentLocale);
        updateUI();
        primaryStage.setScene(tagsScene);
        primaryStage.show();
    }

    /**
     * returns to overview page
     */

    public void goBack() {
        mainCtrl.showOverview(event, "-1");
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
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Tag added");
                alert.setHeaderText(null);
                alert.setContentText("The tag: " + newTag.getName() +
                        " is added successfully to event "
                        + event.getTitle());
                alert.showAndWait();
                updateSceneData(event);
            } catch (WebApplicationException e) {
                showAlert("Error occurred while adding the tag: " + e.getMessage());
            }
        });
    }

    /**
     * Displays an alert message
     *
     * @param message - particular message to display
     */
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * key navigation
     *
     * @param e - key pressed
     */
    public void keyPressed(KeyEvent e) {
        if (Objects.requireNonNull(e.getCode()) == KeyCode.ESCAPE) {
            goBack();
        }
    }

    /**
     * Sets the language
     *
     * @param locale - language
     */
    public void setCurrentLocale(Locale locale) {
        this.currentLocale = locale;
    }

    /**
     * updates the locale
     *
     * @param locale - the locale to update to
     */
    public void updateLocal(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle("messages", currentLocale);
        updateUI();
    }

    private void updateUI() {
        goBackButton.setText(bundle.getString("backButtonStat"));
        addTagButton.setText(bundle.getString("addTag"));

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
        private Locale locale;
        private ResourceBundle bundle;

        /**
         * Contructor for the tag cell in the list view
         *
         * @param specificEvent - specific event
         * @param utils         - server utils
         * @param locale        - locale
         */
        public TagCell(Event specificEvent, ServerUtils utils, Locale locale) {
            this.currentEvent = specificEvent;
            this.utils = utils;
            this.locale = locale;
            bundle = ResourceBundle.getBundle("messages", currentLocale);

            editButton.setOnAction(e -> {
                Tag tagToEdit = getItem();
                if (tagToEdit != null) {
                    editTag(tagToEdit);
                }
            });
            deleteButton.setOnAction(e1 -> {
                Tag tagToDelete = getItem();
                if (tagToDelete != null) {
                    deleteTag(tagToDelete);
                    updateSceneData(currentEvent);
                }
            });
            deleteButton.setText(bundle.getString("deleteButton"));
            editButton.setText(bundle.getString("editEventNameButton"));
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
         *
         * @param tag - tag selected
         */
        public void deleteTag(Tag tag) {
            long id = tag.getId();
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
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this tag?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {

                try {
                    utils.deleteTag(id);
                    updateSceneData(currentEvent);
                } catch (WebApplicationException err) {
                    var errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.initModality(Modality.APPLICATION_MODAL);
                    errorAlert.setContentText(err.getMessage());
                    errorAlert.showAndWait();
                    return;
                }
            }
            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setTitle("Tag deleted");
            alert2.setHeaderText(null);
            alert2.setContentText("The tag " + tag.getName() + " is " +
                    "deleted successfully in event "
                    + currentEvent.getTitle());
            alert2.showAndWait();
        }

        /**
         * Edits the selected tag
         *
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
                    Tag tag1 = new Tag(newName, newColor, tag.getEvent());
                    tag1.setId(tag.getId());
                    return tag1;
                }
                return null;
            });

            dialog.showAndWait().ifPresent(editedTag -> {
                try {
                    utils.updateTags(editedTag.getId(), editedTag);
                    for (Expense expense : event.getExpenses()) {
                        if (expense.getTag().getId().equals(tag.getId())) {
                            expense.getTag().setName(editedTag.getName());
                            expense.getTag().setColor(editedTag.getColor());
                        }
                    }
                    updateSceneData(event);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Tag edited");
                    alert.setHeaderText(null);
                    alert.setContentText("The tag: " + editedTag.getName() + " is edited successfully "
                            + "in event " + event.getTitle());
                    alert.showAndWait();
                } catch (WebApplicationException e) {
                    showAlert("Error occurred while updating the tag: " + e.getMessage());
                }
            });
        }
    }


}
