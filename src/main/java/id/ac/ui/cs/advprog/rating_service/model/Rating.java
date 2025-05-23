package id.ac.ui.cs.advprog.rating_service.model;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "ratings")
public class Rating {

    // Getter dan Setter
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "rating_id", updatable = false, nullable = false)
    private UUID ratingId;

    @Column(nullable = false)
    private UUID mejaId;

    @Column(name = "item_id", nullable = false)
    private UUID itemId;

    @Column(name = "rating_value", nullable = false)
    private int value;

    @Column(nullable = false)
    private boolean canUpdate = true;

    public void setRatingId(UUID ratingId) {
        this.ratingId = ratingId;
    }

    public void setMejaId(UUID mejaId) {
        if (mejaId == null) {
            throw new IllegalArgumentException("Meja ID cannot be null");
        }
        this.mejaId = mejaId;
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
