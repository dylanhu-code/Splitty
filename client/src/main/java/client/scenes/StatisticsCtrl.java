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
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import static client.scenes.SplittyMainCtrl.currentLocale;

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

    @Inject
    public StatisticsCtrl(SplittyMainCtrl mainCtrl, StatisticsUtils utils) {
        this.mainCtrl = mainCtrl;
        this.utils = utils;
    }

    /**
     * Initializes the statistics page
     * @param primaryStage - primary stage
     * @param statistics - statistics scene
     * @param event - specific event
     */

    public void initialize(Stage primaryStage, Scene statistics, Event event)  {
        pieChart.getData().clear();
        this.event = event;
        this.primaryStage = primaryStage;
        this.statistics = statistics;
        eventTitle.setText(event.getTitle());

        bundle = ResourceBundle.getBundle("messages", currentLocale);
        updateUI();

        var pieData = utils.generatePieChartData(event.getExpenses());
        updateTotalExpense(utils.calculateTotalExpense(event.getExpenses()));
        updatePieChartData(pieData);
        utils.setSliceColors(pieChart, event.getExpenses());
        utils.createLegend(legend, event.getExpenses());
        primaryStage.setScene(statistics);
        primaryStage.show();
    }
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
    private void updateUI() {
        backButtonStat.setText(bundle.getString("backButtonStat"));
        totalExpenseText.setText(bundle.getString("totalExpenseText"));
    }

    /**
     * Update the pie chart data
     * @param pieChartData - new pie chart data
     */
    public void updatePieChartData(ObservableList<PieChart.Data> pieChartData) {
        pieChart.setData(pieChartData);
    }

    /**
     * updates total expense amount
     * @param totalExpense - new expense amount
     */
    public void updateTotalExpense(double totalExpense) {
        totalExpenseLabel.setText(String.format("%.2f", totalExpense));
    }
    public void goBack() {
        mainCtrl.showOverview(event);

    }
}
