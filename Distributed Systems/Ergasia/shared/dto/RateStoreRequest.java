package shared.dto;

import java.io.Serializable;

public class RateStoreRequest implements Serializable {
    private static final long serialVersionUID = 13L; // New unique ID

    private String storeName;
    private int rating; // 1-5

    public RateStoreRequest(String storeName, int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        this.storeName = storeName;
        this.rating = rating;
    }

    public String getStoreName() { return storeName; }
    public int getRating() { return rating; }

    @Override
    public String toString() {
        return "RateStoreRequest{" +
               "store='" + storeName + '\'' +
               ", rating=" + rating +
               '}';
    }
}