package server.api;

import com.google.inject.Inject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import server.services.ExchangeRateService;

import java.util.Map;

@RestController
@RequestMapping("/api/exchange")
public class ExchangeRateController {

    @Autowired
    private ExchangeRateService exchangeRateService;

    @GetMapping("/exchange-rates")
    public ResponseEntity<Map<String, Double>> getExchangeRates(
            @RequestParam String date,
            @RequestParam String[] currencies
    ) {
        try {
            Map<String, Double> exchangeRates = exchangeRateService.getExchangeRates(date, currencies);
            return ResponseEntity.ok().body(exchangeRates);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
