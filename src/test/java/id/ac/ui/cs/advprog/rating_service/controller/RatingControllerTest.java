package id.ac.ui.cs.advprog.rating_service.controller;

import id.ac.ui.cs.advprog.rating_service.exception.RatingNotFoundException;
import id.ac.ui.cs.advprog.rating_service.model.Rating;
import id.ac.ui.cs.advprog.rating_service.service.RatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RatingControllerTest {

    private RatingController controller;
    private RatingService ratingService;

    private Rating sampleRating;

    @BeforeEach
    void setUp() {
        ratingService = mock(RatingService.class);
        controller = new RatingController(ratingService);

        sampleRating = new Rating();
        sampleRating.setRatingId(UUID.randomUUID());
        sampleRating.setMejaId(UUID.randomUUID());
        sampleRating.setItemId(UUID.randomUUID());
        sampleRating.setValue(4);
    }

    // --- HAPPY PATH ---

    @Test
    void testCreateRatingSuccessfully() {
        when(ratingService.save(any(Rating.class))).thenReturn(sampleRating);

        ResponseEntity<Rating> response = controller.createRating(sampleRating);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleRating, response.getBody());
        verify(ratingService, times(1)).save(sampleRating);
    }

    @Test
    void testGetAllRatingsSuccessfully() {
        List<Rating> mockList = List.of(sampleRating);
        when(ratingService.findAll()).thenReturn(mockList);

        ResponseEntity<List<Rating>> response = controller.getAllRatings();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockList, response.getBody());
    }

    @Test
    void testGetRatingByIdFound() {
        when(ratingService.findById(sampleRating.getRatingId())).thenReturn(Optional.of(sampleRating));

        ResponseEntity<Rating> response = controller.getRatingById(sampleRating.getRatingId());

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleRating, response.getBody());
    }

    @Test
    void testUpdateRatingSuccessfully() {
        when(ratingService.update(sampleRating)).thenReturn(sampleRating);

        ResponseEntity<Rating> response = controller.updateRating(sampleRating.getRatingId(), sampleRating);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(sampleRating, response.getBody());
        verify(ratingService, times(1)).update(sampleRating);
    }

    @Test
    void testDeleteRatingSuccessfully() {
        UUID ratingId = sampleRating.getRatingId();

        ResponseEntity<Void> response = controller.deleteRating(ratingId);

        assertEquals(204, response.getStatusCodeValue());
        verify(ratingService, times(1)).deleteById(ratingId);
    }

    @Test
    void testGetRatingsByItemId() {
        UUID itemId = sampleRating.getItemId();
        when(ratingService.findByItemId(itemId)).thenReturn(List.of(sampleRating));

        ResponseEntity<List<Rating>> response = controller.getRatingsByItemId(itemId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetAverageRatingByItemIdSuccessfully() {
        UUID itemId = sampleRating.getItemId();
        double avgRating = 4.5;

        when(ratingService.getAverageRatingByItemId(itemId)).thenReturn(avgRating);

        ResponseEntity<Double> response = controller.getAverageRatingByItemId(itemId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(avgRating, response.getBody());
    }

    @Test
    void testGetRatingsByMejaId() {
        UUID mejaId = sampleRating.getMejaId();
        when(ratingService.findByMejaId(mejaId)).thenReturn(List.of(sampleRating));

        ResponseEntity<List<Rating>> response = controller.getRatingsByMejaId(mejaId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testDisableRatingsForMeja() {
        UUID mejaId = sampleRating.getMejaId();

        ResponseEntity<Void> response = controller.disableRatingsForMeja(mejaId);

        assertEquals(200, response.getStatusCodeValue());
        verify(ratingService, times(1)).disableUpdatesForMeja(mejaId);
    }


    // --- UNHAPPY PATH ---

    @Test
    void testCreateNullRating() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.createRating(null);
        });
        assertEquals("Rating must not be null", exception.getMessage());
    }

    @Test
    void testDeleteRatingWithNullId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.deleteRating(null);
        });
        assertEquals("Rating ID must not be null", exception.getMessage());
    }

    @Test
    void testGetRatingByIdNotFound() {
        UUID notFoundId = UUID.randomUUID();
        when(ratingService.findById(notFoundId)).thenReturn(Optional.empty());

        ResponseEntity<Rating> response = controller.getRatingById(notFoundId);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    @Test
    void testUpdateRatingWithMismatchedId() {
        UUID pathId = UUID.randomUUID(); // berbeda dengan di body
        sampleRating.setRatingId(UUID.randomUUID());

        ResponseEntity<Rating> response = controller.updateRating(pathId, sampleRating);

        assertEquals(400, response.getStatusCodeValue());
        verify(ratingService, never()).update(any());
    }

    @Test
    void testHandleRatingNotFoundException() {
        String errorMessage = "Rating tidak ditemukan";
        RatingNotFoundException exception = new RatingNotFoundException(errorMessage);

        ResponseEntity<String> response = controller.handleRatingNotFoundException(exception);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals(errorMessage, response.getBody());
    }
}
