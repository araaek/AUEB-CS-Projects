package shared.dto;

import java.io.Serializable;
import java.util.List;

public class FilterCriteria implements Serializable {
    private static final long serialVersionUID = 3L;

    private double clientLatitude;
    private double clientLongitude;
    private double radiusKm = 5.0; // Default radius
    private List<String> foodCategories; // Null or empty means no filter
    private int minStars = 0; // 0 means no filter
    private List<String> priceCategories; // e.g., ["$", "$$"] null or empty means no filter

    // Constructor, getters, setters...
    public FilterCriteria(double clientLatitude, double clientLongitude) {
        this.clientLatitude = clientLatitude;
        this.clientLongitude = clientLongitude;
    }

    // Getters
    public double getClientLatitude() { return clientLatitude; }
    public double getClientLongitude() { return clientLongitude; }
    public double getRadiusKm() { return radiusKm; }
    public List<String> getFoodCategories() { return foodCategories; }
    public int getMinStars() { return minStars; }
    public List<String> getPriceCategories() { return priceCategories; }

    // Setters for filters
    public void setRadiusKm(double radiusKm) { this.radiusKm = radiusKm; }
    public void setFoodCategories(List<String> foodCategories) { this.foodCategories = foodCategories; }
    public void setMinStars(int minStars) { this.minStars = minStars; }
    public void setPriceCategories(List<String> priceCategories) { this.priceCategories = priceCategories; }

    @Override
    public String toString() {
        return "FilterCriteria{" +
               "lat=" + clientLatitude +
               ", lon=" + clientLongitude +
               ", radius=" + radiusKm +
               ", foodCats=" + foodCategories +
               ", minStars=" + minStars +
               ", priceCats=" + priceCategories +
               '}';
    }
}