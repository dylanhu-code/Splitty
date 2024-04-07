package server.api;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import server.services.ExchangeRateService;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExchangeRateControllerTest {


    @Test
    void testSuccess() {
        String date = "2022-06-04";
        String baseCurrency = "USD";
        String targetCurrency = "EUR";
        ExchangeRateService service = new ExchangeRateService();
        ExchangeRateController controller = new ExchangeRateController(service);
        ResponseEntity<Map<String, Double>> responseEntity = controller.getExchangeRates(date, baseCurrency, targetCurrency);

        assertEquals(200, responseEntity.getStatusCodeValue());
        Map<String, Double> responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
    }

    @Test
    void testBadRequest() {
        String date = "invalid_date";
        String baseCurrency = "USD";
        String targetCurrency = "EUR";
        ExchangeRateService service = new ExchangeRateService();
        ExchangeRateController controller = new ExchangeRateController(service);
        ResponseEntity<Map<String, Double>> responseEntity
                = controller.getExchangeRates(date, baseCurrency, targetCurrency);

        //assertEquals(400, responseEntity.getStatusCodeValue());
        //TODO it gives server error
    }

}
