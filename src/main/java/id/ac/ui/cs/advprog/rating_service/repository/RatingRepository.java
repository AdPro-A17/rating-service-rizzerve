package id.ac.ui.cs.advprog.rating_service.repository;

import id.ac.ui.cs.advprog.rating_service.model.Rating;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class RatingRepository {
    private final Map<UUID, Rating> ratingStorage = new HashMap<>();

    public Rating save(Rating rating) {
        if (rating == null || rating.getRatingId() == null) {
            throw new IllegalArgumentException("Rating or Rating ID cannot be null");
        }
        ratingStorage.put(rating.getRatingId(), rating);
        return rating;
    }

    public Optional<Rating> findById(UUID id) {
        return Optional.ofNullable(ratingStorage.get(id));
    }

    public List<Rating> findAll() {
        return new ArrayList<>(ratingStorage.values());
    }

    public void delete(UUID id) {
        ratingStorage.remove(id);
    }

    public List<Rating> findByItemId(UUID itemId) {
        return ratingStorage.values().stream()
                .filter(r -> r.getItemId().equals(itemId))
                .toList();
    }

}
