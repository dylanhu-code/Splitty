package client.scenes;

import client.utils.ServerUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    void testUpdateLocale() {
        Locale newLocale = new Locale("es");
        ctrl.updateLocale(newLocale);
        assertEquals(newLocale, ctrl.getCurrentLocale());
    }
}