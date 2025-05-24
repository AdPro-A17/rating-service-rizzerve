package id.ac.ui.cs.advprog.rating_service.observer;

import java.util.UUID;

public interface RatingObserver {
    void updateRating(UUID itemId, double averageRating);
}

