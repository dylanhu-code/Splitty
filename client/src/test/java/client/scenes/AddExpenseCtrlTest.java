package client.scenes;

import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class AddExpenseCtrlTest {
    private AddExpenseCtrl addExpenseCtrl;
    @Mock
    private ServerUtils serverUtils;
    @Mock
    private SplittyMainCtrl mainCtrl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        addExpenseCtrl = new AddExpenseCtrl(serverUtils, mainCtrl);
    }

    @Test
    public void abortTest() {
        assertDoesNotThrow(() ->{
            addExpenseCtrl.abort();
            verify(mainCtrl, times(1)).showOverview(addExpenseCtrl.getEvent(), "-1");
        });
    }

}