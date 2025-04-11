package id.ac.ui.cs.advprog.rating_service.service;

import java.util.List;

public interface RatingSubject {
    void addObserver(RatingObserver observer);
    void removeObserver(RatingObserver observer);
    void notifyObservers(String itemId, double newAverageRating);
}

