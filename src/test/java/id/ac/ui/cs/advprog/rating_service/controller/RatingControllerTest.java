package id.ac.ui.cs.advprog.rating_service.controller;

import id.ac.ui.cs.advprog.rating_service.model.Rating;
import id.ac.ui.cs.advprog.rating_service.service.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping
    public ResponseEntity<Rating> createRating(@RequestBody Rating rating) {
        Rating savedRating = ratingService.save(rating);
        return ResponseEntity.ok(savedRating);
    }

    @GetMapping
    public ResponseEntity<List<Rating>> getAllRatings() {
        return ResponseEntity.ok(ratingService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Rating> getRatingById(@PathVariable UUID id) {
        Optional<Rating> rating = ratingService.findById(id);
        return rating.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Rating> updateRating(@PathVariable UUID id, @RequestBody Rating rating) {
        if (!id.equals(rating.getRatingId())) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Rating updated = ratingService.update(rating);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable UUID id) {
        try {
            ratingService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<Rating>> getRatingsByItemId(@PathVariable UUID itemId) {
        List<Rating> ratings = ratingService.findByItemId(itemId);
        return ResponseEntity.ok(ratings);
    }
}
