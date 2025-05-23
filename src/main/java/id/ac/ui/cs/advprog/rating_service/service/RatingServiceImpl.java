package id.ac.ui.cs.advprog.rating_service.service;

import id.ac.ui.cs.advprog.rating_service.model.Rating;
import id.ac.ui.cs.advprog.rating_service.repository.RatingRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RatingServiceImpl implements RatingService, RatingSubject {

    private final RatingRepository ratingRepository;
    private final List<RatingObserver> observers = new ArrayList<>();

    public RatingServiceImpl(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public void addObserver(RatingObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(RatingObserver observer) {
        observers.remove(observer);
    }

    @Override
    @Async("taskExecutor") // gunakan executor dari AsyncConfig
    public void notifyObservers(UUID itemId) {
        List<Rating> ratings = findByItemId(itemId);
        double avg = ratings.stream()
                .mapToInt(Rating::getValue)
                .average()
                .orElse(0.0); // Return 0.0 if no ratings are found
        for (RatingObserver observer : observers) {
            observer.updateRating(itemId, avg);
        }
    }

    @Override
    public Rating save(Rating rating) {
        List<Rating> existing = ratingRepository.findByItemIdAndMejaId(rating.getItemId(), rating.getMejaId());

        Rating saved;
        if (!existing.isEmpty()) {
            Rating current = existing.get(0);
            if (current.isCanUpdate()) {
                current.setValue(rating.getValue());
                saved = ratingRepository.save(current);
            } else {
                rating.setCanUpdate(true);
                saved = ratingRepository.save(rating);
            }
        } else {
            rating.setCanUpdate(true);
            saved = ratingRepository.save(rating);
        }

        notifyObservers(saved.getItemId());
        return saved;
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
            ratingRepository.deleteById(id);
            notifyObservers(rating.get().getItemId());
        } else {
            throw new IllegalArgumentException("Rating not found");
        }
    }

    @Override
    public List<Rating> findByItemId(UUID itemId) {
        return ratingRepository.findByItemId(itemId);
    }

    @Override
    public double getAverageRatingByItemId(UUID itemId) {
        List<Rating> ratings = findByItemId(itemId);
        return ratings.stream()
                .mapToInt(Rating::getValue)
                .average()
                .orElse(0.0);
    }

    @Override
    public List<Rating> findByItemIdAndMejaId(UUID itemId, UUID mejaId) {
        return ratingRepository.findByItemIdAndMejaId(itemId, mejaId);
    }

    @Override
    public void disableUpdatesForMeja(UUID mejaId) {
        List<Rating> ratings = ratingRepository.findByMejaId(mejaId);
        for (Rating rating : ratings) {
            if (rating.isCanUpdate()) {
                rating.setCanUpdate(false);
                ratingRepository.save(rating);
            }
        }
    }


}
