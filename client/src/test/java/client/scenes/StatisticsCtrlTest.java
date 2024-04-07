package client.scenes;

import javafx.scene.input.KeyEvent;

import client.utils.StatisticsUtils;
import commons.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StatisticsCtrlTest extends ApplicationTest {
    private StatisticsCtrl statisticsCtrl;
    @Mock
    private SplittyMainCtrl mainCtrl;
    @Mock
    private StatisticsUtils utils;
    @Mock
    private Stage primaryStage;
    @Mock
    private Scene statistics;



    @Override
    public void start(Stage stage) {
        statisticsCtrl = new StatisticsCtrl(new SplittyMainCtrl(), new StatisticsUtils());
        Scene scene = new Scene(new VBox(), 800, 600); // You might need to adjust the scene size
        statisticsCtrl.initialize(stage, scene);
        statisticsCtrl.eventTitle = new Label();
        statisticsCtrl.pieChart = new PieChart();
        statisticsCtrl.totalExpenseLabel = new Label();
        statisticsCtrl.backButtonStat = new Button();
        statisticsCtrl.totalExpenseText = new Label();
        statisticsCtrl.legend = new VBox();
        stage.setScene(scene);
        stage.show();
    }


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        statisticsCtrl = new StatisticsCtrl(mainCtrl, utils);

        // Mock the required UI elements
        statisticsCtrl.eventTitle = new Label();
        statisticsCtrl.pieChart = new PieChart();
        statisticsCtrl.totalExpenseLabel = new Label();
        statisticsCtrl.backButtonStat = new Button();
        statisticsCtrl.totalExpenseText = new Label();
        statisticsCtrl.legend = new VBox();

        statisticsCtrl.initialize(primaryStage, statistics);
    }

    @Test
    void testUpdateData() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        pieChartData.add(new PieChart.Data("Category 1", 50));
        pieChartData.add(new PieChart.Data("Category 2", 30));

        double totalExpense = 80;

        Event event = new Event("Event Title");

        when(utils.generatePieChartData(event.getExpenses())).thenReturn(pieChartData);
        when(utils.calculateTotalExpense(event.getExpenses())).thenReturn(totalExpense);

        statisticsCtrl.updateData(event);

        verify(utils).setSliceColors(any(PieChart.class), any());
        verify(utils).createLegend(any(), any());

        assertEquals(event.getTitle(), statisticsCtrl.eventTitle.getText());
        assertEquals(pieChartData, statisticsCtrl.pieChart.getData());
        assertEquals(String.format("%.2f", totalExpense), statisticsCtrl.totalExpenseLabel.getText());
    }

    @Test
    void testUpdateLocale() {
        Locale locale = Locale.US;
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);

        statisticsCtrl.updateLocale(locale);

        assertEquals(bundle.getString("backButtonStat"), statisticsCtrl.backButtonStat.getText());
        assertEquals(bundle.getString("totalExpenseText"), statisticsCtrl.totalExpenseText.getText());
    }

    @Test
    void testUpdatePieChartData() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        pieChartData.add(new PieChart.Data("Category 1", 50));
        pieChartData.add(new PieChart.Data("Category 2", 30));

        statisticsCtrl.updatePieChartData(pieChartData);

        // Verify that pieChart's data is updated with the provided pieChartData
        assertEquals(pieChartData, statisticsCtrl.pieChart.getData());
    }

    @Test
    void testUpdateTotalExpense() {
        double totalExpense = 100.50;

        statisticsCtrl.updateTotalExpense(totalExpense);

        // Verify that totalExpenseLabel's text is updated with the formatted totalExpense
        assertEquals(String.format("%.2f", totalExpense), statisticsCtrl.totalExpenseLabel.getText());
    }

    @Test
    void testGoBack() {


        statisticsCtrl.goBack();

        // Verify that showOverview method of mainCtrl is called with the provided event
        verify(mainCtrl).showOverview(statisticsCtrl.event);
    }

    @Test
    void testKeyPressed() {
        // Mock the SplittyMainCtrl instance
        SplittyMainCtrl mainCtrlMock = mock(SplittyMainCtrl.class);

        // Set the mocked mainCtrlMock to the statisticsCtrl
        statisticsCtrl = new StatisticsCtrl(mainCtrlMock, utils);

        // Create a new KeyEvent instance
        KeyEvent escapeKeyEvent = new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.ESCAPE, false, false, false, false);

        // Call keyPressed method with the KeyEvent instance
        statisticsCtrl.keyPressed(escapeKeyEvent);
        Event e = statisticsCtrl.event;

        // Verify that goBack method is called on the mocked mainCtrlMock with any Event argument
        verify(mainCtrlMock).showOverview(e);
    }


}

