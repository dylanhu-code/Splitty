package client.scenes;

import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class AddParticipantCtrlTest {

    private AddParticipantCtrl addParticipantCtrl;
    @Mock
    private ServerUtils serverUtils;
    @Mock
    private SplittyMainCtrl mainCtrl;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        addParticipantCtrl = new AddParticipantCtrl(serverUtils, mainCtrl);
    }

    @Test
    public void abortTest() {
        assertDoesNotThrow(() ->{
            addParticipantCtrl.abortAdding();
            verify(mainCtrl, times(1)).showOverview(addParticipantCtrl.getEvent(), "-1");
        });
    }

}