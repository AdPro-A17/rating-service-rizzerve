package id.ac.ui.cs.advprog.rating_service.model;
import id.ac.ui.cs.advprog.rating_service.model.Rating;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RatingTest {
    private Rating rating;
    private UUID ratingId;
    private UUID userId;
    private UUID itemId;

    @BeforeEach
    void setUp() {
        ratingId = UUID.randomUUID();
        userId = UUID.randomUUID();
        itemId = UUID.randomUUID();

        rating = new Rating();
        rating.setRatingId(ratingId);
        rating.setUserId(userId);
        rating.setItemId(itemId);
        rating.setValue(4); // valid default
    }

    @Test
    void testSetAndGetRatingId() {
        assertEquals(ratingId, rating.getRatingId(), "Rating ID should match the assigned UUID");
    }

    @Test
    void testSetAndGetUserId() {
        assertEquals(userId, rating.getUserId(), "User ID should match the assigned UUID");
    }

    @Test
    void testSetAndGetItemId() {
        assertEquals(itemId, rating.getItemId(), "Item ID should match the assigned UUID");
    }

    @Test
    void testSetAndGetValue() {
        assertEquals(4, rating.getValue(), "Rating value should be 4");
    }

    @Test
    void testSetInvalidLowValue() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            rating.setValue(0);
        });
        assertEquals("Rating value must be between 1 and 5", exception.getMessage());
    }

    @Test
    void testSetInvalidHighValue() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            rating.setValue(6);
        });
        assertEquals("Rating value must be between 1 and 5", exception.getMessage());
    }

    @Test
    void testSetNullUserId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            rating.setUserId(null);
        });
        assertEquals("User ID cannot be null", exception.getMessage());
    }

    @Test
    void testSetNullItemId() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            rating.setItemId(null);
        });
        assertEquals("Item ID cannot be null", exception.getMessage());
    }
}
