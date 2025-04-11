package id.ac.ui.cs.advprog.rating_service.repository;

import id.ac.ui.cs.advprog.rating_service.model.Rating;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class RatingRepository {
    private final Map<String, Rating> ratingStorage = new HashMap<>();

    public Rating create(Rating rating) {
        String id = UUID.randomUUID().toString();
        rating.setId(id);
        ratingStorage.put(id, rating);
        return rating;
    }

    public List<Rating> findAll() {
        return new ArrayList<>(ratingStorage.values());
    }

    public Rating update(Rating rating) {
        ratingStorage.put(rating.getId(), rating);
        return rating;
    }

    public void delete(String id) {
        ratingStorage.remove(id);
    }
}
