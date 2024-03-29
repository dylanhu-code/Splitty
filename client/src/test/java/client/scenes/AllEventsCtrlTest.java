package client.scenes;

import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

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
    }

    @Test
    public void hasBackButton() {
        assertDoesNotThrow(() ->{
            allEventsCtrl.back();
            verify(mainCtrl, times(1)).showStartScreen();
        });
    }
}