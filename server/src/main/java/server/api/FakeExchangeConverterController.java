package server.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
public class FakeExchangeConverterController {

    @GetMapping("/fake_exchange_converter")
    public ResponseEntity<Map<String, Double>> fakeExchangeConverter(
            @RequestParam String date,
            @RequestParam String baseCurrency,
            @RequestParam String targetCurrency
    ) {
        try {
            double rate = generateBaseRate(date);

            double reciprocalRate = 1 / rate;

            Map<String, Double> response = new HashMap<>();
            response.put("base_to_target", rate);
            response.put("target_to_base", reciprocalRate);
            return ResponseEntity.ok().body(response);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     *   Generate base rate using timestamp as seed for stability
     */
    private double generateBaseRate(String date) {
        long timestamp = Long.parseLong(date);
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
}
