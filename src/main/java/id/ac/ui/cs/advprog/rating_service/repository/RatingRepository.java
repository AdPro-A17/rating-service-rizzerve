package id.ac.ui.cs.advprog.rating_service.repository;

import id.ac.ui.cs.advprog.rating_service.model.Rating;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RatingRepository extends JpaRepository<Rating, UUID> {
    List<Rating> findByItemId(UUID itemId);
}
