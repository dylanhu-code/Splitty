package client.scenes;

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

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ctrl = new StartScreenCtrl(mainCtrl, server);
    }
    @Test
    public void testAdminOption() {
        ctrl.adminOption();
        verify(mainCtrl, times(1)).showAdmin();
    }

    @Test
    public void readPreferredLanguageTest() {
        String fileToRead = "configTest.txt";
        assertEquals("nl", ctrl.readPreferredLanguage(fileToRead));
    }


}