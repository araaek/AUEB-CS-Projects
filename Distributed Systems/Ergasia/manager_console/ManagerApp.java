package manager_console;

import shared.dto.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ManagerApp {
    private static final Logger logger = Logger.getLogger(ManagerApp.class.getName());
    private final String masterHost;
    private final int masterPort;
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final Scanner consoleScanner;

    public ManagerApp(String host, int port) {
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

     private void handleAddStore() { 
         System.out.println("\n--- Adding New Store from JSON ---");
         System.out.print("Enter the full path to the store's JSON file: ");
         String jsonFilePath = consoleScanner.nextLine();

         try {
             String jsonContent = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
             JSONObject storeJson = new JSONObject(jsonContent);
             String name = storeJson.getString("StoreName");
             double lat = storeJson.getDouble("Latitude");
             double lon = storeJson.getDouble("Longitude");
             String category = storeJson.getString("FoodCategory");
             int stars = storeJson.getInt("Stars");
             int votes = storeJson.getInt("NoOfVotes");
             String logoPath = storeJson.getString("StoreLogo");
             Store newStore = new Store(name, lat, lon, category, stars, votes, logoPath);
             JSONArray productsJsonArray = storeJson.getJSONArray("Products");
             for (int i = 0; i < productsJsonArray.length(); i++) {
                 JSONObject productJson = productsJsonArray.getJSONObject(i);
                 String prodName = productJson.getString("ProductName");
                 String prodType = productJson.getString("ProductType");
                 int stock = productJson.optInt("Available Amount", 0);
                 double price = productJson.getDouble("Price");
                 Product product = new Product(prodName, prodType, stock, price);
                 newStore.addProduct(product);
             }
             System.out.println("Parsed JSON successfully. Sending store data to Master...");
             sendRequest(newStore);
         } catch (IOException e) {
             System.err.println("Error reading JSON file: " + e.getMessage());
             logger.log(Level.SEVERE, "Could not read JSON file: " + jsonFilePath, e);
         } catch (org.json.JSONException e) {
             System.err.println("Error parsing JSON content: " + e.getMessage());
             System.err.println("Please ensure the JSON file structure is correct (check quotes, commas, brackets). Example:\n" + getExampleJson());
             logger.log(Level.SEVERE, "JSON parsing error", e);
         } catch (Exception e) {
             System.err.println("An unexpected error occurred: " + e.getMessage());
             logger.log(Level.SEVERE, "Error in handleAddStore", e);
         }
     }
      private String getExampleJson() {
          return """
                 {
                   "StoreName": "Example Pizzeria",
                   "Latitude": 37.99,
                   "Longitude": 23.73,
                   "FoodCategory": "pizzeria",
                   "Stars": 4,
                   "NoOfVotes": 50,
                   "StoreLogo": "/path/logos/pizzeria.png",
                   "Products": [
                     {
                       "ProductName": "margarita",
                       "ProductType": "pizza",
                       "Available Amount": 50,
                       "Price": 8.5
                     },
                     {
                       "ProductName": "coca cola",
                       "ProductType": "drink",
                       "Available Amount": 100,
                       "Price": 1.5
                     }
                   ]
                 }
                 """;
      }
      private void handleAddProduct() {
          try {
              System.out.println("\n--- Add Product to Existing Store ---");
              System.out.print("Enter Store Name: ");
              String storeName = consoleScanner.nextLine();
              System.out.print("New Product Name: ");
              String prodName = consoleScanner.nextLine();
              System.out.print("New Product Type: ");
              String prodType = consoleScanner.nextLine();
              System.out.print("Initial Available Amount: ");
              int stock = Integer.parseInt(consoleScanner.nextLine());
              System.out.print("Price: ");
              double price = Double.parseDouble(consoleScanner.nextLine());
              Product newProduct = new Product(prodName, prodType, stock, price);
              AddProductRequest request = new AddProductRequest(storeName, newProduct);
              sendRequest(request);
          } catch (NumberFormatException e) {
             System.err.println("Invalid number format.");
         } catch (Exception e) {
              System.err.println("An error occurred: " + e.getMessage());
              logger.log(Level.WARNING, "Error in handleAddProduct", e);
         }
     }
     private void handleUpdateStock() {
          try {
              System.out.println("\n--- Update Product Stock ---");
              System.out.print("Enter Store Name: ");
              String storeName = consoleScanner.nextLine();
              System.out.print("Enter Product Name to Update: ");
              String productName = consoleScanner.nextLine();
              System.out.print("Enter New Stock Amount: ");
              int newStock = Integer.parseInt(consoleScanner.nextLine());
              if (newStock < 0) {
                  System.err.println("Stock cannot be negative.");
                  return;
              }
              UpdateStockRequest request = new UpdateStockRequest(storeName, productName, newStock);
              sendRequest(request);
          } catch (NumberFormatException e) {
             System.err.println("Invalid number format for stock.");
         } catch (Exception e) {
              System.err.println("An error occurred: " + e.getMessage());
              logger.log(Level.WARNING, "Error in handleUpdateStock", e);
         }
     }


    private void handleRemoveProduct() {
        try {
            System.out.println("\n--- Remove Product from Store (Mark as Unavailable) ---");
            System.out.print("Enter Store Name: ");
            String storeName = consoleScanner.nextLine();
            System.out.print("Enter Product Name to Remove: ");
            String productName = consoleScanner.nextLine();

            if (storeName.trim().isEmpty() || productName.trim().isEmpty()) {
                System.err.println("Store name and product name cannot be empty.");
                return;
            }

            RemoveProductRequest request = new RemoveProductRequest(storeName, productName);

            sendRequest(request);

        } catch (Exception e) {
             System.err.println("An error occurred: " + e.getMessage());
             logger.log(Level.WARNING, "Error in handleRemoveProduct", e);
        }
    }
    private void handleDisplaySales() {
        try {
            System.out.println("\n--- Display Total Sales per Product for a Store ---");
            System.out.print("Enter Store Name: ");
            String storeName = consoleScanner.nextLine();

            if (storeName.trim().isEmpty()) {
                System.err.println("Store name cannot be empty.");
                return;
            }

            GetStoreProductSalesRequest request = new GetStoreProductSalesRequest(storeName);

       
            sendRequest(request);

        } catch (Exception e) {
             System.err.println("An error occurred: " + e.getMessage());
             logger.log(Level.WARNING, "Error in handleDisplaySales", e);
        }
    }

    private void displayMenu() {
        System.out.println("\n--- Manager Console Menu ---");
        System.out.println("1. Add New Store (from JSON file)");
        System.out.println("2. Add Product to Existing Store");
        System.out.println("3. Update Product Stock in Store");
        System.out.println("4. Remove Product from Store");
        System.out.println("5. Display Sales per Product in Store"); // Νέα επιλογή
        System.out.println("----------------------------");
        System.out.println("0. Exit");
        System.out.println("----------------------------");
    }

     public void run() {
         if (!connectToMaster()) {
            if (!connectToMaster()) {
                System.err.println("Failed to connect to the Master server. Exiting.");
                return; 
            }
          }
         int choice;
         do {
             displayMenu();
             System.out.print("Enter your choice: ");
             try { choice = Integer.parseInt(consoleScanner.nextLine()); }
             catch (NumberFormatException e) { choice = -1; }

             switch (choice) {
                 case 1: handleAddStore(); break;
                 case 2: handleAddProduct(); break;
                 case 3: handleUpdateStock(); break;
                 case 4: handleRemoveProduct(); break;
                 case 5: handleDisplaySales(); break; 
                 case 0: System.out.println("Exiting Manager App..."); break;
                 default: System.out.println("Invalid choice. Please try again.");
             }
             if (choice >= 1 && choice <= 5) { 
                  System.out.println("\nPress Enter to continue...");
                  consoleScanner.nextLine(); 
             }
         } while (choice != 0);
         closeConnection();
         consoleScanner.close();
     }


     private void sendRequest(Serializable requestObject) { 
          if (out == null) {
              System.err.println("Not connected to Master.");
              if (!connectToMaster()) return;
          }
          try {
              System.out.println("Sending request to Master: " + requestObject.toString());
              out.writeObject(requestObject);
              out.flush();
              receiveResponse();
          } catch (IOException e) {
              logger.log(Level.SEVERE, "Failed to send request to Master", e);
              System.err.println("Error communicating with Master: " + e.getMessage());
              closeConnection();
          }
     }
      private void receiveResponse() {
           if (in == null) return;
           try {
              Response response = (Response) in.readObject();
              System.out.println("\n--- Server Response ---");
              System.out.println("Status: " + response.getStatus());
              System.out.println("Message: " + response.getMessage());
              if (response.getData() != null) {
                   System.out.println("Data: " + response.getData().toString());
              }
              System.out.println("----------------------");
           } catch (IOException | ClassNotFoundException e) {
               logger.log(Level.SEVERE, "Failed to receive response from Master", e);
               System.err.println("Error receiving response from Master: " + e.getMessage());
                closeConnection();
           }
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
           }
       }
        public static void main(String[] args) { 
             String host = "localhost";
             int port = 5000;
             if (args.length >= 2) { host = args[0]; try { port = Integer.parseInt(args[1]); } catch (NumberFormatException e) { System.err.println("Invalid port: " + args[1]); } }
             else if (args.length == 1) { host = args[0]; }
             System.err.println("Using Master: " + host + ":" + port);
             ManagerApp app = new ManagerApp(host, port);
             app.run();
         }

} 