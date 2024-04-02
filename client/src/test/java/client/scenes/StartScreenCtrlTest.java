package client.scenes;

import client.EventStorageManager;
import client.utils.ConfigUtils;
import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class StartScreenCtrlTest {

    private StartScreenCtrl ctrl;
    @Mock
    private ServerUtils server;
    @Mock
    private SplittyMainCtrl mainCtrl;

    private ConfigUtils configUtils = new ConfigUtils();

    /**
     * pipeline
     */
    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ctrl = new StartScreenCtrl(mainCtrl, server, new EventStorageManager(server));
    }

    /**
     * pipeline
     */
    @Test
    public void testAdminOption() {
        ctrl.adminOption();
        verify(mainCtrl, times(1)).showAdminLogin();
    }

    /**
     * pipeline
     */
    @Test
    public void readPreferredLanguageTest() {
        String fileToRead = "configTest2.txt";
        assertEquals("nl", ConfigUtils.readPreferredLanguage(fileToRead));
    }


}