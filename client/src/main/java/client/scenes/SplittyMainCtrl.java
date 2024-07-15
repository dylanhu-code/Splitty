package client.scenes;

import client.EventStorageManager;
import client.utils.ConfigUtils;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.net.MalformedURLException;
import java.util.Locale;

/**
 * Controller class for managing different scenes in the application.
 */
public class SplittyMainCtrl {

    private Stage primaryStage;
    private OverviewCtrl overviewCtrl;
    private Parent overview;
    private StartScreenCtrl startScreenCtrl;
    private Parent startScreen;
    private Scene backups;
    private AddExpenseCtrl addExpenseCtrl;
    private Parent addExpense;
    private AddParticipantCtrl addParticipantCtrl;
    private Parent addParticipant;
    private InvitationCtrl invitationCtrl;
    private Parent invitation;
    private OpenDebtsCtrl openDebtsCtrl;
    private Parent openDebts;
    private AdminLoginCtrl adminLoginCtrl;
    private Parent adminLogin;
    private AdminCtrl adminCtrl;
    private Parent admin;
    private EditNameCtrl editNameCtrl;
    private Parent editName;
    private ManageTagsCtrl tagsCtrl;
    private Parent tags;
    private MenuBarCtrl menuBarCtrl;
    private Parent menuBar;
    private BorderPane baseScene;
    private StatisticsCtrl statisticsCtrl;
    private Parent statistics;
    private EventStorageManager storageManager;
    protected Locale currentLocale;

    public SplittyMainCtrl() {
        currentLocale = new Locale(ConfigUtils.readPreferredLanguage("config.txt"));
        ConfigUtils.currency = ConfigUtils.readPreferredCurrency("config.txt");
    }

    /**
     * Initialises all scenes and controls
     *
     * @param primaryStage   - the primary stage
     * @param overview       - overviewCtrl and parent pair
     * @param startScreen    - StartScreenCtrl and parent pair
     * @param addParticipant - addParticipantCtrl and parent pair
     * @param addExpense     - AddExpenseCtrl and parent pair
     * @param invitation     - InvitationCtrl and parent pair
     * @param openDebts      - DebtsCtl and parent pair
     * @param admin          - AllEventsCtrl and parent pair
     * @param adminLogin     - AdminCtrl and parent pair
     * @param storageManager - the manager for the events in the user file
     * @param pairStatistics - StatisticsCtrl and parent pair
     * @param editName       - EditNameCtrl and parent pair
     * @param tagsPair       - ManagerTagsCtrl and parent pair
     * @param menuBarPair    - MenuBarCtrl and parent pair
     */
    public void initialize(Stage primaryStage, Pair<OverviewCtrl, Parent> overview,
                           Pair<StartScreenCtrl, Parent> startScreen,
                           Pair<AddParticipantCtrl, Parent> addParticipant,
                           Pair<AddExpenseCtrl, Parent> addExpense,
                           Pair<InvitationCtrl, Parent> invitation,
                           Pair<OpenDebtsCtrl, Parent> openDebts,
                           Pair<AdminCtrl, Parent> admin,
                           Pair<AdminLoginCtrl, Parent> adminLogin,
                           EventStorageManager storageManager,
                           Pair<StatisticsCtrl, Parent> pairStatistics,
                           Pair<EditNameCtrl, Parent> editName,
                           Pair<ManageTagsCtrl, Parent> tagsPair,
                           Pair<MenuBarCtrl, Parent> menuBarPair) {

        this.primaryStage = primaryStage;
        this.overviewCtrl = overview.getKey();
        this.overview = overview.getValue();

        this.startScreenCtrl = startScreen.getKey();
        this.startScreen = startScreen.getValue();

        this.addParticipantCtrl = addParticipant.getKey();
        this.addParticipant = addParticipant.getValue();

        this.addExpenseCtrl = addExpense.getKey();
        this.addExpense = addExpense.getValue();

        this.invitationCtrl = invitation.getKey();
        this.invitation = invitation.getValue();

        this.openDebtsCtrl = openDebts.getKey();
        this.openDebts = openDebts.getValue();

        this.adminLoginCtrl = adminLogin.getKey();
        this.adminLogin = adminLogin.getValue();
        this.storageManager = storageManager;

        this.adminCtrl = admin.getKey();
        this.admin = admin.getValue();

        this.editNameCtrl = editName.getKey();
        this.editName = editName.getValue();

        this.statisticsCtrl = pairStatistics.getKey();
        this.statistics = pairStatistics.getValue();

        this.tagsCtrl = tagsPair.getKey();
        this.tags = tagsPair.getValue();

        this.menuBarCtrl = menuBarPair.getKey();
        this.menuBar = menuBarPair.getValue();

        createBaseScene();
        updateLocale(currentLocale);
        showStartScreen();
        primaryStage.show();
    }

    /**
     * Creates the base scene.
     */
    public void createBaseScene(){
        this.baseScene = new BorderPane();
        this.primaryStage.setScene(new Scene(this.baseScene));
        this.baseScene.setTop(menuBar);
    }

    /**
     * used to show the overview of a certain event.
     *
     * @param event - current event
     * @param previousPage - the previous page
     */
    public void showOverview(Event event, String previousPage) {
        primaryStage.setTitle("Event overview");
        overviewCtrl.initialize(event, previousPage);
        this.baseScene.setCenter(overview);
        overview.setOnKeyPressed(e -> overviewCtrl.keyPressed(e));
    }

    /**
     * Shows the start screen of the application.
     */
    public void showStartScreen() {
        primaryStage.setTitle("Start screen");
        this.baseScene.setCenter(startScreen);
        startScreenCtrl.initialize();
        startScreen.setOnKeyPressed(e -> startScreenCtrl.keyPressed(e));
    }

    /**
     * Shows the add participant screen.
     *
     * @param event       - current event
     * @param participant - the participant
     */
    public void showAddParticipant(Event event, Participant participant) {
        primaryStage.setTitle("Add Participant");
        this.baseScene.setCenter(addParticipant);
        addParticipantCtrl.initialize(event, participant);
        addParticipant.setOnKeyPressed(e -> addParticipantCtrl.keyPressed(e));
    }

    /**
     * Initialises the Invitation page
     *
     * @param event - current event
     */
    public void showInvitation(Event event) {
        primaryStage.setTitle("Invitation");
        this.baseScene.setCenter(invitation);
        invitationCtrl.initialize(event);
        invitation.setOnKeyPressed(e -> {
            try {
                invitationCtrl.keyPressed(e);
            } catch (MalformedURLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    /**
     * Shows the open debts screen.
     *
     * @param event - current event
     */
    public void showOpenDebts(Event event) {
        primaryStage.setTitle("Open Debts");
        this.baseScene.setCenter(openDebts);
        openDebtsCtrl.initialize(event);
        openDebts.setOnKeyPressed(e -> openDebtsCtrl.keyPressed(e));
    }

    /**
     * Displays the Edit Expense scene
     *
     * @param expense - the expense the user wants to edit
     * @param event   - the event this expense belongs to
     */
    public void showAddOrEditExpense(Expense expense, Event event) {
        primaryStage.setTitle("Add/Edit Expense");
        this.baseScene.setCenter(addExpense);
        addExpenseCtrl.initialize(event, expense, primaryStage);
        addExpense.setOnKeyPressed(e -> addExpenseCtrl.keyPressed(e));
    }

    /**
     * Shows the admin login screen
     */
    public void showAdminLogin() {
        primaryStage.setTitle("Admin login");
        this.baseScene.setCenter(adminLogin);
        adminLoginCtrl.initialize();
        adminLogin.setOnKeyPressed(e -> adminLoginCtrl.keyPressed(e));
    }

    /**
     * Shows the page with all events for the admin
     */
    public void showAdmin() {
        primaryStage.setTitle("Events");
        this.baseScene.setCenter(admin);
        adminCtrl.initialize();
        admin.setOnKeyPressed(e -> adminCtrl.keyPressed(e));
    }

    /**
     * Displays statistics page
     *
     * @param event - specific event
     */
    public void showStatistics(Event event) {
        primaryStage.setTitle("Statistics");
        this.baseScene.setCenter(statistics);
        statisticsCtrl.initialize(event);
        statistics.setOnKeyPressed(e -> statisticsCtrl.keyPressed(e));

    }

    /**
     * Displays tags scene
     * @param event - particular event
     */
    public void showTags(Event event) {
        primaryStage.setTitle("Manage Tags");
        this.baseScene.setCenter(tags);
        tagsCtrl.initialize();
        tagsCtrl.updateSceneData(event);
        tags.setOnKeyPressed(e -> tagsCtrl.keyPressed(e));

    }

    /**
     * Displays Edit Event Name page
     *
     * @param event - specific event
     */
    public void showEditName(Event event) {
        primaryStage.setTitle("Edit Event Name");
        this.baseScene.setCenter(editName);
        editNameCtrl.initialize(event);
        editName.setOnKeyPressed(e -> editNameCtrl.keyPressed(e));
    }

    /**
     * Updates the locale of the application
     *
     * @param locale - the new locale
     */
    public void updateLocale(Locale locale) {
        currentLocale = locale;
        startScreenCtrl.updateLocale();
        overviewCtrl.updateLocale();
        addParticipantCtrl.updateLocale();
        addExpenseCtrl.updateLocale();
        invitationCtrl.updateLocale();
        openDebtsCtrl.updateLocale();
        adminLoginCtrl.updateLocale();
        adminCtrl.updateLocale();
        editNameCtrl.updateLocale();
        statisticsCtrl.updateLocale();
        tagsCtrl.updateLocale();
    }

    public Locale getCurrentLocale() {
        return currentLocale;
    }

    public void setCurrentLocale(Locale currentLocale) {
        this.currentLocale = currentLocale;
        updateLocale(currentLocale);
    }

    public OverviewCtrl getOverview() {
        return overviewCtrl;
    }

    public OpenDebtsCtrl getOpenDebts() {
        return openDebtsCtrl;
    }
}
