package server.api;

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

    private ExchangeRateService exchangeRateService;

    @Autowired
    public ExchangeRateController(ExchangeRateService exchangeRateService){this.exchangeRateService = exchangeRateService;}

    @GetMapping("/exchange-rates")
    public ResponseEntity<Map<String, Double>> getExchangeRates(
            @RequestParam String date,
            @RequestParam String from,
            @RequestParam String to
    ) {
        try {
            Map<String, Double> exchangeRates
                    = exchangeRateService.getExchangeRates(date, from, to);
            return ResponseEntity.ok().body(exchangeRates);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
