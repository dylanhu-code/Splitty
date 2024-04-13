package client.scenes;

import client.utils.StatisticsUtils;
import commons.Event;
import commons.Expense;
import commons.Participant;
import commons.Tag;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.junit.jupiter.api.*;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StatisticsCtrlTest extends ApplicationTest {

    private StatisticsCtrl statisticsCtrl;
    private Stage stage;
    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        statisticsCtrl = new StatisticsCtrl(new SplittyMainCtrl(), new StatisticsUtils());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/scenes/Statistics.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }
    @BeforeAll
    public void setup() {
        assumeFalse(HeadlessModeChecker.isHeadless(), "Tests cannot run in headless mode");


    }
    @AfterAll
    @Timeout(value = 1, unit = TimeUnit.MINUTES)
    public void tearDown() throws TimeoutException {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }
    @Test
    void initialize() {
        assertNotNull(statisticsCtrl);
    }


    @Test
    void updateData() {
        Event event = new Event();
        event.setTitle("Test Event");
        Participant p = new Participant();
        ObservableList<Expense> expenses = FXCollections.observableArrayList();
        Expense e1 = new Expense(p, 100, "EUR", List.of(p), "Expense 1", new Date(), new Tag("food", "green", 1L));
        Expense e2 = new Expense(p, 200, "EUR", List.of(p), "Expense 2", new Date(), new Tag("travel", "red", 1L));

        expenses.add(e1);
        expenses.add(e2);
        event.setExpenses(expenses);


        Platform.runLater(() -> {
            statisticsCtrl.updateData(event);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            assertEquals("Test Event", statisticsCtrl.eventTitle.getText());
            assertEquals("300.00", statisticsCtrl.totalExpenseLabel.getText());

            ObservableList<PieChart.Data> pieChartData = statisticsCtrl.pieChart.getData();
            assertEquals(2, pieChartData.size());
            assertEquals("travel", pieChartData.get(0).getName().substring(0, 6));
            assertEquals(200.00, pieChartData.get(0).getPieValue());
            assertEquals("food", pieChartData.get(1).getName().substring(0, 4));
            assertEquals(100.00, pieChartData.get(1).getPieValue());
        });
    }


    @Test
    void setCurrentLocale() {
        statisticsCtrl.setCurrentLocale(Locale.US);
        assertEquals(Locale.US, statisticsCtrl.currentLocale);
    }

    @Test
    void updateTotalExpense() {
        statisticsCtrl.updateTotalExpense(500);
        assertEquals("500.00", statisticsCtrl.totalExpenseLabel.getText());
    }

}