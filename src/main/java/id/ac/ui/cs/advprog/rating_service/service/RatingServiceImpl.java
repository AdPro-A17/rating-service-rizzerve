package id.ac.ui.cs.advprog.rating_service.service;

import id.ac.ui.cs.advprog.rating_service.model.Rating;
import id.ac.ui.cs.advprog.rating_service.service.RatingObserver;
import id.ac.ui.cs.advprog.rating_service.service.RatingSubject;
import id.ac.ui.cs.advprog.rating_service.repository.RatingRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final List<RatingObserver> observers = new ArrayList<>();

    public RatingServiceImpl(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public Rating addRating(Rating rating) {
        Rating savedRating = ratingRepository.save(rating);
        notifyObservers(rating.getItemId());
        return savedRating;
    }

    @Override
    public Optional<Rating> getRatingById(String id) {
        return ratingRepository.findById(id);
    }

    @Override
    public void deleteRatingById(String id) {
        Optional<Rating> rating = ratingRepository.findById(id);
        rating.ifPresent(r -> {
            ratingRepository.deleteById(id);
            notifyObservers(r.getItemId());
        });
    }

    @Override
    public List<Rating> getRatingsByItemId(String itemId) {
        return ratingRepository.findByItemId(itemId);
    }

    // Observer logic
    public void addObserver(RatingObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(RatingObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(String itemId) {
        double avg = calculateAverageRating(itemId);
        for (RatingObserver observer : observers) {
            observer.updateRating(itemId, avg);
        }
    }

    private double calculateAverageRating(String itemId) {
        List<Rating> ratings = ratingRepository.findByItemId(itemId);
        return ratings.stream()
                .mapToInt(Rating::getValue)
                .average()
                .orElse(0.0);
    }
}

