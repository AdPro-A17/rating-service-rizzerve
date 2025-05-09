package id.ac.ui.cs.advprog.rating_service.service;

import id.ac.ui.cs.advprog.rating_service.model.Rating;
import id.ac.ui.cs.advprog.rating_service.repository.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RatingServiceImplTest {

    @InjectMocks
    private RatingServiceImpl ratingService;

    @Mock
    private RatingRepository ratingRepository;

    private Rating rating;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks

        rating = new Rating();
        rating.setRatingId(UUID.randomUUID());
        rating.setUserId(UUID.randomUUID());
        rating.setItemId(UUID.randomUUID());
        rating.setValue(4);
    }

    @Test
    void testSave() {
        // Mock the save method in the repository
        when(ratingRepository.save(rating)).thenReturn(rating);

        // Call the service method
        Rating savedRating = ratingService.save(rating);

        // Assert the saved rating is the same as the one returned by the repository
        assertEquals(rating, savedRating);
        verify(ratingRepository).save(rating); // Verify that save method was called
    }

    @Test
    void testFindAll() {
        // Create a list of ratings
        List<Rating> ratings = new ArrayList<>();
        ratings.add(rating);

        // Mock the repository method to return the list
        when(ratingRepository.findAll()).thenReturn(ratings);

        // Call the service method
        List<Rating> allRatings = ratingService.findAll();

        // Assert the result is a List containing the single rating
        assertNotNull(allRatings);
        assertEquals(1, allRatings.size());
        assertEquals(rating, allRatings.get(0));
        verify(ratingRepository).findAll();  // Verify that findAll method was called
    }

    @Test
    void testFindById() {
        UUID ratingId = rating.getRatingId();

        // Mock the repository to return the rating when finding by ID
        when(ratingRepository.findById(ratingId)).thenReturn(Optional.of(rating));

        // Call the service method
        Optional<Rating> foundRating = ratingService.findById(ratingId);

        // Assert that the found rating is present and matches the expected rating
        assertTrue(foundRating.isPresent());
        assertEquals(rating, foundRating.get());
        verify(ratingRepository).findById(ratingId); // Verify that findById method was called
    }

    @Test
    void testUpdate() {
        // Mock the repository method to return the rating when finding by ID
        UUID ratingId = rating.getRatingId();
        when(ratingRepository.findById(ratingId)).thenReturn(Optional.of(rating));
        when(ratingRepository.save(rating)).thenReturn(rating);

        // Call the service method
        Rating updatedRating = ratingService.update(rating);

        // Assert that the updated rating is the same as the one returned by the repository
        assertEquals(rating, updatedRating);
        verify(ratingRepository).findById(ratingId); // Verify that findById method was called
        verify(ratingRepository).save(rating);      // Verify that save method was called
    }

    @Test
    void testUpdateNotFound() {
        // Mock the repository to return empty when looking for the rating by ID
        when(ratingRepository.findById(rating.getRatingId())).thenReturn(Optional.empty());

        // Call the service method and assert that an exception is thrown
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ratingService.update(rating);
        });

        // Assert the exception message
        assertEquals("Rating not found", exception.getMessage());
        verify(ratingRepository).findById(rating.getRatingId()); // Verify that findById method was called
        verify(ratingRepository, never()).save(rating);        // Ensure save was never called
    }

    @Test
    void testDeleteById() {
        UUID ratingId = UUID.randomUUID();

        // Mock the delete method to do nothing when called
        doNothing().when(ratingRepository).delete(ratingId);

        // Call the service method
        ratingService.deleteById(ratingId);

        // Verify that delete method was called
        verify(ratingRepository).delete(ratingId);
    }
}
