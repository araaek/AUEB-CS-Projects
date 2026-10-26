
package worker;

import shared.dto.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class MasterHandler implements Runnable {
    private static final Logger logger = Logger.getLogger(MasterHandler.class.getName());
    private final Socket masterSocket;
    private final StoreRepository storeRepository;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public MasterHandler(Socket socket, StoreRepository repository) {
        this.masterSocket = socket;
        this.storeRepository = repository;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(masterSocket.getOutputStream());
            in = new ObjectInputStream(masterSocket.getInputStream());

            RequestType requestType = (RequestType) in.readObject();
            logger.info("Received request type from Master: " + requestType);

            switch (requestType) {
                case ADD_STORE:
                    Store storeToAdd = (Store) in.readObject();
                    storeRepository.addOrUpdateStore(storeToAdd);
                    sendResponse(new Response(Response.Status.SUCCESS, "Store added/updated by worker.", null));
                    break;
                case SEARCH_STORES:
                     FilterCriteria criteria = (FilterCriteria) in.readObject();
                     List<Store> results = storeRepository.findStoresMatchingCriteria(criteria);
                     sendObject(results); // Send raw list
                     break;
                case PURCHASE_PRODUCTS:
                     PurchaseRequest purchase = (PurchaseRequest) in.readObject();
                     Response purchaseResponse = storeRepository.processPurchase(purchase);
                     sendResponse(purchaseResponse);
                     break;
                case ADD_PRODUCT:
                     AddProductRequest addReq = (AddProductRequest) in.readObject();
                     Response addResp = storeRepository.addProductToStore(addReq.getStoreName(), addReq.getNewProduct());
                     sendResponse(addResp);
                     break;
                case UPDATE_STOCK:
                     UpdateStockRequest stockReq = (UpdateStockRequest) in.readObject();
                     Response stockResp = storeRepository.updateProductStock(stockReq.getStoreName(), stockReq.getProductName(), stockReq.getNewStock());
                     sendResponse(stockResp);
                     break;
                case RATE_STORE:
                     RateStoreRequest rateReq = (RateStoreRequest) in.readObject();
                     Response rateResp = storeRepository.addRating(rateReq.getStoreName(), rateReq.getRating());
                     sendResponse(rateResp);
                     break;

                case REMOVE_PRODUCT:
                    RemoveProductRequest removeReq = (RemoveProductRequest) in.readObject();
                    Response removeResp = storeRepository.removeProductFromStore(removeReq.getStoreName(), removeReq.getProductName());
                    sendResponse(removeResp); 
                    break;
                case GET_STORE_PRODUCT_SALES: {
                    GetStoreProductSalesRequest salesReq = (GetStoreProductSalesRequest) in.readObject();
                    Response salesResp = storeRepository.getSalesPerProduct(salesReq.getStoreName());
                    sendResponse(salesResp); 
                    break;
                }

                default:
                    logger.warning("Unknown request type received: " + requestType);
            }

        } catch (EOFException | SocketException e) {
            logger.info("Master disconnected: " + masterSocket.getInetAddress() + " (" + e.getMessage() + ")");
        } catch (IOException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error handling Master connection " + masterSocket.getInetAddress(), e);
        } finally {
            closeConnection();
        }
    }

      private void sendResponse(Response response) { sendObject(response); }
       private void sendObject(Object object) { 
           if (!masterSocket.isClosed() && out != null) { try { out.writeObject(object); out.flush(); } catch (IOException e) { logger.log(Level.SEVERE, "Failed to send object to Master", e); closeConnection(); } }
       }
        private void closeConnection() { 
            try { if (in != null) in.close(); if (out != null) out.close(); if (masterSocket != null && !masterSocket.isClosed()) masterSocket.close(); logger.fine("Closed connection to Master: " + masterSocket.getInetAddress()); } catch (IOException e) { logger.log(Level.SEVERE, "Error closing Master connection resources", e); }
        }

}