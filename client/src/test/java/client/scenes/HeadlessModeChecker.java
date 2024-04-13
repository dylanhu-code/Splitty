package client.scenes;

import java.awt.GraphicsEnvironment;

public class HeadlessModeChecker {


    public static boolean isHeadless() {
        return GraphicsEnvironment.isHeadless();
    }


}
