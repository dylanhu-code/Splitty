package client.scenes;

import client.EventStorageManager;
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
    private Scene addExpense;
    private AddParticipantCtrl addParticipantCtrl;
    private Scene addParticipant;
    private InvitationCtrl invitationCtrl;
    private Scene invitation;
    private OpenDebtsCtrl openDebtsCtrl;
    private Scene openDebts;
    private AdminLoginCtrl adminLoginCtrl;
    private Scene adminLogin;
    private AdminCtrl adminCtrl;
    private Scene admin;
    private EditNameCtrl editNameCtrl;
    private Scene editName;
    private ManageTagsCtrl tagsCtrl;
    private Scene tags;
    private MenuBarCtrl menuBarCtrl;
    private Parent menuBar;
    private BorderPane baseScene;

    private EventStorageManager storageManager;
    protected static Locale currentLocale;
    private StatisticsCtrl statisticsCtrl;
    private Scene statisticsScene;

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
        this.overviewCtrl.setCurrentLocale(currentLocale);

        this.startScreenCtrl = startScreen.getKey();
        this.startScreen = startScreen.getValue();
        this.startScreenCtrl.setCurrentLocale(currentLocale);

        this.addParticipantCtrl = addParticipant.getKey();
        this.addParticipant = new Scene(addParticipant.getValue());
        this.addParticipantCtrl.setCurrentLocale(currentLocale);

        this.addExpenseCtrl = addExpense.getKey();
        this.addExpense = new Scene(addExpense.getValue());
        addExpenseCtrl.initialize(primaryStage, this.addExpense);
        this.addExpenseCtrl.setCurrentLocale(currentLocale);

        this.invitationCtrl = invitation.getKey();
        this.invitation = new Scene(invitation.getValue());
        this.invitationCtrl.setCurrentLocale(currentLocale);

        this.openDebtsCtrl = openDebts.getKey();
        this.openDebts = new Scene(openDebts.getValue());
        this.openDebtsCtrl.setCurrentLocale(currentLocale);

        this.adminLoginCtrl = adminLogin.getKey();
        this.adminLogin = new Scene(adminLogin.getValue());
        this.adminLoginCtrl.setCurrentLocale(currentLocale);
        this.storageManager = storageManager;

        this.adminCtrl = admin.getKey();
        this.admin = new Scene(admin.getValue());
        this.adminCtrl.setCurrentLocale(currentLocale);

        this.editNameCtrl = editName.getKey();
        this.editName = new Scene(editName.getValue());
        this.editNameCtrl.setCurrentLocale(currentLocale);

        this.statisticsCtrl = pairStatistics.getKey();
        this.statisticsScene = new Scene(pairStatistics.getValue());
        this.statisticsCtrl.setCurrentLocale(currentLocale);
        this.statisticsCtrl.initialize(primaryStage, statisticsScene);

        this.tagsCtrl = tagsPair.getKey();
        this.tags = new Scene(tagsPair.getValue());
        this.tagsCtrl.setCurrentLocale(currentLocale);
        this.tagsCtrl.initialize(primaryStage, tags);

        this.menuBarCtrl = menuBarPair.getKey();
        this.menuBar = menuBarPair.getValue();

        createBaseScene();

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
        System.out.println("Base scene created and set.");
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
        System.out.println("Showing overview.");
    }

    /**
     * Shows the start screen of the application.
     */
    public void showStartScreen() {
        primaryStage.setTitle("Start screen");
        startScreenCtrl.initialize();
        this.baseScene.setCenter(startScreen);
        startScreen.setOnKeyPressed(e -> startScreenCtrl.keyPressed(e));
        System.out.println("Showing start screen.");
    }

    /**
     * Shows the add participant screen.
     *
     * @param event       - current event
     * @param participant - the participant
     */
    public void showAddParticipant(Event event, Participant participant) {
        primaryStage.setTitle("Add Participant");
        addParticipantCtrl.initialize(primaryStage, addParticipant, event, participant);
        addParticipant.setOnKeyPressed(e -> addParticipantCtrl.keyPressed(e));
    }

    /**
     * Initialises the Invitation page
     *
     * @param event - current event
     */
    public void showInvitation(Event event) {
        primaryStage.setTitle("Invitation");
        invitationCtrl.initialize(primaryStage, invitation, event);
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
        openDebtsCtrl.initialize(primaryStage, openDebts, event);
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
        addExpenseCtrl.updateAllSceneData(event, expense);
        addExpenseCtrl.initScene();
        addExpense.setOnKeyPressed(e -> addExpenseCtrl.keyPressed(e));
    }

    /**
     * Shows the admin login screen
     */
    public void showAdminLogin() {
        primaryStage.setTitle("Admin login");
        adminLoginCtrl.initialize(primaryStage, adminLogin);
        adminLogin.setOnKeyPressed(e -> adminLoginCtrl.keyPressed(e));
    }

    /**
     * Shows the page with all events for the admin
     */
    public void showAdmin() {
        primaryStage.setTitle("Events");
        adminCtrl.initialize(primaryStage, admin);
        admin.setOnKeyPressed(e -> adminCtrl.keyPressed(e));
    }

    /**
     * Displays statistics page
     *
     * @param event - specific event
     */
    public void showStatistics(Event event) {
        primaryStage.setTitle("Statistics");
        statisticsCtrl.initScene();
        statisticsCtrl.updateData(event);
        statisticsScene.setOnKeyPressed(e -> statisticsCtrl.keyPressed(e));

    }

    /**
     * Displays tags scene
     * @param event - particular event
     */
    public void showTags(Event event) {
        primaryStage.setTitle("Manage Tags");
        tagsCtrl.initScene();
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
        editNameCtrl.initialize(primaryStage, editName, event);
        editName.setOnKeyPressed(e -> editNameCtrl.keyPressed(e));
    }

    /**
     * Updates the locale of the application
     *
     * @param locale - the new locale
     */
    public void updateLocale(Locale locale) {
        currentLocale = locale;
        startScreenCtrl.updateLocale(locale);
        overviewCtrl.updateLocale(locale);
        addParticipantCtrl.updateLocale(locale);
        addExpenseCtrl.updateLocale(locale);
        invitationCtrl.updateLocale(locale);
        openDebtsCtrl.updateLocale(locale);
        adminLoginCtrl.updateLocale(locale);
        adminCtrl.updateLocale(locale);
        editNameCtrl.updateLocale(locale);
        statisticsCtrl.updateLocale(locale);
        tagsCtrl.updateLocal(locale);
    }
}
