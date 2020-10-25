package com.ltstest.weatherapi.rest;

import com.ltstest.weatherapi.service.OwmClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;


@RestController
public class WeatherController {

    @GetMapping("/weather/{city}")
    public HashMap<String, Object> weather(@PathVariable String city) {
        OwmClient owmClient = new OwmClient(city);
        HashMap<String, Object> map = new HashMap<>();
        map.put("condition", owmClient.getDescription());
        map.put("temperature", owmClient.getTemp());

        return map;
    }
}
