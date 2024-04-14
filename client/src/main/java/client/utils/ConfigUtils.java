package client.utils;

import java.io.*;
import java.util.Scanner;

public class ConfigUtils {
    public static String preferredLanguage;
    public static String serverUrl;
    public static String currency;

    /**
     * Constructor
     */
    public ConfigUtils(){
    }

    /**
     * reads the server url from the config file
     * @param file the file to read from
     * @return string of the url
     */
    public static String readServerUrl(String file) {
        Scanner urlReader;
        try {
            urlReader = new Scanner(new File(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("The file you are trying to read from was not found");
        }
        urlReader.nextLine();
        urlReader.next();
        String serverUrl = urlReader.next();
        ConfigUtils.serverUrl = serverUrl;
        return serverUrl;
    }

    /**
     * reads the language from the config file
     * @param file - file
     * @return preferred language
     */
    public static String readPreferredLanguage(String file) {
        Scanner configReader;
        try {
            configReader = new Scanner(new File(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Something went wrong when reading from the file - config");
        }
        configReader.next();
        configReader.next();
        return configReader.next();
    }

    /**
     * reads the preferred currency from the config file
     * @param file the file to read from
     * @return string of the url
     */
    public static String readPreferredCurrency(String file){
        Scanner currencyReader;
        try {
            currencyReader = new Scanner(new File(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        while (currencyReader.hasNextLine()) {
            String line = currencyReader.nextLine();
            if (line.startsWith("preferred currency: ")) {
                String currency = line.substring("preferred currency: ".length());
                ConfigUtils.currency = currency;
                return currency;
            }
        }
        throw new RuntimeException("Preferred currency not found in config file");
    }

    /**
     * writes the necessary persistence data to the config file at closing of application
     * @param file - file
     */
    public static void writeToConfig(String file) {
        try {
            if (currency == null) {
                currency = readPreferredCurrency(file);
            }

            FileOutputStream outputStream = new FileOutputStream(file);
            PrintWriter configWriter = new PrintWriter(outputStream);
            configWriter.write("preferred language: " + preferredLanguage
                + "\nserverUrl: " + serverUrl + "\npreferred currency: " + currency
                    // TODO change to have actual url
                + "\nemail: ooppteam42@gmail.com" + "\npassword: qjbs wpla keub qtas");

            configWriter.flush();
            configWriter.close();
            outputStream.close();
        } catch (IOException e){
            throw new RuntimeException("Something went wrong when writing to the file");
        }
    }

    /**
     * gets the preferred language
     * @return string of preferred language
     */
    public static String getPreferredLanguage() {
        return preferredLanguage;
    }

    /**
     * gets the server URL
     * @return string of server URL
     */
    public static String getServerUrl() {
        return serverUrl;
    }

    /**
     * getter for the preferred currency
     * @return the currency
     */
    public static String getCurrency() {
        return currency;
    }
}
