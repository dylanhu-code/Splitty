package client.scenes;

import client.utils.ConfigUtils;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ConfigUtilsTest {

    /**
     * checkstyle
     */
    @Test
    public void writeToConfigTest(){
        Path configTestPath = Path.of("configTest.txt");
        ConfigUtils.preferredLanguage = "en";
        ConfigUtils.writeToConfig("configTest.txt");
        try {
            assertEquals("preferred language: en\nserverUrl: " + ConfigUtils.serverUrl
                    + "\npreferred currency: "+ ConfigUtils.currency +
                    "\nemail: ooppteam42@gmail.com\n" + "password: qjbs wpla keub qtas"
                , Files.readString(configTestPath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * checkstyle
     */
    @Test
    public void readUrlTest() throws MalformedURLException {
        String fileToRead = "configTest.txt";
        assertEquals("localhost:8080", ConfigUtils.readServerUrl(fileToRead));
    }

    /**
     * checkstyle
     */
    @Test
    public void readCurrencyTest(){
        String fileToRead = "configTest.txt";
        assertEquals("EUR", ConfigUtils.readPreferredCurrency(fileToRead));
    }

    /**
     * checkstyle
     */
    @Test
    public void readUrlFaultyFileTest(){
        String fileToRead = "";
        assertThrows(RuntimeException.class, () -> {
            ConfigUtils.readServerUrl(fileToRead);
        });
    }

    /**
     * checkstyle
     */
    @Test
    public void readLangFaultyFileTest(){
        String fileToRead = "";
        assertThrows(RuntimeException.class, () -> {
            ConfigUtils.readPreferredLanguage(fileToRead);
        });
    }

    /**
     * checkstyle
     */
    @Test
    public void readCurrFaultyFileTest(){
        String fileToRead = "";
        assertThrows(RuntimeException.class, () -> {
            ConfigUtils.readPreferredCurrency(fileToRead);
        });
    }

    /**
     * checkstyle
     */
    @Test
    public void writeFaultyFileTest(){
        String fileToRead = "";
        assertThrows(RuntimeException.class, () -> {
            ConfigUtils.writeToConfig(fileToRead);
        });
    }

    /**
     * checkstyle
     */
    @Test
    public void getPrefferedLanguageTest(){
        ConfigUtils.readPreferredLanguage("configTest.txt");
        assertEquals(ConfigUtils.getPreferredLanguage(), "en");
    }

    /**
     * checkstyle
     */
    @Test
    public void getServerUrlTest(){
        ConfigUtils.readServerUrl("configTest.txt");
        assertEquals(ConfigUtils.getServerUrl(), "localhost:8080");
    }

    /**
     * checkstyle
     */
    @Test
    public void getCurrencyTest(){
        ConfigUtils.readPreferredCurrency("configTest.txt");
        assertEquals(ConfigUtils.getCurrency(), "EUR");
    }
}
