package id.ac.ui.cs.advprog.rating_service.repository;

import id.ac.ui.cs.advprog.rating_service.model.Rating;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class RatingRepositoryTest {

    @Autowired
    private RatingRepository ratingRepository;

    private Rating rating;
    private UUID itemId;

    @BeforeEach
    void setUp() {
        rating = new Rating();
        rating.setRatingId(UUID.randomUUID());
        rating.setUserId(UUID.randomUUID());

        itemId = UUID.randomUUID();
        rating.setItemId(itemId);
        rating.setValue(5);

        ratingRepository.save(rating);
    }

    @Test
    void testFindByItemId() {
        List<Rating> results = ratingRepository.findByItemId(itemId);
        assertEquals(1, results.size(), "Should return exactly one result");
        assertEquals(rating.getValue(), results.get(0).getValue(), "Rating value should match");
        assertEquals(itemId, results.get(0).getItemId(), "Item ID should match");
    }

    @Test
    void testDeleteById() {
        ratingRepository.deleteById(rating.getRatingId());
        Optional<Rating> result = ratingRepository.findById(rating.getRatingId());
        assertTrue(result.isEmpty(), "Deleted rating should not be present in repository");
    }
}
