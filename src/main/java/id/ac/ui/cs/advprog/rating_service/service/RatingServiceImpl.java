package id.ac.ui.cs.advprog.rating_service.service;

import id.ac.ui.cs.advprog.rating_service.model.Rating;
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

    public void addObserver(RatingObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(RatingObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(UUID itemId) {
        List<Rating> ratings = findByItemId(itemId);
        double avg = ratings.stream().mapToInt(Rating::getValue).average().orElse(0.0);
        for (RatingObserver observer : observers) {
            observer.updateRating(itemId, avg);
        }
    }

    @Override
    public Rating save(Rating rating) {
        Rating savedRating = ratingRepository.save(rating);
        notifyObservers(rating.getItemId());
        return savedRating;
    }

    @Override
    public List<Rating> findAll() {
        return ratingRepository.findAll();
    }

    @Override
    public Optional<Rating> findById(UUID id) {
        return ratingRepository.findById(id);
    }

    @Override
    public Rating update(Rating rating) {
        UUID ratingId = rating.getRatingId();
        Optional<Rating> existing = ratingRepository.findById(ratingId);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Rating not found");
        }
        Rating updatedRating = ratingRepository.save(rating);
        notifyObservers(rating.getItemId());
        return updatedRating;
    }

    @Override
    public void deleteById(UUID id) {
        Optional<Rating> rating = ratingRepository.findById(id);
        if (rating.isPresent()) {
            ratingRepository.delete(id);
            notifyObservers(rating.get().getItemId());
        } else {
            throw new IllegalArgumentException("Rating not found");
        }
    }
    
}
