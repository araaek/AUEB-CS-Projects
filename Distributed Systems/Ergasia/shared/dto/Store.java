// File: shared/dto/Store.java
package shared.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Store implements Serializable {
   
    private static final long serialVersionUID = 2L;

    private String storeName;
    private double latitude;
    private double longitude;
    private String foodCategory; // e.g., "pizzeria", "souvlaki"
    private int stars; // Represents the current average rating (likely rounded as per JSON example)
    private int noOfVotes;
    private String storeLogoPath; // Path as given in JSON
    private List<Product> products;
    private String priceCategory; // "$", "$$", "$$$" - Calculated by Worker/Master
    private double totalRevenue = 0.0; // Total revenue, updated on the Worker

    /**
     * Constructor to initialize a Store object based on initial data.
     * @param storeName Name of the store.
     * @param latitude Latitude coordinate.
     * @param longitude Longitude coordinate.
     * @param foodCategory Type of food (e.g., pizzeria).
     * @param stars Initial average star rating (1-5).
     * @param noOfVotes Initial number of votes.
     * @param storeLogoPath Filesystem path to the store's logo.
     */
    public Store(String storeName, double latitude, double longitude, String foodCategory,
                 int stars, int noOfVotes, String storeLogoPath) {
        this.storeName = storeName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.foodCategory = foodCategory;
        this.stars = stars; // Initial average rating
        this.noOfVotes = noOfVotes; // Initial vote count
        this.storeLogoPath = storeLogoPath;
        this.products = new ArrayList<>(); // Initialize product list
        // Price category is calculated later
    }

    /**
     * Adds a product to the store's product list.
     * @param product The Product object to add.
     */
    public void addProduct(Product product) {
        if (this.products == null) {
            this.products = new ArrayList<>();
        }
        // Optional: Check if product with the same name already exists before adding
        this.products.add(product);
        // Consider recalculating price category here if desired upon product changes
    }

  
    public void calculateAndSetPriceCategory() {
        if (products == null || products.isEmpty()) {
            this.priceCategory = "$"; // Default if no products
            return;
        }
        double sum = 0;
        int count = 0;
        for (Product p : products) {
            if (p != null) { // Basic null check
                sum += p.getPrice();
                count++;
            }
        }

        if (count == 0) {
             this.priceCategory = "$"; // Default if only null products somehow
             return;
        }

        double averagePrice = sum / count;

        if (averagePrice <= 5.0) {
            this.priceCategory = "$";
        } else if (averagePrice <= 15.0) {
            this.priceCategory = "$$";
        } else {
            this.priceCategory = "$$$";
        }
    }

    /**
     * Adds a specified amount to the store's total revenue.
     * This method is synchronized to handle potential concurrent revenue updates (e.g., from purchases).
     * @param amount The revenue amount to add.
     */
    public synchronized void addRevenue(double amount) {
        this.totalRevenue += amount;
    }

    /**
     * Finds a product within the store's product list by its name.
     * Synchronization might be needed depending on how the product list is modified elsewhere,
     * but reads are generally safe if additions/removals are synchronized.
     * A synchronized block around the loop could be added for absolute safety if modifications are frequent and unsynchronized.
     * @param productName The name of the product to find.
     * @return The Product object if found, otherwise null.
     */
    public Product findProduct(String productName) {
         if (this.products == null || productName == null) {
             return null;
         }
         // Basic iteration should be safe for reads if list modifications are handled correctly
         for (Product p : products) {
            if (p != null && productName.equals(p.getProductName())) {
                return p;
            }
        }
        return null;
    }

    // --- Getters ---
    public String getStoreName() { return storeName; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getFoodCategory() { return foodCategory; }
    public int getStars() { return stars; }
    public int getNoOfVotes() { return noOfVotes; }
    public String getStoreLogoPath() { return storeLogoPath; }
    public List<Product> getProducts() { return products; } // Returns a modifiable list - consider returning a copy if needed
    public String getPriceCategory() { return priceCategory; } // Ensure it's calculated before accessing
    public double getTotalRevenue() { return totalRevenue; }


    // --- Setters ---

    /**
     * Sets the entire list of products for the store.
     * Use with caution, prefer addProduct/removeProduct for modifications.
     * @param products The new list of products.
     */
    public void setProducts(List<Product> products) {
        this.products = products;
        // Recalculate price category whenever the product list is replaced
        calculateAndSetPriceCategory();
    }

    /**
     * Sets the store's average star rating. Used by the Worker when updating ratings.
     * @param stars The new average star rating (typically 1-5).
     */
    public void setStars(int stars) {
        // Add validation if desired (e.g., ensure 1-5 range)
        this.stars = stars;
    }

    /**
     * Sets the total number of votes (ratings) the store has received. Used by the Worker.
     * @param noOfVotes The new total number of votes.
     */
    public void setNoOfVotes(int noOfVotes) {
         // Add validation if desired (e.g., ensure non-negative)
        this.noOfVotes = noOfVotes;
    }


    // --- Standard Object Methods ---

    @Override
    public String toString() {
        // Include more details for better debugging if needed
        return "Store{" +
               "name='" + storeName + '\'' +
               ", lat=" + latitude +
               ", lon=" + longitude +
               ", category='" + foodCategory + '\'' +
               ", stars=" + stars +
               ", votes=" + noOfVotes +
               ", priceCat='" + (priceCategory != null ? priceCategory : "N/A") + '\'' +
               ", revenue=" + String.format("%.2f", totalRevenue) +
               ", products#=" + (products != null ? products.size() : 0) +
               '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Store store = (Store) o;
        // Assume store names are unique identifiers for equality
        return Objects.equals(storeName, store.storeName);
    }

    @Override
    public int hashCode() {
        // Hash based on the assumed unique identifier
        return Objects.hash(storeName);
    }
}