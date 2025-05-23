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
        UUID itemId = UUID.randomUUID();
        Rating rating = new Rating();
        rating.setRatingId(UUID.randomUUID());
        rating.setUserId(UUID.randomUUID());
        rating.setItemId(itemId);
        rating.setValue(4);

        RatingObserver observer = mock(RatingObserver.class);
        ratingService.addObserver(observer);

        when(ratingRepository.save(rating)).thenReturn(rating);
        when(ratingRepository.findByItemId(itemId)).thenReturn(List.of(rating)); // Mock ini penting

        Rating savedRating = ratingService.save(rating);

        assertEquals(rating, savedRating);
        verify(observer).updateRating(eq(itemId), eq(4.0d));
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
        when(ratingRepository.findByItemId(rating.getItemId())).thenReturn(List.of(rating)); // Tambahan penting

        Rating updated = ratingService.update(rating);

        assertEquals(rating, updated);
        verify(observer).updateRating(eq(rating.getItemId()), eq(4.0d));
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

        verify(ratingRepository).deleteById(id);
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

    @Test
    void testGetAverageRatingByItemIdWithRatings() {
        UUID itemId = UUID.randomUUID();

        Rating r1 = new Rating();
        r1.setItemId(itemId);
        r1.setValue(4);

        Rating r2 = new Rating();
        r2.setItemId(itemId);
        r2.setValue(2);

        when(ratingRepository.findByItemId(itemId)).thenReturn(List.of(r1, r2));

        double average = ratingService.getAverageRatingByItemId(itemId);

        assertEquals(3.0, average, 0.01, "Average rating should be correct");
        verify(ratingRepository).findByItemId(itemId);
    }

    @Test
    void testGetAverageRatingByItemIdWithNoRatings() {
        UUID itemId = UUID.randomUUID();

        when(ratingRepository.findByItemId(itemId)).thenReturn(List.of());

        double average = ratingService.getAverageRatingByItemId(itemId);

        assertEquals(0.0, average, "Average rating should be 0.0 when no ratings");
        verify(ratingRepository).findByItemId(itemId);
    }

    @Test
    void testNotifyObserversAsync() {
        UUID itemId = UUID.randomUUID();
        Rating r1 = new Rating();
        r1.setItemId(itemId);
        r1.setValue(3);

        Rating r2 = new Rating();
        r2.setItemId(itemId);
        r2.setValue(5);

        List<Rating> ratings = List.of(r1, r2);

        when(ratingRepository.findByItemId(itemId)).thenReturn(ratings);

        RatingObserver asyncObserver = mock(RatingObserver.class);
        ratingService.addObserver(asyncObserver);

        ratingService.notifyObservers(itemId); // async call

        // Verifikasi bahwa observer dipanggil dalam 1 detik
        verify(asyncObserver, timeout(1000).times(1)).updateRating(itemId, 4.0);
    }

}
