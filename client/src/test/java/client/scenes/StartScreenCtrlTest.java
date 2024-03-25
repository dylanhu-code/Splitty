package client.scenes;

import client.EventStorageManager;
import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class StartScreenCtrlTest {

    private StartScreenCtrl ctrl;
    @Mock
    private ServerUtils server;
    @Mock
    private SplittyMainCtrl mainCtrl;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ctrl = new StartScreenCtrl(mainCtrl, server, new EventStorageManager(server));
    }
    @Test
    public void testAdminOption() {
        ctrl.adminOption();
        verify(mainCtrl, times(1)).showAdmin();
    }

}