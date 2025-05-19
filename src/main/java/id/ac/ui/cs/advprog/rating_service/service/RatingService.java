package id.ac.ui.cs.advprog.rating_service.service;

import id.ac.ui.cs.advprog.rating_service.model.Rating;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RatingService {
    Rating save(Rating rating);
    List<Rating> findAll();
    Optional<Rating> findById(UUID ratingId);
    Rating update(Rating rating);
    void deleteById(UUID ratingId);
    List<Rating> findByItemId(UUID itemId);

}
