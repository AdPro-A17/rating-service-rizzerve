package id.ac.ui.cs.advprog.rating_service.service;

import id.ac.ui.cs.advprog.rating_service.model.Rating;
import id.ac.ui.cs.advprog.rating_service.observer.RatingObserver;
import id.ac.ui.cs.advprog.rating_service.repository.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import io.micrometer.core.instrument.MeterRegistry;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class RatingObserverTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private RatingObserver observer1;
    @Mock
    private MeterRegistry meterRegistry;
    @Mock
    private RatingObserver observer2;

    @InjectMocks
    private RatingServiceImpl ratingService;

    private UUID itemId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        itemId = UUID.randomUUID();
    }

    @Test
    void testAddAndNotifyObservers() {
        Rating rating1 = new Rating();
        rating1.setItemId(itemId);
        rating1.setValue(4);

        Rating rating2 = new Rating();
        rating2.setItemId(itemId);
        rating2.setValue(2);

        when(ratingRepository.findByItemId(itemId)).thenReturn(List.of(rating1, rating2));

        ratingService.addObserver(observer1);
        ratingService.addObserver(observer2);

        ratingService.notifyObservers(itemId);

        verify(observer1).updateRating(itemId, 3.0); // (4+2)/2 = 3.0
        verify(observer2).updateRating(itemId, 3.0);
    }

    @Test
    void testRemoveObserver() {
        Rating rating = new Rating();
        rating.setItemId(itemId);
        rating.setValue(5);

        when(ratingRepository.findByItemId(itemId)).thenReturn(List.of(rating));

        ratingService.addObserver(observer1);
        ratingService.addObserver(observer2);
        ratingService.removeObserver(observer2); // hanya observer1 yang aktif

        ratingService.notifyObservers(itemId);

        verify(observer1).updateRating(itemId, 5.0);
        verify(observer2, never()).updateRating(any(), anyDouble());
    }

    @Test
    void testNotifyObserversWithNoRatings() {
        when(ratingRepository.findByItemId(itemId)).thenReturn(List.of());

        ratingService.addObserver(observer1);
        ratingService.notifyObservers(itemId);

        verify(observer1).updateRating(itemId, 0.0); // Tidak ada rating, rata-rata = 0.0
    }
}
