package dummy_client;

import shared.dto.*; 

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*; 
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors; 

public class DummyClientApp {

    private static final Logger logger = Logger.getLogger(DummyClientApp.class.getName());
    private final String masterHost;
    private final int masterPort;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final Scanner consoleScanner;

    // Store the results of the last search to allow buying from them
    private List<Store> lastSearchResults = null;

    public DummyClientApp(String host, int port) {
        this.masterHost = host;
        this.masterPort = port;
        this.consoleScanner = new Scanner(System.in);
    }

    public boolean connectToMaster() {
        try {
            socket = new Socket(masterHost, masterPort);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            logger.info("Connected to Master at " + masterHost + ":" + masterPort);
            return true;
        } catch (UnknownHostException e) {
            logger.log(Level.SEVERE, "Master host not found: " + masterHost, e);
            return false;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Couldn't connect to Master at " + masterHost + ":" + masterPort, e);
            return false;
        }
    }

     public void run() {
        if (!connectToMaster()) {
            System.err.println("Failed to connect to the Master server. Exiting.");
            return;
        }

        int choice;
        do {
            displayMenu();
            System.out.print("Enter your choice: ");
            try {
                choice = Integer.parseInt(consoleScanner.nextLine());
            } catch (NumberFormatException e) {
                choice = -1; // Invalid choice
            }

            switch (choice) {
                case 1:
                    handleSearch();
                    break;
                case 2:
                    handleBuy();
                    break;
                case 3:
                    handleRateStore(); 
                    break;
                case 0:
                    System.out.println("Exiting Dummy Client App...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
             if (choice != 0) {
                 System.out.println("\nPress Enter to continue...");
                 consoleScanner.nextLine(); // Pause
            }

        } while (choice != 0);

        closeConnection();
        consoleScanner.close();
    }

    private void displayMenu() {
        System.out.println("\n--- Dummy Client Menu ---");
        System.out.println("1. Search Stores");
        System.out.println("2. Buy Products (from last search results)");
        System.out.println("3. Rate Store (from last search results)"); // <-- ΝΕΟ
        System.out.println("-------------------------");
        System.out.println("0. Exit");
        System.out.println("-------------------------");
    }

    // --- Action Handlers ---

    private void handleSearch() {
         try {
             System.out.println("\n--- Search Stores ---");
             System.out.println("Enter your location (required):");
             System.out.print("Your Latitude: ");
             double lat = Double.parseDouble(consoleScanner.nextLine());
             System.out.print("Your Longitude: ");
             double lon = Double.parseDouble(consoleScanner.nextLine());

             FilterCriteria criteria = new FilterCriteria(lat, lon);

             // Optional Filters
             System.out.print("Filter by Food Category (e.g., pizzeria,souvlaki - comma separated, leave blank for no filter): ");
             String foodCatsInput = consoleScanner.nextLine().trim();
             if (!foodCatsInput.isEmpty()) {
                  criteria.setFoodCategories(
                      Arrays.stream(foodCatsInput.split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .collect(Collectors.toList())
                  );
             }

              System.out.print("Filter by Minimum Stars (1-5, enter 0 for no filter): ");
              int minStars = Integer.parseInt(consoleScanner.nextLine());
              if (minStars >= 1 && minStars <= 5) {
                  criteria.setMinStars(minStars);
              }

              System.out.print("Filter by Price Category (e.g., $,$$ - comma separated, leave blank for no filter): ");
              String priceCatsInput = consoleScanner.nextLine().trim();
               if (!priceCatsInput.isEmpty()) {
                  List<String> validPriceCats = Arrays.asList("$", "$$", "$$$");
                  criteria.setPriceCategories(
                      Arrays.stream(priceCatsInput.split(","))
                            .map(String::trim)
                            .filter(validPriceCats::contains) // Only allow valid price categories
                            .collect(Collectors.toList())
                  );
              }

              sendRequestAndProcessSearchResponse(criteria);

         } catch (NumberFormatException e) {
             System.err.println("Invalid number format. Please enter correct numeric values for location/stars.");
         } catch (Exception e) {
              System.err.println("An error occurred during search input: " + e.getMessage());
              logger.log(Level.WARNING, "Input error in handleSearch", e);
         }
    }
    private void handleRateStore() {
        System.out.println("\n--- Rate Store ---");
        if (lastSearchResults == null || lastSearchResults.isEmpty()) {
            System.out.println("You need to perform a search first to see available stores.");
            return;
        }
   
        try {
            // Display last search results
            System.out.println("Stores found in last search:");
            for (int i = 0; i < lastSearchResults.size(); i++) {
                Store store = lastSearchResults.get(i);
                System.out.printf("%d: %s (%d*)\n", i, store.getStoreName(), store.getStars());
            }
            System.out.println("-------------------------");
   
            // Ask user to select a store
            System.out.print("Enter the number of the store you want to rate: ");
            int storeIndex = Integer.parseInt(consoleScanner.nextLine());
   
            if (storeIndex < 0 || storeIndex >= lastSearchResults.size()) {
                System.err.println("Invalid store number selected.");
                return;
            }
            Store selectedStore = lastSearchResults.get(storeIndex);
   
            // Ask for rating
            System.out.print("Enter your rating for " + selectedStore.getStoreName() + " (1-5): ");
            int rating = Integer.parseInt(consoleScanner.nextLine());
   
             if (rating < 1 || rating > 5) {
                System.err.println("Invalid rating. Please enter a number between 1 and 5.");
                return;
            }
   
            // Create and send request
            RateStoreRequest rateRequest = new RateStoreRequest(selectedStore.getStoreName(), rating);
            System.out.println("Sending rating to Master...");
            sendRequest(rateRequest); // 
   
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format entered.");
        } catch (IndexOutOfBoundsException e) {
             System.err.println("Invalid store number selected.");
        } catch (IllegalArgumentException e) { 
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
             System.err.println("An error occurred while rating: " + e.getMessage());
             logger.log(Level.WARNING, "Error in handleRateStore", e);
        }
    }

    private void handleBuy() {
        System.out.println("\n--- Buy Products ---");
        if (lastSearchResults == null || lastSearchResults.isEmpty()) {
            System.out.println("You need to perform a search first to see available stores.");
            return;
        }
  
        try {
            // Display stores (same as before)
            System.out.println("Stores found in last search:");
            for (int i = 0; i < lastSearchResults.size(); i++) {
                Store store = lastSearchResults.get(i);
                System.out.printf("%d: %s (%s, %d*, %s)\n", i, store.getStoreName(), store.getFoodCategory(), store.getStars(), store.getPriceCategory());
            }
            System.out.println("-------------------------");
            System.out.print("Enter the number of the store you want to buy from: ");
            int storeIndex = Integer.parseInt(consoleScanner.nextLine());
  
            if (storeIndex < 0 || storeIndex >= lastSearchResults.size()) {
                System.err.println("Invalid store number selected.");
                return;
            }
  
            Store selectedStore = lastSearchResults.get(storeIndex);
            System.out.println("\nProducts available at " + selectedStore.getStoreName() + ":");
            List<Product> products = selectedStore.getProducts();
             if (products == null || products.isEmpty()) {
                System.out.println("No products listed for this store.");
                return;
            }
            for(Product p : products) {
                 System.out.printf("  - %s (Type: %s, Price: %.2f €, Available: %d)\n", 
                                   p.getProductName(), p.getProductType(), p.getPrice(), p.getAvailableAmount());
            }
            System.out.println("-------------------------");
  
  
            Map<String, Integer> productsToBuy = new HashMap<>();
            String productName;
            while (true) {
                System.out.print("Enter Product Name to buy (or leave blank to finish): ");
                productName = consoleScanner.nextLine().trim();
                if (productName.isEmpty()) {
                    break;
                }
  
                Product foundProduct = selectedStore.findProduct(productName);
                if (foundProduct == null) {
                     System.err.println("Product '" + productName + "' not found in this store. Please try again.");
                    continue;
                }
  
                int availableStock = foundProduct.getAvailableAmount();
                System.out.print("Quantity for " + productName + " (Available: " + availableStock + "): ");
                try {
                    int quantity = Integer.parseInt(consoleScanner.nextLine());
                    if (quantity <= 0) {
                        System.err.println("Quantity must be positive.");
                        continue; 
                    }
  
                    if (quantity > availableStock) {
                         System.err.println("Warning: Requested quantity (" + quantity + ") exceeds available stock (" + availableStock + "). The purchase might fail on the server.");
                        
                    }
  
                    productsToBuy.put(productName, quantity);
  
                } catch (NumberFormatException e) {
                    System.err.println("Invalid quantity.");
                }
            }
  
            if (!productsToBuy.isEmpty()) {
                 PurchaseRequest purchaseRequest = new PurchaseRequest(selectedStore.getStoreName(), productsToBuy);
                 System.out.println("Sending purchase request to Master...");
                 sendRequest(purchaseRequest); 
            } else {
                System.out.println("No products selected for purchase.");
            }
  
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format entered.");
        } catch (IndexOutOfBoundsException e) {
             System.err.println("Invalid store number selected.");
        } catch (Exception e) {
             System.err.println("An error occurred during purchase: " + e.getMessage());
             logger.log(Level.WARNING, "Error in handleBuy", e);
        }
   }


    // --- Communication Methods ---
    private void sendRequest(Serializable requestObject) {
        if (!isConnected()) {
            System.err.println("Not connected to Master.");
            if (!connectToMaster()) return; // Attempt reconnect
        }
        try {
            logger.info("Sending request to Master: " + requestObject.toString());
            out.writeObject(requestObject);
            out.flush();

            // Wait for and display the standard response
            receiveResponse(null); 
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to send request to Master", e);
            System.err.println("Error communicating with Master: " + e.getMessage());
            closeConnection();
        }
    }

     
    private void sendRequestAndProcessSearchResponse(FilterCriteria criteria) {
        if (!isConnected()) {
            System.err.println("Not connected to Master.");
            if (!connectToMaster()) return;
        }
        try {
            logger.info("Sending search request to Master: " + criteria.toString());
            out.writeObject(criteria); // Send the FilterCriteria object
            out.flush();

            // Wait for and process the response, PASSING the original criteria object
            receiveResponse(criteria); // <-- ΑΛΛΑΓΗ ΕΔΩ

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to send search request to Master", e);
            System.err.println("Error communicating with Master: " + e.getMessage());
            closeConnection();
        }   
    }

    /**
  * Receives a Response object from the Master and processes it.
  * Handles the special case where the response data is a List<Store> for search results.
  * @param requestContextObject The original request object sent (e.g., FilterCriteria)
  * or null for generic requests.
  */
 private void receiveResponse(Serializable requestContextObject) { // <-- ΝΕΑ ΥΠΟΓΡΑΦΗ
    if (in == null) return;
    try {
       Response response = (Response) in.readObject();
       System.out.println("\n--- Server Response ---");
       System.out.println("Status: " + response.getStatus());
       System.out.println("Message: " + response.getMessage());

       // Handle data based on context (Search Results?)
       if (requestContextObject instanceof FilterCriteria && 
           (response.getStatus() == Response.Status.SUCCESS || response.getStatus() == Response.Status.PARTIAL_SUCCESS) &&
           response.getData() instanceof List) {

           // Cast the context object back to FilterCriteria to get client coords
           FilterCriteria originalCriteria = (FilterCriteria) requestContextObject; 

           try {
               // Attempt to cast the data to List<Store>
                @SuppressWarnings("unchecked")
               List<Store> stores = (List<Store>) response.getData();
               this.lastSearchResults = stores; // Store results locally

               System.out.println("Found " + stores.size() + " store(s):");
                if (!stores.isEmpty()) {
                    for (int i = 0; i < stores.size(); i++) {
                        Store store = stores.get(i);
                        System.out.printf("  %d: %s (%s, %d*, %s, %.2f km)\n",
                              i,
                              store.getStoreName(),
                              store.getFoodCategory(),
                              store.getStars(),
                              store.getPriceCategory() == null ? "?" : store.getPriceCategory(),
                              DistanceCalculator.calculateDistance(
                                      originalCriteria.getClientLatitude(),  
                                      originalCriteria.getClientLongitude(), 
                                      store.getLatitude(),
                                      store.getLongitude())
                              );
                    }
                }

           } catch (ClassCastException e) {
               logger.log(Level.SEVERE, "Received data is not List<Store> as expected for search results.", e);
               System.err.println("Error: Received unexpected data format from server for search results.");
                this.lastSearchResults = null;
           }

       } else if (response.getData() != null) {
            System.out.println("Data: " + response.getData().toString());
       }

      
       if (requestContextObject instanceof FilterCriteria && response.getStatus() == Response.Status.FAILED){ 
            this.lastSearchResults = null;
       }

       System.out.println("----------------------");
    } catch (IOException | ClassNotFoundException e) {
        logger.log(Level.SEVERE, "Failed to receive response from Master", e);
        System.err.println("Error receiving response from Master: " + e.getMessage());
        this.lastSearchResults = null;
         closeConnection();
    }
}

     // Helper to check connection status
     private boolean isConnected() {
         return socket != null && socket.isConnected() && !socket.isClosed() && out != null && in != null;
     }



    private void closeConnection() {
        try {
            if (in != null) in.close();
            if (out != null) out.close();
            if (socket != null && !socket.isClosed()) socket.close();
            logger.info("Connection to Master closed.");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error closing connection resources", e);
        } finally {
             in = null;
             out = null;
             socket = null;
             lastSearchResults = null; // Clear results on disconnect
        }
    }

    // --- Main Method ---
    public static void main(String[] args) {
         String host = "localhost";
         int port = 5000; // Default port 

          if (args.length >= 2) {
            host = args[0];
            try {
                port = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Invalid port number provided: " + args[1] + ". Using default port " + port);
            }
        } else if (args.length == 1) {
             host = args[0];
              System.err.println("Port not specified. Using default port " + port);
        } else {
              System.err.println("Host/Port not specified. Using defaults: " + host + ":" + port);
        }


        DummyClientApp app = new DummyClientApp(host, port);
        app.run();
    }
     
     static class DistanceCalculator {
        private static final double EARTH_RADIUS_KM = 6371.0;
        public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
            double latDistance = Math.toRadians(lat2 - lat1);
            double lonDistance = Math.toRadians(lon2 - lon1);
            double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                    + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                    * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
            return EARTH_RADIUS_KM * c;
        }
    }

} 