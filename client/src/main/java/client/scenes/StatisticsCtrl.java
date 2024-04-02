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
    private Label totalExpenseLabel;
    @FXML
    private Button backButton;
    @FXML
    private Label eventTitle;
    @FXML
    private VBox legend;
    private Scene statistics;
    private Stage primaryStage;
    private Event event;
    private final SplittyMainCtrl mainCtrl;
    private final StatisticsUtils utils;

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
        this.event = event;
        this.primaryStage = primaryStage;
        this.statistics = statistics;
        eventTitle.setText(event.getTitle());
        var pieData = utils.generatePieChartData(event.getExpenses());
        updateTotalExpense(utils.calculateTotalExpense(event.getExpenses()));
        updatePieChartData(pieData);
        utils.setSliceColors(pieChart, event.getExpenses());
        utils.createLegend(legend, event.getExpenses());
        primaryStage.setScene(statistics);
        primaryStage.show();
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
        totalExpenseLabel.setText(String.valueOf(totalExpense));
    }
    public void goBack() {
        mainCtrl.showOverview(event);

    }
}
