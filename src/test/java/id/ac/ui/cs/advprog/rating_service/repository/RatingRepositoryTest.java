package id.ac.ui.cs.advprog.rating_service.repository;

import id.ac.ui.cs.advprog.rating_service.model.Rating;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RatingRepositoryTest {
    RatingRepository ratingRepository;

    @BeforeEach
    void setUp() {
        ratingRepository = new RatingRepository();
    }

    @Test
    void testCreateRating() {
        Rating rating = new Rating();
        rating.setUserId("user1");
        rating.setMenuId("menu1");
        rating.setScore(4);

        Rating savedRating = ratingRepository.create(rating);

        assertNotNull(savedRating.getId());
        assertEquals("user1", savedRating.getUserId());
        assertEquals("menu1", savedRating.getMenuId());
        assertEquals(4, savedRating.getScore());
    }

    @Test
    void testFindAllRatings() {
        Rating rating1 = new Rating();
        rating1.setUserId("user1");
        rating1.setMenuId("menu1");
        rating1.setScore(4);

        Rating rating2 = new Rating();
        rating2.setUserId("user2");
        rating2.setMenuId("menu2");
        rating2.setScore(5);

        ratingRepository.create(rating1);
        ratingRepository.create(rating2);

        List<Rating> ratings = ratingRepository.findAll();
        assertEquals(2, ratings.size());
    }

    @Test
    void testUpdateRating() {
        Rating rating = new Rating();
        rating.setUserId("user1");
        rating.setMenuId("menu1");
        rating.setScore(3);

        Rating saved = ratingRepository.create(rating);
        saved.setScore(5);

        Rating updated = ratingRepository.update(saved);

        assertEquals(5, updated.getScore());
    }

    @Test
    void testDeleteRating() {
        Rating rating = new Rating();
        rating.setUserId("user1");
        rating.setMenuId("menu1");
        rating.setScore(4);

        Rating saved = ratingRepository.create(rating);
        ratingRepository.delete(saved.getId());

        List<Rating> ratings = ratingRepository.findAll();
        assertEquals(0, ratings.size());
    }
}

