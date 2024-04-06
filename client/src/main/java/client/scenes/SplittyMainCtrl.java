package client.scenes;

import client.EventStorageManager;
import commons.Event;
import commons.Expense;
import commons.Participant;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    private Scene overview;
    private StartScreenCtrl startScreenCtrl;
    private Scene startScreen;
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
                           Pair<EditNameCtrl, Parent> editName) {

        this.primaryStage = primaryStage;
        this.overviewCtrl = overview.getKey();
        this.overview = new Scene(overview.getValue());
        this.overviewCtrl.setCurrentLocale(currentLocale);

        this.startScreenCtrl = startScreen.getKey();
        this.startScreen = new Scene(startScreen.getValue());
        this.startScreenCtrl.setCurrentLocale(currentLocale);

        this.addParticipantCtrl = addParticipant.getKey();
        this.addParticipant = new Scene(addParticipant.getValue());
        this.addParticipantCtrl.setCurrentLocale(currentLocale);

        this.addExpenseCtrl = addExpense.getKey();
        this.addExpense = new Scene(addExpense.getValue());
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

        showStartScreen();
        primaryStage.show();
    }

    /**
     * used to show the overview of a certain event.
     *
     * @param event - current event
     */
    public void showOverview(Event event) {
        primaryStage.setTitle("Event overview");
        overviewCtrl.initialize(primaryStage, overview, event);
        overview.setOnKeyPressed(e -> overviewCtrl.keyPressed(e));
    }

    /**
     * Shows the start screen of the application.
     */
    public void showStartScreen() {
        primaryStage.setTitle("Start screen");
        startScreenCtrl.initialize(primaryStage, startScreen);
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
    public void showEditExpense(Expense expense, Event event) {
        primaryStage.setTitle("Add/Edit Expense");
        addExpenseCtrl.initialize(primaryStage, addExpense, event, expense);
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
    }
}
