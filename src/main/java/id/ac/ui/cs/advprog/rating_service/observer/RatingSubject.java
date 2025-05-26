package id.ac.ui.cs.advprog.rating_service.observer;

import java.util.UUID;

public interface RatingSubject {
    void addObserver(RatingObserver observer);
    void removeObserver(RatingObserver observer);
    void notifyObservers(UUID itemId);
}

