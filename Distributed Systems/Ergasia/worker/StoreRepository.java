// File: worker/StoreRepository.java
package worker;

import shared.dto.*; // Import DTOs

import worker.DistanceCalculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level; // Import Level if needed for logging
import java.util.logging.Logger;

public class StoreRepository {
    private static final Logger logger = Logger.getLogger(StoreRepository.class.getName());
    private final Map<String, Store> stores = new HashMap<>();

    // --- Store Management Methods (addOrUpdateStore, getStore) - παραμένουν ίδια ---
    public synchronized void addOrUpdateStore(Store store) {
        if (store.getPriceCategory() == null || store.getPriceCategory().isEmpty()) {
            store.calculateAndSetPriceCategory();
            logger.fine("Calculated price category for " + store.getStoreName() + ": " + store.getPriceCategory());
        }
        stores.put(store.getStoreName(), store);
        logger.info("Added/Updated store: " + store.getStoreName() + ". Total stores: " + stores.size());
    }
    public synchronized Store getStore(String storeName) {
        return stores.get(storeName);
    }

    // --- Search (Map Phase) Method (findStoresMatchingCriteria, matchesCriteria) - παραμένουν ίδια ---
     public List<Store> findStoresMatchingCriteria(FilterCriteria criteria) {
        List<Store> results = new ArrayList<>();
        synchronized (stores) {
            logger.fine("Starting map phase search. Filtering " + stores.size() + " local stores.");
            for (Store store : stores.values()) {
                if (matchesCriteria(store, criteria)) {
                    results.add(store);
                }
            }
        }
         logger.fine("Map phase search complete. Found " + results.size() + " matching stores locally.");
        return results;
    }
     private boolean matchesCriteria(Store store, FilterCriteria criteria) {
        // 1. Check Distance
        double distance = DistanceCalculator.calculateDistance(
                criteria.getClientLatitude(), criteria.getClientLongitude(),
                store.getLatitude(), store.getLongitude()
        );
        if (distance > criteria.getRadiusKm()) {
             logger.finest(() -> store.getStoreName() + " rejected: distance " + distance + " > " + criteria.getRadiusKm());
            return false;
        }
        // 2. Check Food Category
        if (criteria.getFoodCategories() != null && !criteria.getFoodCategories().isEmpty()) {
            if (!criteria.getFoodCategories().contains(store.getFoodCategory())) {
                 logger.finest(() -> store.getStoreName() + " rejected: food category '" + store.getFoodCategory() + "' not in " + criteria.getFoodCategories());
                return false;
            }
        }
        // 3. Check Minimum Stars
        if (criteria.getMinStars() > 0) {
            if (store.getStars() < criteria.getMinStars()) {
                 logger.finest(() -> store.getStoreName() + " rejected: stars " + store.getStars() + " < " + criteria.getMinStars());
                return false;
            }
        }
        // 4. Check Price Category
         if (store.getPriceCategory() == null || store.getPriceCategory().isEmpty()) {
             logger.warning("Store " + store.getStoreName() + " missing price category during filtering!");
             return false;
         }
        if (criteria.getPriceCategories() != null && !criteria.getPriceCategories().isEmpty()) {
            if (!criteria.getPriceCategories().contains(store.getPriceCategory())) {
                 logger.finest(() -> store.getStoreName() + " rejected: price category '" + store.getPriceCategory() + "' not in " + criteria.getPriceCategories());
                return false;
            }
        }
         logger.finest(() -> store.getStoreName() + " matched criteria.");
        return true;
    }


    // --- Purchase Processing Method (Τροποποιημένη) ---
    public Response processPurchase(PurchaseRequest request) {
        Store store = getStore(request.getStoreName()); // Uses the synchronized getStore

        if (store == null) {
            logger.warning("Purchase failed: Store not found locally - " + request.getStoreName());
            return new Response(Response.Status.FAILED, "Store not found on this worker.", null);
        }

        // *** Critical Section: Synchronize on the specific store object ***
        synchronized (store) {
            logger.fine("Processing purchase for store: " + store.getStoreName() + " (Thread ID: " + Thread.currentThread().getId() + ")");
            double totalCost = 0;
            Map<Product, Integer> productsToProcess = new HashMap<>(); // Store Product object and quantity

            // --- PHASE 1: Validate products and check stock ---
            for (Map.Entry<String, Integer> item : request.getProductsToBuy().entrySet()) {
                String productName = item.getKey();
                int quantity = item.getValue();

                if (quantity <= 0) {
                    logger.warning("Purchase item ignored for " + store.getStoreName() + ": Invalid quantity (" + quantity + ") for product '" + productName + "'.");
                    continue; // Ignore invalid quantities, but don't fail the whole order for this
                }

                Product product = store.findProduct(productName);

                if (product == null) {
                    logger.warning("Purchase FAILED for " + store.getStoreName() + ": Product '" + productName + "' not found.");
                    // Fail entire order if product not found
                    return new Response(Response.Status.FAILED, "Product not found: " + productName, null);
                }

                if (!product.isAvailable()) {
                    logger.warning("Purchase FAILED for " + store.getStoreName() + ": Product '" + productName + "' is marked as unavailable.");
                    return new Response(Response.Status.FAILED, "Product is unavailable: " + productName, null);
                }

                // --- STOCK CHECK ---
                if (product.getAvailableAmount() < quantity) {
                    logger.warning("Purchase FAILED for " + store.getStoreName() + ": Insufficient stock for '" + productName + "'. Requested: " + quantity + ", Available: " + product.getAvailableAmount());
                    // Fail entire order if stock is insufficient
                    return new Response(Response.Status.FAILED,
                            "Insufficient stock for: " + productName + ". Available: " + product.getAvailableAmount() + ", Requested: " + quantity,
                            null);
                }
                // --- End Stock Check ---

                // If product exists and stock is sufficient, add to temporary map and calculate cost
                productsToProcess.put(product, quantity);
                totalCost += product.getPrice() * quantity;
            }

            // --- PHASE 2: If all checks passed, update stock, sales, and revenue ---
            // This part executes only if the loop above completed without returning a FAILED response.
            if (productsToProcess.isEmpty() && !request.getProductsToBuy().isEmpty()) {
                // This case might happen if only invalid quantities were requested.
                logger.warning("Purchase request for " + store.getStoreName() + " contained only items with invalid quantity.");
                return new Response(Response.Status.FAILED, "No valid items in purchase request.", null);
            }

             logger.fine("Stock verified for all items in purchase for " + store.getStoreName() + ". Proceeding with updates.");

            for (Map.Entry<Product, Integer> entry : productsToProcess.entrySet()) {
                 Product product = entry.getKey();
                 int quantity = entry.getValue();

                 // Decrease stock
                 int newStock = product.getAvailableAmount() - quantity;
                 product.setAvailableAmount(newStock);
                 logger.finest(()-> "Decreased stock for " + product.getProductName() + " in " + store.getStoreName() + " by " + quantity + ". New stock: " + newStock);

                 // Update sales count
                 product.addSoldCount(quantity);
                 logger.finest(()-> "Updated sales for " + product.getProductName() + " in " + store.getStoreName() + " by " + quantity + ". New total sold: " + product.getTotalSold());
            }

            // Update total revenue for the store
            store.addRevenue(totalCost);
             logger.fine("Updated total revenue for " + store.getStoreName() + " by " + String.format("%.2f", totalCost) + ". New total: " + String.format("%.2f", store.getTotalRevenue()));

             logger.info("Purchase successful for store: " + store.getStoreName() + " (Total cost: " + String.format("%.2f", totalCost) + ")");
            // Return success response, potentially including the total cost
            return new Response(Response.Status.SUCCESS, "Purchase processed successfully. Total Cost: " + String.format("%.2f", totalCost) + "€", totalCost);

        } // *** End Critical Section for the specific store ***
    }

        // --- ΝΕΑ ΜΕΘΟΔΟΣ ---
     /**
      * Marks a product as unavailable in a specific store.
      * Requires synchronization on the store object.
      * @param storeName Name of the store.
      * @param productName Name of the product to remove (mark as unavailable).
      * @return Response indicating success/failure.
      */
      public Response removeProductFromStore(String storeName, String productName) {
        Store store = getStore(storeName); // Already synchronized internally
        if (store == null) {
            logger.log(Level.WARNING, "[REMOVE PROD FAILED] Store not found: {0}", storeName);
            return new Response(Response.Status.FAILED, "Store not found: " + storeName, null);
        }

        // Synchronize on the specific store for modification
        synchronized (store) {
            Product product = store.findProduct(productName);
            if (product == null) {
                 logger.log(Level.WARNING, "[REMOVE PROD FAILED] Store: {0}, Product not found: {1}", new Object[]{storeName, productName});
                return new Response(Response.Status.FAILED, "Product not found: " + productName + " in store " + storeName, null);
            }

            // Check if already removed (optional)
            if (!product.isAvailable()) {
                 logger.log(Level.INFO, "[REMOVE PROD] Store: {0}, Product already marked as unavailable: {1}", new Object[]{storeName, productName});
                 return new Response(Response.Status.SUCCESS, "Product was already unavailable.", null); // Or FAILED depending on desired semantics
            }

            // Mark the product as unavailable
            product.setAvailable(false);
            logger.log(Level.INFO, "[REMOVE PROD SUCCESS] Store: {0}, Marked product as unavailable: {1}", new Object[]{storeName, productName});

            // Note: We do NOT recalculate price category here, as the product still exists for manager queries.
            // If removal should affect price category, recalculation would be needed.

            return new Response(Response.Status.SUCCESS, "Product marked as unavailable successfully.", null);
        } // End synchronized block
    }


   // --- Manager Update Methods (addProductToStore, updateProductStock) - παραμένουν ίδια ---
   public synchronized Response addProductToStore(String storeName, Product product) {
       Store store = stores.get(storeName);
       if (store == null) {
           return new Response(Response.Status.FAILED, "Store not found: " + storeName, null);
       }
       synchronized(store) {
            if (store.findProduct(product.getProductName()) != null) {
                return new Response(Response.Status.FAILED, "Product already exists: " + product.getProductName(), null);
            }
           store.addProduct(product);
           store.calculateAndSetPriceCategory();
           logger.info("Added product " + product.getProductName() + " to store " + storeName + ". New price category: " + store.getPriceCategory());
       }
       return new Response(Response.Status.SUCCESS, "Product added.", null);
   }
   public synchronized Response updateProductStock(String storeName, String productName, int newStock) {
       Store store = stores.get(storeName);
        if (store == null) {
           return new Response(Response.Status.FAILED, "Store not found: " + storeName, null);
       }
        synchronized(store) {
            Product product = store.findProduct(productName);
             if (product == null) {
                return new Response(Response.Status.FAILED, "Product not found: " + productName, null);
            }
            if (newStock < 0) { // Add validation for negative stock by manager
                 return new Response(Response.Status.FAILED, "New stock cannot be negative.", null);
            }
            product.setAvailableAmount(newStock);
            logger.info("Updated stock for " + productName + " in store " + storeName + " to " + newStock);
        }
        return new Response(Response.Status.SUCCESS, "Stock updated.", null);
   }

   // --- Rating Method (addRating) - παραμένει ίδια ---
    public Response addRating(String storeName, int rating) {
        if (rating < 1 || rating > 5) {
            return new Response(Response.Status.FAILED, "Invalid rating value (must be 1-5).", null);
        }
        Store store = getStore(storeName); // Uses synchronized getStore
        if (store == null) {
            logger.warning("Rating failed: Store not found locally - " + storeName);
            return new Response(Response.Status.FAILED, "Store not found on this worker.", null);
        }
        synchronized (store) {
            logger.fine("Processing rating for store: " + store.getStoreName() + " (Thread ID: " + Thread.currentThread().getId() + ")");
            int currentVotes = store.getNoOfVotes();
            double currentAvg = store.getStars();
            double currentTotalStarSum = currentAvg * currentVotes;
            int newVoteCount = currentVotes + 1;
            double newTotalStarSum = currentTotalStarSum + rating;
            double newAverageStars = (newVoteCount == 0) ? 0 : newTotalStarSum / newVoteCount;
            int newStarsInt = (int) Math.round(newAverageStars);
            newStarsInt = Math.max(1, Math.min(5, newStarsInt));
            store.setStars(newStarsInt);
            store.setNoOfVotes(newVoteCount);
            logger.info("Rating added for " + storeName + ". New avg stars (rounded): " + newStarsInt + ", Total votes: " + newVoteCount);
            return new Response(Response.Status.SUCCESS, "Rating submitted successfully.", null);
        }
    }

    // --- ΝΕΑ ΜΕΘΟΔΟΣ ---
     /**
      * Retrieves the total sales count for each product in a specific store.
      * Includes products marked as unavailable.
      * @param storeName The name of the store.
      * @return Response object containing a Map<ProductName, TotalSold> or failure status.
      */
      // No need for the whole method to be synchronized, just the access part.
     public Response getSalesPerProduct(String storeName) {
         Store store = getStore(storeName); // getStore is synchronized
         if (store == null) {
              logger.log(Level.WARNING, "[GET SALES FAILED] Store not found: {0}", storeName);
             return new Response(Response.Status.FAILED, "Store not found: " + storeName, null);
         }

         // Create a map to hold the results (ProductName -> TotalSold)
         // Using LinkedHashMap preserves the order products were added/exist in the list.
         Map<String, Integer> salesData = new LinkedHashMap<>();

         // Synchronize on the store object while accessing its product list
         // to ensure consistency if a purchase/update happens concurrently.
         synchronized (store) {
             List<Product> products = store.getProducts();
             if (products != null) {
                 for (Product product : products) {
                      // Add every product, including those marked unavailable, as requested
                     if (product != null) { // Basic null check
                         salesData.put(product.getProductName(), product.getTotalSold());
                     }
                 }
             }
         } // End synchronized block

         logger.log(Level.INFO, "[GET SALES SUCCESS] Retrieved product sales data for store: {0}", storeName);
         return new Response(Response.Status.SUCCESS, "Product sales data retrieved.", salesData);
     }
     // --- ΤΕΛΟΣ ΝΕΑΣ ΜΕΘΟΔΟΥ ---
} // End of StoreRepository