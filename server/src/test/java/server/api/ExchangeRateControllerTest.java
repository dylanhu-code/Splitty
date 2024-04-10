package server.api;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import server.services.ExchangeRateService;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


class ExchangeRateControllerTest {


    /**
     * checkstyle
     */
    @Test
    void testSuccess() {
        String date = "2022-06-01";
        String baseCurrency = "CHF";
        String targetCurrency = "EUR";
        ExchangeRateService service = new ExchangeRateService();
        ExchangeRateController controller = new ExchangeRateController(service);
        ResponseEntity<Map<String, Double>> responseEntity
                = controller.getExchangeRates(date, baseCurrency, targetCurrency);

        assertEquals(200, responseEntity.getStatusCodeValue());
        Map<String, Double> responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
    }

    /**
     * checkstyle
     */
    @Test
    void testBadRequest() {
        String date = "inva";
        String baseCurrency = "USD";
        String targetCurrency = "EUR";
        ExchangeRateService service = new ExchangeRateService();
        ExchangeRateController controller = new ExchangeRateController(service);
        ResponseEntity<Map<String, Double>> responseEntity
                = controller.getExchangeRates(date, baseCurrency, targetCurrency);

        assertEquals(400, responseEntity.getStatusCodeValue());
    }

    @Test
    void testSameCurrency() {
        String date = "2022-06-01";
        String baseCurrency = "CHF";
        String targetCurrency = "CHF";
        ExchangeRateService service = new ExchangeRateService();
        ExchangeRateController controller = new ExchangeRateController(service);
        ResponseEntity<Map<String, Double>> responseEntity
                = controller.getExchangeRates(date, baseCurrency, targetCurrency);

        Map<String, Double> responseBody = responseEntity.getBody();
        assertEquals(1.0, responseBody.get(baseCurrency));
    }

}
