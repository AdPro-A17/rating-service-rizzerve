package id.ac.ui.cs.advprog.rating_service.service;

import id.ac.ui.cs.advprog.rating_service.model.Rating;
import id.ac.ui.cs.advprog.rating_service.repository.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RatingServiceImplTest {

    @InjectMocks
    private RatingServiceImpl ratingService;

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private RatingObserver observer;

    private Rating rating;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rating = new Rating();
        rating.setRatingId(UUID.randomUUID());
        rating.setUserId(UUID.randomUUID());
        rating.setItemId(UUID.randomUUID());
        rating.setValue(4);

        ratingService.addObserver(observer); // daftarkan observer ke service
    }

    @Test
    void testSave() {
        when(ratingRepository.save(rating)).thenReturn(rating);
        when(ratingRepository.findAll()).thenReturn(List.of(rating));

        Rating saved = ratingService.save(rating);

        assertEquals(rating, saved);
        verify(ratingRepository).save(rating);
        verify(observer).updateRating(eq(rating.getItemId()), eq(4.0));
    }

    @Test
    void testFindAll() {
        when(ratingRepository.findAll()).thenReturn(List.of(rating));

        List<Rating> result = ratingService.findAll();

        assertEquals(1, result.size());
        assertEquals(rating, result.get(0));
        verify(ratingRepository).findAll();
    }

    @Test
    void testFindById() {
        UUID id = rating.getRatingId();
        when(ratingRepository.findById(id)).thenReturn(Optional.of(rating));

        Optional<Rating> result = ratingService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(rating, result.get());
    }

    @Test
    void testUpdate() {
        UUID id = rating.getRatingId();
        when(ratingRepository.findById(id)).thenReturn(Optional.of(rating));
        when(ratingRepository.save(rating)).thenReturn(rating);
        when(ratingRepository.findAll()).thenReturn(List.of(rating));

        Rating updated = ratingService.update(rating);

        assertEquals(rating, updated);
        verify(ratingRepository).findById(id);
        verify(ratingRepository).save(rating);
        verify(observer).updateRating(eq(rating.getItemId()), eq(4.0));
    }

    @Test
    void testUpdateNotFound() {
        UUID id = rating.getRatingId();
        when(ratingRepository.findById(id)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> ratingService.update(rating));
        assertEquals("Rating not found", ex.getMessage());
        verify(ratingRepository).findById(id);
        verify(ratingRepository, never()).save(any());
    }

    @Test
    void testDeleteById() {
        UUID id = rating.getRatingId();
        when(ratingRepository.findById(id)).thenReturn(Optional.of(rating));
        when(ratingRepository.findAll()).thenReturn(List.of()); // setelah delete, rating kosong

        ratingService.deleteById(id);

        verify(ratingRepository).delete(id);
        verify(observer).updateRating(eq(rating.getItemId()), eq(0.0));
    }

    @Test
    void testDeleteNotFound() {
        UUID id = rating.getRatingId();
        when(ratingRepository.findById(id)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class, () -> ratingService.deleteById(id));
        assertEquals("Rating not found", ex.getMessage());

        verify(ratingRepository).findById(id);
        verify(ratingRepository, never()).delete(any());
        verify(observer, never()).updateRating(any(), anyDouble());
    }

    @Test
    void testFindByItemId() {
        UUID itemId = rating.getItemId();
        List<Rating> ratings = List.of(rating);

        when(ratingRepository.findByItemId(itemId)).thenReturn(ratings);

        List<Rating> found = ratingService.findByItemId(itemId);

        assertEquals(1, found.size());
        assertEquals(rating, found.get(0));
        verify(ratingRepository).findByItemId(itemId);
    }
}
