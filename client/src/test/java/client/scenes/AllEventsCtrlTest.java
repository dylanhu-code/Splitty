package client.scenes;

import client.utils.ServerUtils;
import commons.Event;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class AllEventsCtrlTest {
    private AllEventsCtrl allEventsCtrl;
    @Mock
    private ServerUtils serverUtils;
    @Mock
    private SplittyMainCtrl mainCtrl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        allEventsCtrl = new AllEventsCtrl(serverUtils, mainCtrl);
        ListView<Event> listView = new ListView<>();
        allEventsCtrl.setListView(listView);
    }

    @Test
    void displayAllEventsTest() {
        ListView<Event> mockListView = Mockito.mock(ListView.class);
        ObservableList<Event> items = FXCollections.observableArrayList(new Event("Item 1"), new Event("Item 2"), new Event("Item 3"));
        Mockito.when(mockListView.getItems()).thenReturn(items);
        allEventsCtrl.displayAllEvents();

        assertEquals(3, items.size());

        assertEquals("Item 1", items.get(0).getTitle());
        assertEquals("Item 2", items.get(1).getTitle());
        assertEquals("Item 3", items.get(2).getTitle());
    }
}