package com.example.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PinCodeService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson ObjectMapper

    public String[] getCityAndCountry(String pinCode) {
        try {
            // API Call (Using Zippopotam.us)
            String url = "http://api.zippopotam.us/in/" + pinCode;
            String response = restTemplate.getForObject(url, String.class);

            // Parse JSON Response using Jackson
            if (response != null) {
                JsonNode jsonResponse = objectMapper.readTree(response);
                JsonNode placesArray = jsonResponse.get("places");

                if (placesArray != null && placesArray.isArray() && placesArray.size() > 0) {
                    JsonNode place = placesArray.get(0);
                    String city = place.get("place name").asText();
                    String country = jsonResponse.get("country").asText();

                    return new String[]{city, country};
                }
            }
        } catch (Exception e) {
            System.out.println("Error fetching pin code details: " + e.getMessage());
        }

        // Default values if API fails
        return new String[]{"Unknown City", "Unknown Country"};
    }
}

