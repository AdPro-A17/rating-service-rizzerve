package id.ac.ui.cs.advprog.rating_service.controller;

import id.ac.ui.cs.advprog.rating_service.exception.RatingNotFoundException;
import id.ac.ui.cs.advprog.rating_service.model.Rating;
import id.ac.ui.cs.advprog.rating_service.service.RatingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping
    public ResponseEntity<Rating> createRating(@RequestBody Rating rating) {
        if (rating == null) {
            throw new IllegalArgumentException("Rating must not be null");
        }
        Rating savedRating = ratingService.save(rating); // Sesuaikan dengan service
        return ResponseEntity.ok(savedRating);
    }

    @GetMapping
    public ResponseEntity<List<Rating>> getAllRatings() {
        List<Rating> ratings = ratingService.findAll(); // Sesuaikan dengan service
        return ResponseEntity.ok(ratings);
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
        Rating updated = ratingService.update(rating);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Rating ID must not be null");
        }

        ratingService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Tangani exception RatingNotFoundException agar respon 404
    @ExceptionHandler(RatingNotFoundException.class)
    public ResponseEntity<String> handleRatingNotFoundException(RatingNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<Rating>> getRatingsByItemId(@PathVariable UUID itemId) {
        List<Rating> ratings = ratingService.findByItemId(itemId);
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/item/{itemId}/average")
    public ResponseEntity<Double> getAverageRatingByItemId(@PathVariable UUID itemId) {
        double averageRating = ratingService.getAverageRatingByItemId(itemId);
        return ResponseEntity.ok(averageRating);
    }

    @PutMapping("/meja/{mejaId}/disable")
    public ResponseEntity<Void> disableRatingsForMeja(@PathVariable UUID mejaId) {
        ratingService.disableUpdatesForMeja(mejaId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/meja/{mejaId}")
    public ResponseEntity<List<Rating>> getRatingsByMejaId(@PathVariable UUID mejaId) {
        List<Rating> ratings = ratingService.findByMejaId(mejaId);
        return ResponseEntity.ok(ratings);
    }

    @PutMapping("/meja/{mejaId}/checkout")
    public ResponseEntity<Map<String, UUID>> checkoutMeja(@PathVariable UUID mejaId) {
        Map<String, UUID> response = new HashMap<>();
        return ResponseEntity.ok(response);
    }


}
