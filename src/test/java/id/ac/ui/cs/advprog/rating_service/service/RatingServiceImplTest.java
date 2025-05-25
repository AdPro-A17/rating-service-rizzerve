package id.ac.ui.cs.advprog.rating_service.service;

import id.ac.ui.cs.advprog.rating_service.exception.RatingNotFoundException;
import id.ac.ui.cs.advprog.rating_service.model.Rating;
import id.ac.ui.cs.advprog.rating_service.observer.RatingObserver;
import id.ac.ui.cs.advprog.rating_service.repository.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import id.ac.ui.cs.advprog.rating_service.client.MenuServiceClient;
import id.ac.ui.cs.advprog.rating_service.dto.MenuItemDTO;
import org.springframework.http.ResponseEntity;

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

    @Mock
    private MenuServiceClient menuServiceClient;

    private Rating rating;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rating = new Rating();
        rating.setRatingId(UUID.randomUUID());
        rating.setMejaId(UUID.randomUUID());
        rating.setItemId(UUID.randomUUID());
        rating.setValue(4);

        ratingService.addObserver(observer); // daftarkan observer ke service
    }

    @Test
    void testSave() {
        UUID itemId = UUID.randomUUID();
        UUID mejaId = UUID.randomUUID();
        Rating rating = new Rating();
        rating.setRatingId(UUID.randomUUID());
        rating.setMejaId(mejaId);
        rating.setItemId(itemId);
        rating.setValue(4);
        rating.setCanUpdate(true);

        RatingObserver observer = mock(RatingObserver.class);
        ratingService.addObserver(observer);

        // ✅ Mock respons dari menuServiceClient agar itemId valid
        MenuServiceClient.MenuItemResponse mockItem = new MenuServiceClient.MenuItemResponse();
        mockItem.setId(itemId);
        mockItem.setName("Nasi Goreng");

        when(menuServiceClient.getMenuItemById(itemId)).thenReturn(mockItem);

        when(ratingRepository.save(rating)).thenReturn(rating);
        when(ratingRepository.findByItemId(itemId)).thenReturn(List.of(rating));

        Rating savedRating = ratingService.save(rating);

        assertEquals(rating, savedRating);
        verify(observer).updateRating(eq(itemId), eq(4.0d));
        verify(menuServiceClient).getMenuItemById(itemId);
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

        Exception ex = assertThrows(RatingNotFoundException.class, () -> ratingService.update(rating));
        assertTrue(ex.getMessage().contains("Rating dengan ID"));

        verify(ratingRepository).findById(id);
        verify(ratingRepository, never()).save(any());
    }

    @Test
    void testDeleteNotFound() {
        UUID id = rating.getRatingId();
        when(ratingRepository.findById(id)).thenReturn(Optional.empty());

        Exception ex = assertThrows(RatingNotFoundException.class, () -> ratingService.deleteById(id));
        assertTrue(ex.getMessage().contains("Rating dengan ID"));

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

    @Test
    void testFindByItemIdAndMejaId_ReturnsRatings() {
        // Prepare sample data
        UUID itemId = UUID.randomUUID();
        UUID mejaId = UUID.randomUUID();
        Rating rating1 = new Rating();
        rating1.setRatingId(UUID.randomUUID());
        rating1.setItemId(itemId);
        rating1.setMejaId(mejaId);
        rating1.setValue(4);
        rating1.setCanUpdate(true);

        List<Rating> expectedRatings = List.of(rating1);

        // Mock repository behavior
        when(ratingRepository.findByItemIdAndMejaId(itemId, mejaId)).thenReturn(expectedRatings);

        // Call service method
        List<Rating> actualRatings = ratingService.findByItemIdAndMejaId(itemId, mejaId);

        // Verify
        assertNotNull(actualRatings);
        assertEquals(1, actualRatings.size());
        assertEquals(expectedRatings, actualRatings);

        verify(ratingRepository, times(1)).findByItemIdAndMejaId(itemId, mejaId);
    }

    @Test
    void testFindByItemIdAndMejaId_ReturnsEmptyListWhenNoRatings() {
        // Mock repository to return empty list
        UUID itemId = UUID.randomUUID();
        UUID mejaId = UUID.randomUUID();
        when(ratingRepository.findByItemIdAndMejaId(itemId, mejaId)).thenReturn(Collections.emptyList());

        List<Rating> actualRatings = ratingService.findByItemIdAndMejaId(itemId, mejaId);

        assertNotNull(actualRatings);
        assertTrue(actualRatings.isEmpty());

        verify(ratingRepository, times(1)).findByItemIdAndMejaId(itemId, mejaId);
    }

    @Test
    void testSaveThrowsExceptionWhenItemIdIsInvalid() {
        UUID invalidItemId = UUID.randomUUID();
        Rating rating = new Rating();
        rating.setRatingId(UUID.randomUUID());
        rating.setItemId(invalidItemId);
        rating.setMejaId(UUID.randomUUID());
        rating.setValue(3);
        rating.setCanUpdate(true);

        when(menuServiceClient.getMenuItemById(invalidItemId)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> ratingService.save(rating));

        assertEquals("Invalid itemId: item does not exist in MenuService", exception.getMessage());
        verify(menuServiceClient).getMenuItemById(invalidItemId);
        verify(ratingRepository, never()).save(any());
    }

    @Test
    void testCheckoutSetsCanUpdateFalse() {
        UUID mejaId = UUID.randomUUID();
        UUID itemId = UUID.randomUUID();

        Rating rating1 = new Rating();
        rating1.setRatingId(UUID.randomUUID());
        rating1.setItemId(itemId);
        rating1.setMejaId(mejaId);
        rating1.setValue(5);
        rating1.setCanUpdate(true);

        Rating rating2 = new Rating();
        rating2.setRatingId(UUID.randomUUID());
        rating2.setItemId(itemId);
        rating2.setMejaId(mejaId);
        rating2.setValue(3);
        rating2.setCanUpdate(true);

        List<Rating> ratings = List.of(rating1, rating2);

        when(ratingRepository.findByMejaId(mejaId)).thenReturn(ratings);
        when(ratingRepository.save(any(Rating.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ratingService.checkoutMeja(mejaId);

        assertFalse(rating1.isCanUpdate());
        assertFalse(rating2.isCanUpdate());
        verify(ratingRepository, times(2)).save(any(Rating.class));
    }

    @Test
    void testFindByMejaId_ReturnsRatings() {
        UUID mejaId = UUID.randomUUID();
        Rating rating1 = new Rating();
        rating1.setRatingId(UUID.randomUUID());
        rating1.setMejaId(mejaId);
        rating1.setItemId(UUID.randomUUID());
        rating1.setValue(5);

        List<Rating> expectedRatings = List.of(rating1);

        when(ratingRepository.findByMejaId(mejaId)).thenReturn(expectedRatings);

        List<Rating> actualRatings = ratingService.findByMejaId(mejaId);

        assertNotNull(actualRatings);
        assertEquals(1, actualRatings.size());
        assertEquals(expectedRatings, actualRatings);

        verify(ratingRepository, times(1)).findByMejaId(mejaId);
    }

}
