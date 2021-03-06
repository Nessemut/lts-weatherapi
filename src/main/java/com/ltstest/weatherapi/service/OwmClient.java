package com.ltstest.weatherapi.service;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.filter.LoggingFilter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class OwmClient {

    private String city;
    private String description;
    private String temp;

    private final String URL = "https://api.openweathermap.org/data/2.5/weather";
    private final String APIKEY = "8e251fd34daa0f4af9187c3a7133329d";

    public OwmClient(String city) {
        this.setCity(city);
        apiCall();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    private void apiCall() throws ResponseStatusException {
        Client client = ClientBuilder.newClient( new ClientConfig().register( LoggingFilter.class) );
        city = city.replace("%20", "+").replace(" ", "+");
        WebTarget webTarget = client.target(String.format("%s?q=%s&APPID=%s&units=metric", URL, city, APIKEY));
        Invocation.Builder invocationBuilder;
        invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);

        Response response = invocationBuilder.get();
        if (response.getStatus() == 200) {
            JSONObject jsonObject = new JSONObject(response.readEntity(String.class));
            JSONObject main = (JSONObject) jsonObject.get("main");
            String temp = main.get("temp").toString();
            this.setTemp(temp);
            JSONArray weather = (JSONArray) jsonObject.get("weather");
            String description = (String) weather.getJSONObject(0).get("description");
            this.setDescription(StringUtils.capitalize(description));
        } else if (response.getStatus() == 404) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "City " + this.city + " not found"
            );
        } else {
            HttpStatus status = HttpStatus.resolve(response.getStatus());
            throw new ResponseStatusException(status, status.getReasonPhrase());
        }
    }
}
