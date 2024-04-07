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
        if (ratesAreCached(date, from, to)) {
            return fetchRatesFromCache(date, from, to);
        } else {
            Map<String, Double> rates = fetchRatesUsingFakeConverter(date, from, to);
            cacheRates(date, from, to, rates);
            return rates;
        }
    }

    /**
     * Check if the rates are cached
     * @param date - the date
     * @param from - the first currency
     * @param to - the second currency
     */
    private boolean ratesAreCached(String date, String from, String to) {
        String cacheFilePath = getCacheFilePath(date, from, to);
        File cacheFile = new File(cacheFilePath);
        return cacheFile.exists();
    }//TODO returns true even if it doesnt exist
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
    private Map<String, Double> fetchRatesFromCache(String date, String from, String to) {
        Map<String, Double> rates = new HashMap<>();
        String cacheFilePath = getCacheFilePath(date, from, to);
        try (Scanner reader = new Scanner(new File(cacheFilePath))) {
            String line;
            while ((line = reader.nextLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    String currency = parts[0];
                    double rate = Double.parseDouble(parts[1]);
                    rates.put(currency, rate);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
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
    private Map<String, Double> fetchRatesUsingFakeConverter(String date, String from, String to) {
        Map<String, Double> rates = new HashMap<>();
        try {
            double rate = generateBaseRate(date);

            double reciprocalRate = 1 / rate;

            Map<String, Double> response = new HashMap<>();
            response.put(from, rate);
            response.put(to, reciprocalRate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rates;
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
    private void cacheRates(String date, String from, String to, Map<String, Double> rates) {
        try {
            File cacheDir = new File(CACHE_DIRECTORY);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }

            String cacheFilePath = getCacheFilePath(date, from, to);
            try (FileWriter writer = new FileWriter(cacheFilePath)) {
                for (Map.Entry<String, Double> entry : rates.entrySet()) {
                    writer.write(entry.getKey() + ":" + entry.getValue() + "\n");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
