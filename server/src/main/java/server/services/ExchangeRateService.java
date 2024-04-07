package server.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class ExchangeRateService {
    private String fakeExchangeConverterUrl;
    private static final String CACHE_DIRECTORY = "rates";

    /**
     * Check if rates are available in cache
     * @param date the date of the exchange rate
     * @param currencies the requested currencies
     * @return a map with the currency and the rate
     */
    public Map<String, Double> getExchangeRates(String date, String[] currencies) {
        if (ratesAreCached(date, currencies)) {
            return fetchRatesFromCache(date, currencies);
        } else {
            Map<String, Double> rates = fetchRatesFromFakeConverter(date, currencies);
            cacheRates(date, currencies, rates);
            return rates;
        }
    }

    /**
     *Check if the rates are cached
     * @param date the date of the rate
     * @param currencies the currencies
     * @return true if the rate is cached and false otherwise
     */
    private boolean ratesAreCached(String date, String[] currencies) {
        String cacheFilePath = getCacheFilePath(date, currencies);
        File cacheFile = new File(cacheFilePath);
        return cacheFile.exists();
    }

    /**
     * Creates the file path for the requested date and currencies
     * @param date the date of the exchange rate
     * @param currencies the currencies
     * @return the file path
     */
    private String getCacheFilePath(String date, String[] currencies) {
        StringBuilder filePathBuilder = new StringBuilder();
        filePathBuilder.append(CACHE_DIRECTORY);
        filePathBuilder.append(File.separator);
        filePathBuilder.append(date);
        for (String currency : currencies) {
            filePathBuilder.append(File.separator);
            filePathBuilder.append(currency);
        }
        filePathBuilder.append(".txt");
        return filePathBuilder.toString();
    }

    /**
     * Retrieve rates from the cache
     * @param date the date of the requested exchange rate
     * @param currencies the list of currencies
     * @return the rates for each currency
     */
    private Map<String, Double> fetchRatesFromCache(String date, String[] currencies) {
        Map<String, Double> rates = new HashMap<>();
        String cacheFilePath = getCacheFilePath(date, currencies);
        try (BufferedReader reader = new BufferedReader(new FileReader(cacheFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
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
     * Fetch rates from the exchange rate service
     * @param date the date of the requested exchange rate
     * @param currencies the list of currencies
     * @return a map with the currency and the rate
     */
    private Map<String, Double> fetchRatesFromFakeConverter(String date, String[] currencies) {
        Map<String, Double> rates = new HashMap<>();
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = fakeExchangeConverterUrl + "?date=" + date + "&baseCurrency=" + currencies[0] + "&targetCurrency=" + currencies[1];
            Map<String, Double> response = restTemplate.getForObject(url, Map.class);

            rates.put("base_to_target", response.get("base_to_target"));
            rates.put("target_to_base", response.get("target_to_base"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rates;
    }

    /**
     * Cache rates for future use
     * @param date the date of the requested exchange rate
     * @param currencies the list of currencies
     * @param rates the map containing the exchange rates
     */
    private void cacheRates(String date, String[] currencies, Map<String, Double> rates) {
        try {
            File cacheDir = new File(CACHE_DIRECTORY);
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }

            String cacheFilePath = getCacheFilePath(date, currencies);

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
