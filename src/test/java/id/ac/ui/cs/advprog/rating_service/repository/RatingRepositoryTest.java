package id.ac.ui.cs.advprog.rating_service.repository;

import id.ac.ui.cs.advprog.rating_service.model.Rating;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RatingRepositoryTest {

    private RatingRepository ratingRepository;
    private Rating rating;

    @BeforeEach
    void setUp() {
        ratingRepository = new RatingRepository(); // class ini belum ada → RED
        rating = new Rating();

        rating.setRatingId(UUID.randomUUID());
        rating.setUserId(UUID.randomUUID());
        rating.setItemId(UUID.randomUUID());
        rating.setValue(5);
    }

    @Test
    void testSaveRating() {
        Rating savedRating = ratingRepository.save(rating);
        assertEquals(rating, savedRating);
    }

    @Test
    void testFindByIdReturnsRating() {
        ratingRepository.save(rating);
        Optional<Rating> result = ratingRepository.findById(rating.getRatingId());

        assertTrue(result.isPresent());
        assertEquals(rating, result.get());
    }

    @Test
    void testFindByIdReturnsEmptyIfNotFound() {
        Optional<Rating> result = ratingRepository.findById(UUID.randomUUID());
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindAllReturnsAllRatings() {
        Rating anotherRating = new Rating();
        anotherRating.setRatingId(UUID.randomUUID());
        anotherRating.setUserId(UUID.randomUUID());
        anotherRating.setItemId(UUID.randomUUID());
        anotherRating.setValue(3);

        ratingRepository.save(rating);
        ratingRepository.save(anotherRating);

        List<Rating> allRatings = ratingRepository.findAll();

        assertEquals(2, allRatings.size());
        assertTrue(allRatings.contains(rating));
        assertTrue(allRatings.contains(anotherRating));
    }

    @Test
    void testDeleteRating() {
        ratingRepository.save(rating);
        ratingRepository.delete(rating.getRatingId());

        Optional<Rating> result = ratingRepository.findById(rating.getRatingId());
        assertTrue(result.isEmpty());
    }
}
