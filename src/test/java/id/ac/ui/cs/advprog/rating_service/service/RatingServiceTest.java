ppackage id.ac.ui.cs.advprog.rating_service.service;

import id.ac.ui.cs.advprog.rating_service.model.Rating;
import id.ac.ui.cs.advprog.rating_service.repository.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RatingServiceTest {

    private RatingRepository ratingRepository;
    private RatingService ratingService;

    @BeforeEach
    void setUp() {
        ratingRepository = mock(RatingRepository.class);
        ratingService = new RatingServiceImpl(ratingRepository);
    }

    @Test
    void testAddRating() {
        Rating rating = new Rating("food1", "user1", 4);
        when(ratingRepository.save(rating)).thenReturn(rating);

        Rating result = ratingService.addRating(rating);
        assertEquals(4, result.getRatingValue());
        verify(ratingRepository, times(1)).save(rating);
    }

    @Test
    void testGetRatingById() {
        Rating rating = new Rating("food1", "user1", 4);
        when(ratingRepository.findById("food1-user1")).thenReturn(Optional.of(rating));

        Optional<Rating> result = ratingService.getRatingById("food1-user1");
        assertTrue(result.isPresent());
        assertEquals("user1", result.get().getUserId());
    }

    @Test
    void testDeleteRating() {
        String id = "food1-user1";
        doNothing().when(ratingRepository).deleteById(id);

        ratingService.deleteRatingById(id);
        verify(ratingRepository, times(1)).deleteById(id);
    }

    @Test
    void testGetRatingsByItemId() {
        Rating rating1 = new Rating("food1", "user1", 4);
        Rating rating2 = new Rating("food1", "user2", 5);
        when(ratingRepository.findByItemId("food1")).thenReturn(List.of(rating1, rating2));

        List<Rating> ratings = ratingService.getRatingsByItemId("food1");
        assertEquals(2, ratings.size());
    }
}

