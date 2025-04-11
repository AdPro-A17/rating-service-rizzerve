package id.ac.ui.cs.advprog.rating_service.service;

public interface RatingObserver {
    void updateAverageRating(String itemId, double newAverageRating);
}
