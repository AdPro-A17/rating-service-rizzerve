package id.ac.ui.cs.advprog.ratingservice.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RatingTest {

    Rating rating;

    @BeforeEach
    void setUp() {
        this.rating = new Rating();
        this.rating.setUserId("user123");
        this.rating.setMenuItemId("menu456");
        this.rating.setScore(4);
    }

    @Test
    void testGetUserId() {
        assertEquals("user123", this.rating.getUserId());
    }

    @Test
    void testGetMenuItemId() {
        assertEquals("menu456", this.rating.getMenuItemId());
    }

    @Test
    void testGetScore() {
        assertEquals(4, this.rating.getScore());
    }
}
