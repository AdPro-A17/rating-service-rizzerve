package id.ac.ui.cs.advprog.rating_service.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.UUID;

@Component
@Slf4j
public class MenuServiceClient {

    private final RestTemplate restTemplate;
    private final String menuServiceUrl;

    public MenuServiceClient(RestTemplate restTemplate,
                             @Value("${menu.service.url:http://localhost:8081}") String menuServiceUrl) {
        this.restTemplate = restTemplate;
        this.menuServiceUrl = menuServiceUrl;
    }

    public MenuItemResponse getMenuItemById(UUID menuItemId) {
        try {
            String url = menuServiceUrl + "/menu/" + menuItemId;
            log.info("Calling Menu Service: {}", url);
            return restTemplate.getForObject(url, MenuItemResponse.class);
        } catch (HttpClientErrorException.NotFound e) {
            log.warn("Menu item not found: {}", menuItemId);
            return null;
        } catch (Exception e) {
            log.error("Error calling Menu Service for item {}: {}", menuItemId, e.getMessage());
            throw new RuntimeException("Menu Service unavailable", e);
        }
    }

    public static class MenuItemResponse {
        private UUID id;
        private String name;
        private String description;
        private Double price;
        private Boolean available;

        // getters & setters
        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }
        public Boolean getAvailable() { return available; }
        public void setAvailable(Boolean available) { this.available = available; }
    }
}
