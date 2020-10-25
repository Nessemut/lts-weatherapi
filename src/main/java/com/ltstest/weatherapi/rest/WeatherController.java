package com.ltstest.weatherapi.rest;

import com.ltstest.weatherapi.service.OwmClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;


@RestController
public class WeatherController {

    @GetMapping("/weather/{city}")
    public ResponseEntity<?> weather(@PathVariable String city) {
        int status = 200;
        HashMap<String, Object> map = new HashMap<>();
        try {
            OwmClient owmClient = new OwmClient(city);
            map.put("condition", owmClient.getDescription());
            map.put("temperature", owmClient.getTemp());
        } catch (ResponseStatusException e) {
            status = e.getStatus().value();
            map.put("error", e.getReason());
        }
        return ResponseEntity.status(status).body(map);
    }
}
