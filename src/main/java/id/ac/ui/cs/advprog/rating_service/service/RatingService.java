package id.ac.ui.cs.advprog.rating_service.service;

import id.ac.ui.cs.advprog.rating_service.model.Rating;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RatingService {
    Rating save(Rating rating); // Sesuaikan dengan repository
    List<Rating> findAll(); // Sesuaikan dengan repository
    Optional<Rating> findById(UUID ratingId); // Sesuaikan dengan repository
    Rating update(Rating rating); // Sesuaikan dengan repository (kita bisa menambahkan method ini jika perlu)
    void deleteById(UUID ratingId); // Sesuaikan dengan repository
}
