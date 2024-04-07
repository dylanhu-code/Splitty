package server.api;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FakeExchangeConverterControllerTest {


    @Test
    void testFakeExchangeConverter_Success() {
        String date = "1648896000000";
        String baseCurrency = "USD";
        String targetCurrency = "EUR";
        FakeExchangeConverterController controller = new FakeExchangeConverterController();
        ResponseEntity<Map<String, Double>> responseEntity = controller.fakeExchangeConverter(date, baseCurrency, targetCurrency);

        assertEquals(200, responseEntity.getStatusCodeValue());
        Map<String, Double> responseBody = responseEntity.getBody();
        assertNotNull(responseBody);
        assertTrue(responseBody.containsKey("base_to_target"));
        assertTrue(responseBody.containsKey("target_to_base"));
        assertTrue(responseBody.get("base_to_target") instanceof Double);
        assertTrue(responseBody.get("target_to_base") instanceof Double);
    }

    @Test
    void testFakeExchangeConverter_BadRequest() {
        String date = "invalid_date";
        String baseCurrency = "USD";
        String targetCurrency = "EUR";
        FakeExchangeConverterController controller = new FakeExchangeConverterController();
        ResponseEntity<Map<String, Double>> responseEntity = controller.fakeExchangeConverter(date, baseCurrency, targetCurrency);

        assertEquals(400, responseEntity.getStatusCodeValue());
    }

}