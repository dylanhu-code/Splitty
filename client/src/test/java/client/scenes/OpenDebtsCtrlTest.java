package client.scenes;

import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class OpenDebtsCtrlTest {
    private OpenDebtsCtrl ctrl;
    @Mock
    private ServerUtils server;
    @Mock
    private SplittyMainCtrl mainCtrl;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        ctrl = new OpenDebtsCtrl(server, mainCtrl);
    }
//    @Test
//    public void hasBackButton() {
//        assertDoesNotThrow(() ->{
//            ctrl.abortDebts();
//            verify(mainCtrl, times(1)).showOverview(ctrl.getEvent());
//        });
//    }

}