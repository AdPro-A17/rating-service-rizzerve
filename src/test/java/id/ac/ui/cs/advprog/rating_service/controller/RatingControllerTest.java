package id.ac.ui.cs.advprog.rating_service.controller;

import id.ac.ui.cs.advprog.rating_service.model.Rating;
import id.ac.ui.cs.advprog.rating_service.service.RatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

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
        sampleRating.setUserId(UUID.randomUUID());
        sampleRating.setItemId(UUID.randomUUID());
        sampleRating.setValue(4);
    }

    // Happy Path: Membuat rating baru
    @Test
    void testCreateRatingSuccessfully() {
        when(ratingService.save(any(Rating.class))).thenReturn(sampleRating); // Sesuaikan dengan method save()

        ResponseEntity<Rating> response = controller.createRating(sampleRating);

        assertEquals(200, response.getStatusCodeValue(), "Response status should be 200 OK");
        assertEquals(sampleRating, response.getBody(), "Returned rating should match the sample");
        verify(ratingService, times(1)).save(sampleRating); // Sesuaikan dengan method save()
    }

    // Happy Path: Mendapatkan semua rating
    @Test
    void testGetAllRatingsSuccessfully() {
        List<Rating> mockList = List.of(sampleRating);
        when(ratingService.findAll()).thenReturn(mockList); // Sesuaikan dengan method findAll()

        ResponseEntity<List<Rating>> response = controller.getAllRatings();

        assertEquals(200, response.getStatusCodeValue(), "Response status should be 200 OK");
        assertEquals(mockList, response.getBody(), "Returned list should contain sampleRating");
    }

    // Happy Path: Menghapus rating
    @Test
    void testDeleteRatingSuccessfully() {
        UUID ratingId = sampleRating.getRatingId();

        ResponseEntity<Void> response = controller.deleteRating(ratingId);

        assertEquals(204, response.getStatusCodeValue(), "Response status should be 204 No Content");
        verify(ratingService, times(1)).deleteById(ratingId); // Sesuaikan dengan method deleteById()
    }

    // Unhappy Path: Coba buat rating null
    @Test
    void testCreateNullRating() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.createRating(null);
        });
        assertEquals("Rating must not be null", exception.getMessage());
    }

    // Unhappy Path: Hapus rating dengan ID null
    @Test
    void testDeleteRatingWithNullId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            controller.deleteRating(null);
        });
        assertEquals("Rating ID must not be null", exception.getMessage());
    }

    @Test
    void testGetAverageRatingByItemIdSuccessfully() {
        UUID itemId = sampleRating.getItemId();
        double avgRating = 4.5;

        when(ratingService.getAverageRatingByItemId(itemId)).thenReturn(avgRating);

        ResponseEntity<Double> response = controller.getAverageRatingByItemId(itemId);

        assertEquals(200, response.getStatusCodeValue(), "Response status should be 200 OK");
        assertEquals(avgRating, response.getBody(), "Returned average rating should match");
        verify(ratingService, times(1)).getAverageRatingByItemId(itemId);
    }
}
