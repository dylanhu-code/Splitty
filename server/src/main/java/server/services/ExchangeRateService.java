package server.services;

import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

@Service
public class ExchangeRateService {
    private static final String CACHE_DIRECTORY = "rates";

    /**
     * Check if rates are available in cache
     * @param date - the date
     * @param from - the first currency
     * @param to - the second currency
     * @return the currencies with their rates
     */
    public Map<String, Double> getExchangeRates(String date, String from, String to) {
        if(from.equals(to)) {
            Map<String, Double> rates = new HashMap<>();
            rates.put(to, 1.0);
            rates.put(from, 1.0);
            return rates;
        }else{
            if (ratesAreCached(date, from, to)) {
                return fetchRatesFromCache(date, from, to);
            } else {
                Map<String, Double> rates = fetchRatesUsingFakeConverter(date, from, to);
                cacheRates(date, from, to, rates);
                return rates;
            }
        }
    }

    /**
     * Check if the rates are cached
     * @param date - the date
     * @param from - the first currency
     * @param to - the second currency
     */
    boolean ratesAreCached(String date, String from, String to) {
        String cacheFilePath = getCacheFilePath(date, from, to);
        File cacheFile = new File(cacheFilePath);
        return cacheFile.exists();
    }
    /**
     * Creates the file path for the requested date and currencies
     */
    private String getCacheFilePath(String date, String from, String to) {
        StringBuilder filePathBuilder = new StringBuilder();
        filePathBuilder.append(CACHE_DIRECTORY);
        filePathBuilder.append(File.separator);
        filePathBuilder.append(date);
        filePathBuilder.append(File.separator);
        filePathBuilder.append(from);
        filePathBuilder.append(File.separator);
        filePathBuilder.append(to);

        filePathBuilder.append(".txt");
        return filePathBuilder.toString();
    }

    /**
     * Retrieve rates from the cache
     * @param date - the date
     * @param from - the first currency
     * @param to - the second currency
     */
    Map<String, Double> fetchRatesFromCache(String date, String from, String to) {
        Map<String, Double> rates = new HashMap<>();
        String cacheFilePath = getCacheFilePath(date, from, to);
        String cacheFilePath2 = getCacheFilePath(date, to, from);

        try (Scanner reader = new Scanner(new File(cacheFilePath))) {
            String line = reader.nextLine();
            rates.put(from, Double.parseDouble(line));
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        try (Scanner reader = new Scanner(new File(cacheFilePath2))) {
            String line = reader.nextLine();
            rates.put(to, Double.parseDouble(line));
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return rates;
    }

    /**
     * Fetch rates from the exchange rate converter
     * @param date - the date
     * @param from - the first currency
     * @param to - the second currency
     */
    Map<String, Double> fetchRatesUsingFakeConverter(String date, String from, String to) {
        try {
            double rate = generateBaseRate(date);

            double reciprocalRate = 1 / rate;

            Map<String, Double> response = new HashMap<>();
            response.put(from, rate);
            response.put(to, reciprocalRate);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    /**
     * Generate base rate using timestamp as seed for stability
     * @param date - the date
     */
    private double generateBaseRate(String date) {
        LocalDate localDate = LocalDate.parse(date);
        long timestamp = localDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
        Random random = new Random(timestamp);
        return 1.0 + getRandomAdjustment(random);
    }

    /**
     *  Generate random adjustment between -10% and +10%
     */
    private double getRandomAdjustment(Random random) {
        double minAdjustment = -0.1;
        double maxAdjustment = 0.1;
        return minAdjustment + (maxAdjustment - minAdjustment) * random.nextDouble();
    }

    /**
     * Cache rates for future use
     * @param date - the date
     * @param from - the first currency
     * @param to - the second currency
     * @param rates - the pairs of rates to be cached
     */
    void cacheRates(String date, String from, String to, Map<String, Double> rates) {
        if(from.equals(to) || (from.startsWith("U") && to.startsWith("U")) ||
                (from.startsWith("E") && to.startsWith("E")) ||
                (from.startsWith("C") && to.startsWith("C"))) {
           rates.put(to, 1.0);
           rates.put(from, 1.0);
        }
        File cacheDir = new File(CACHE_DIRECTORY);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }

        String cacheFilePath = getCacheFilePath(date, from, to);
        String cacheFilePath2 = getCacheFilePath(date, to, from);

        File file = new File(cacheFilePath);
        File file2 = new File(cacheFilePath2);
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(rates.get(from) + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
            file2.getParentFile().mkdirs();
            file2.createNewFile();
            try (FileWriter writer = new FileWriter(file2)) {
                writer.write(rates.get(to) + "\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
