package id.ac.ui.cs.advprog.rating_service.model;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "ratings")
public class Rating {

    // Getter dan Setter
    @Id
    @Column(name = "rating_id", nullable = false, updatable = false)
    private UUID ratingId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "item_id", nullable = false)
    private UUID itemId;

    @Column(name = "rating_value", nullable = false)
    private int value;

    public void setRatingId(UUID ratingId) {
        this.ratingId = ratingId;
    }

    public void setUserId(UUID userId) {
        if (userId == null) throw new IllegalArgumentException("User ID cannot be null");
        this.userId = userId;
    }

    public void setItemId(UUID itemId) {
        if (itemId == null) throw new IllegalArgumentException("Item ID cannot be null");
        this.itemId = itemId;
    }

    public void setValue(int value) {
        if (value < 1 || value > 5) {
            throw new IllegalArgumentException("Rating value must be between 1 and 5");
        }
        this.value = value;
    }
}
