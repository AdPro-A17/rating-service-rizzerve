package id.ac.ui.cs.advprog.rating_service.service;

import java.util.UUID;

public interface RatingObserver {
    void updateRating(UUID itemId, double averageRating);
}

