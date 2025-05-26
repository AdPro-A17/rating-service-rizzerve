package id.ac.ui.cs.advprog.rating_service.service;

import id.ac.ui.cs.advprog.rating_service.model.Rating;
import id.ac.ui.cs.advprog.rating_service.observer.RatingObserver;
import id.ac.ui.cs.advprog.rating_service.repository.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class RatingSubjectTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private RatingObserver mockObserver;

    @InjectMocks
    private RatingServiceImpl ratingService;

    private UUID itemId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        itemId = UUID.randomUUID();
    }

    @Test
    void testAddObserverAddsSuccessfully() {
        ratingService.addObserver(mockObserver);
        Rating rating = new Rating();
        rating.setItemId(itemId);
        rating.setValue(5);

        when(ratingRepository.findByItemId(itemId)).thenReturn(List.of(rating));

        ratingService.notifyObservers(itemId);

        verify(mockObserver).updateRating(itemId, 5.0);
    }

    @Test
    void testRemoveObserverStopsNotification() {
        ratingService.addObserver(mockObserver);
        ratingService.removeObserver(mockObserver);

        Rating rating = new Rating();
        rating.setItemId(itemId);
        rating.setValue(4);
        when(ratingRepository.findByItemId(itemId)).thenReturn(List.of(rating));

        ratingService.notifyObservers(itemId);

        verify(mockObserver, never()).updateRating(any(), anyDouble());
    }

    @Test
    void testNotifyObserversWithMultipleObservers() {
        RatingObserver anotherObserver = mock(RatingObserver.class);
        Rating rating = new Rating();
        rating.setItemId(itemId);
        rating.setValue(3);

        when(ratingRepository.findByItemId(itemId)).thenReturn(List.of(rating));

        ratingService.addObserver(mockObserver);
        ratingService.addObserver(anotherObserver);

        ratingService.notifyObservers(itemId);

        verify(mockObserver).updateRating(itemId, 3.0);
        verify(anotherObserver).updateRating(itemId, 3.0);
    }

    @Test
    void testNotifyObserversWithNoRatings() {
        when(ratingRepository.findByItemId(itemId)).thenReturn(List.of());

        ratingService.addObserver(mockObserver);
        ratingService.notifyObservers(itemId);

        verify(mockObserver).updateRating(itemId, 0.0);
    }
}