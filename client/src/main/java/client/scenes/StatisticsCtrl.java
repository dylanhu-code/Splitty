package client.scenes;

import client.utils.StatisticsUtils;
import com.google.inject.Inject;
import commons.Event;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javafx.stage.Stage;

import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;


public class StatisticsCtrl {
    @FXML
    private PieChart pieChart;
    @FXML
    private Label totalExpenseText;

    @FXML
    private Label totalExpenseLabel;
    @FXML
    private Button backButtonStat;
    @FXML
    private Label eventTitle;
    @FXML
    private VBox legend;
    private Scene statistics;
    private Stage primaryStage;
    private Event event;
    private final SplittyMainCtrl mainCtrl;
    private final StatisticsUtils utils;
    private ResourceBundle bundle;
    private Locale currentLocale;

    /**
     * Constructor -
     * @param mainCtrl - Splitty Main controller
     * @param utils - the server utils
     */
    @Inject
    public StatisticsCtrl(SplittyMainCtrl mainCtrl, StatisticsUtils utils) {
        this.mainCtrl = mainCtrl;
        this.utils = utils;
    }

    /**
     * Initializes the statistics page
     *
     * @param primaryStage - primary stage
     * @param statistics   - statistics scene
     */
    public void initialize(Stage primaryStage, Scene statistics)  {
        this.primaryStage = primaryStage;
        this.statistics = statistics;
    }

    /**
     * Sets the scene
     */
    public void initScene() {
        bundle = ResourceBundle.getBundle("messages", currentLocale);
        updateUI();
        primaryStage.setScene(statistics);
        primaryStage.show();
    }

    /**
     * Update the pie chart data on the page
     * @param event - the specific event
     */
    public void updateData(Event event) {
        this.event = event;
        eventTitle.setText(event.getTitle());
        pieChart.getData().clear();
        var pieData = utils.generatePieChartData(event.getExpenses());
        updateTotalExpense(utils.calculateTotalExpense(event.getExpenses()));
        updatePieChartData(pieData);
        utils.setSliceColors(pieChart, event.getExpenses());
        utils.createLegend(legend, event.getExpenses());

    }

    /**
     * Sets the language
     * @param locale - language
     */
    public void setCurrentLocale(Locale locale) {
        this.currentLocale = locale;
    }
    /**
     * updates the locale
     * @param locale - the locale to update to
     */
    public void updateLocale(Locale locale) {
        currentLocale = locale;
        bundle = ResourceBundle.getBundle("messages", currentLocale);
        updateUI();
    }

    /**
     * updates the language on the fields
     */
    private void updateUI() {
        backButtonStat.setText(bundle.getString("backButtonStat"));
        totalExpenseText.setText(bundle.getString("totalExpenseText"));
    }

    /**
     * Update the pie chart data
     *
     * @param pieChartData - new pie chart data
     */
    public void updatePieChartData(ObservableList<PieChart.Data> pieChartData) {
        pieChart.setData(pieChartData);
    }

    /**
     * updates total expense amount
     *
     * @param totalExpense - new expense amount
     */
    public void updateTotalExpense(double totalExpense) {
        totalExpenseLabel.setText(String.format("%.2f", totalExpense));
    }

    /**
     * Goes back to the overview page
     */
    public void goBack() {
        mainCtrl.showOverview(event);

    }


    /**
     * Handles actions when common keys are pressed
     *
     * @param e key event taking place
     */
    public void keyPressed(KeyEvent e) {
        if (Objects.requireNonNull(e.getCode()) == KeyCode.ESCAPE) {
            //TODO should go back to the overview of the event
            goBack();
        }
    }
}
