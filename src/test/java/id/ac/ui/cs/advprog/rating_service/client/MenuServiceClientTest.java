package id.ac.ui.cs.advprog.rating_service.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MenuServiceClientTest {

    private RestTemplate restTemplate;
    private MenuServiceClient menuServiceClient;
    private final String BASE_URL = "http://localhost:8081";

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        menuServiceClient = new MenuServiceClient(restTemplate, BASE_URL);
    }

    @Test
    void testGetMenuItemById_success() {
        UUID menuId = UUID.randomUUID();
        MenuServiceClient.MenuItemResponse mockResponse = new MenuServiceClient.MenuItemResponse();
        mockResponse.setId(menuId);
        mockResponse.setName("Nasi Goreng");
        mockResponse.setDescription("Spicy");
        mockResponse.setPrice(20.0);
        mockResponse.setAvailable(true);

        String url = BASE_URL + "/menu/" + menuId;
        when(restTemplate.getForObject(url, MenuServiceClient.MenuItemResponse.class)).thenReturn(mockResponse);

        MenuServiceClient.MenuItemResponse response = menuServiceClient.getMenuItemById(menuId);
        assertNotNull(response);
        assertEquals(menuId, response.getId());
        assertEquals("Nasi Goreng", response.getName());
    }


    @Test
    void testGetMenuItemById_otherException() {
        UUID menuId = UUID.randomUUID();
        String url = BASE_URL + "/menu/" + menuId;

        when(restTemplate.getForObject(url, MenuServiceClient.MenuItemResponse.class))
                .thenThrow(new RuntimeException("Timeout"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            menuServiceClient.getMenuItemById(menuId);
        });

        assertTrue(ex.getMessage().contains("Menu Service unavailable"));
    }
}
