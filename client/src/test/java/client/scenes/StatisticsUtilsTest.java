package client.scenes;

import client.utils.StatisticsUtils;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatisticsUtilsTest {
    private StatisticsUtils utils;
    private Expense e1;
    private Expense e2;
    private Expense e3;
    private List<Expense> expenses;

    /**
     *
     */
//    @BeforeAll
//    public static void initJavaFX() {
//        // This initialises the JavaFx toolkit, otherwise tests wouldn't compile
//        Platform.startup(() -> {});
//    }

    /**
     *
     */
    @BeforeEach
    public void setup() {
        utils = new StatisticsUtils();
        e1 = new Expense(new Participant(), 50.0, null, "testExpense", null,
                new Tag("food", "green"));
        e2 = new Expense(new Participant(), 30.0, null, "testExpense2", null,
                new Tag("entrance fees", "blue"));
        e3 = new Expense(new Participant(), 20.0, null, "testExpense3", null,
                new Tag("travel", "red"));
        expenses = List.of(e1, e2, e3);
    }

    /**
     *
     */
    @Test
    public void testGeneratePieChartData() {

        ObservableList<PieChart.Data> pieChartData = utils.generatePieChartData(expenses);

        assertEquals(3, pieChartData.size());

        assertEquals("food\n50.00 (50.00%)", pieChartData.get(0).getName());
        assertEquals(50.0, pieChartData.get(0).getPieValue());
        assertEquals("entrance fees\n30.00 (30.00%)", pieChartData.get(1).getName());
        assertEquals(30.0, pieChartData.get(1).getPieValue());
        assertEquals("travel\n20.00 (20.00%)", pieChartData.get(2).getName());
        assertEquals(20.0, pieChartData.get(2).getPieValue());
    }

    /**
     *
     */
    @Test
    public void testCalculateTotalExpense() {
        double totalExpense = utils.calculateTotalExpense(expenses);
        assertEquals(100.0, totalExpense);
    }

    /**
     *
     */
//    @Test
//    public void testSetSliceColors() {
//
//        PieChart pieChart = new PieChart();
//        pieChart.setData(utils.generatePieChartData(expenses));
//
//        utils.setSliceColors(pieChart, expenses);
//
//        assertEquals("green", pieChart.getData().get(0)
//                .getNode().getStyle().substring(14, 20).strip());
//        assertEquals("blue", pieChart.getData().get(1)
//                .getNode().getStyle().substring(14, 19).strip());
//        assertEquals("red", pieChart.getData().get(2)
//                .getNode().getStyle().substring(14, 18).strip());
//
//    }

    /**
     *
     */
//    @Test
//    public void testCreateLegend() {
//        VBox legendBox = new VBox();
//
//        utils.createLegend(legendBox, expenses);
//        assertEquals(6, legendBox.getChildren().size());
//    }
}
