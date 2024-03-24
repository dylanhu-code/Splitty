package client.scenes;

import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class OpenDebtsCtrlTest {
    private OpenDebtsCtrl ctrl;
    @Mock
    private ServerUtils server;
    @Mock
    private SplittyMainCtrl mainCtrl;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ctrl = new OpenDebtsCtrl(mainCtrl);
    }
    @Test
    public void hasBackButton() {
        assertDoesNotThrow(() ->{
            ctrl.back();
            verify(mainCtrl, times(1)).showOverview(ctrl.getEvent());
        });
    }

}