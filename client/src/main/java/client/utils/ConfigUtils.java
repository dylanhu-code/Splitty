package client.utils;

import java.io.*;
import java.net.MalformedURLException;
import java.util.Scanner;

public class ConfigUtils {
    public static String preferredLanguage;
    public static String serverUrl;

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
    public static String readServerUrl(String file) throws MalformedURLException {
        Scanner urlReader;
        try {
            urlReader = new Scanner(new File(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
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
            System.out.println("Something went wrong when reading from the file - config");
            throw new RuntimeException(e);
        }
        configReader.next();
        configReader.next();
        return configReader.next();
    }

    /**
     * writes the necessary persistence data to the config file at closing of application
     * @param file - file
     */
    public static void writeToConfig(String file) {
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            PrintWriter configWriter = new PrintWriter(outputStream);
            configWriter.write("preferred language: " + preferredLanguage
                + "\nserverUrl: " + serverUrl // TODO change to have actual url
                + "\nemail: ooppteam42@gmail.com" + "\npassword: qjbs wpla keub qtas");
            configWriter.flush();
            configWriter.close();
            outputStream.close();
        } catch (IOException e){
            e.printStackTrace();
            System.out.println("Something went wrong when writing to the file");
        }
    }

    /**
     * gets the preferred language
     * @return string of preferred language
     */
    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    /**
     * gets the server URL
     * @return string of server URL
     */
    public String getServerUrl() {
        return serverUrl;
    }

}
