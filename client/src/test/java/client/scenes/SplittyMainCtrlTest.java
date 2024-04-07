/*
 * Copyright 2021 Delft University of Technology
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package client.scenes;

import client.utils.ConfigUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SplittyMainCtrlTest {

    private SplittyMainCtrl mainCtrl;
    private ConfigUtils configUtils;
    /**
     * checkstyle
     */
    @BeforeEach
    public void setup() {
        mainCtrl = new SplittyMainCtrl();
    }

    /**
     * checkstyle
     */
    @Test
    public void writeSomeTests() {
        // TODO create replacement objects and write some tests
        // sut.initialize(null, null, null);
    }

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
        assertEquals("http://localhost:8080/", ConfigUtils.readServerUrl(fileToRead));
    }

    /**
     * checkstyle
     */
    @Test
    public void readCurrencyTest(){
        String fileToRead = "configTest.txt";
        assertEquals("EUR", ConfigUtils.readPreferredCurrency(fileToRead));
    }

}