package id.ac.ui.cs.advprog.rating_service.service;

import id.ac.ui.cs.advprog.rating_service.model.Rating;

import java.util.List;
import java.util.Optional;

public interface RatingService {
    Rating addRating(Rating rating);
    Optional<Rating> getRatingById(String id);
    void deleteRatingById(String id);
    List<Rating> getRatingsByItemId(String itemId);
}

