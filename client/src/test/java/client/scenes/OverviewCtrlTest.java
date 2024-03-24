package client.scenes;

import client.EventStorageManager;
import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class OverviewCtrlTest {

    private OverviewCtrl ctrl;
    @Mock
    private ServerUtils server;
    @Mock
    private SplittyMainCtrl mainCtrl;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ctrl = new OverviewCtrl(server, mainCtrl, new EventStorageManager(server));
    }
    @Test
    public void hasBackButton() {
        assertDoesNotThrow(() ->{
            ctrl.back();
            verify(mainCtrl, times(1)).showStartScreen();
        });
    }

}