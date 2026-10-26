package master;

import shared.WorkerInfo;
import shared.dto.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ClientHandler implements Runnable {

    private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());
    private final Socket clientSocket;
    private final Master master;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private static final long SEARCH_TIMEOUT_MS = 30000;
    private final int[] workersFinishedCount = {0};
    private final Object searchLock = new Object();

    public ClientHandler(Socket socket, Master master) {
        this.clientSocket = socket;
        this.master = master;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(clientSocket.getOutputStream());
            in = new ObjectInputStream(clientSocket.getInputStream());

            Object requestObject;
            while (!Thread.currentThread().isInterrupted() && (requestObject = in.readObject()) != null) {

                if (requestObject instanceof Store) {
                    Store storeToAdd = (Store) requestObject;
                    logger.info("Received ADD_STORE request for: " + storeToAdd.getStoreName());
                    handleStoreUpdateRequest(storeToAdd, RequestType.ADD_STORE);
                } else if (requestObject instanceof FilterCriteria) {
                    FilterCriteria criteria = (FilterCriteria) requestObject;
                    logger.info("Received SEARCH_STORES request with criteria: " + criteria);
                    handleSearchRequest(criteria);
                } else if (requestObject instanceof PurchaseRequest) {
                     PurchaseRequest purchase = (PurchaseRequest) requestObject;
                     logger.info("Received PURCHASE_PRODUCTS request: " + purchase);
                     handlePurchaseRequest(purchase);
                } else if (requestObject instanceof AddProductRequest) {
                    AddProductRequest req = (AddProductRequest) requestObject;
                    logger.info("Received ADD_PRODUCT request for store: " + req.getStoreName());
                    forwardUpdateRequestToWorker(req.getStoreName(), RequestType.ADD_PRODUCT, req);
                } else if (requestObject instanceof UpdateStockRequest) {
                     UpdateStockRequest req = (UpdateStockRequest) requestObject;
                     logger.info("Received UPDATE_STOCK request for store: " + req.getStoreName() + ", product: " + req.getProductName());
                     forwardUpdateRequestToWorker(req.getStoreName(), RequestType.UPDATE_STOCK, req);
                } else if (requestObject instanceof RateStoreRequest) {
                     RateStoreRequest req = (RateStoreRequest) requestObject;
                     logger.info("Received RATE_STORE request for store: " + req.getStoreName() + " with rating: " + req.getRating());
                     forwardUpdateRequestToWorker(req.getStoreName(), RequestType.RATE_STORE, req);
                }
                else if (requestObject instanceof RemoveProductRequest) {
                    RemoveProductRequest req = (RemoveProductRequest) requestObject;
                    logger.info("Received REMOVE_PRODUCT request for store: " + req.getStoreName() + ", product: " + req.getProductName());
                    forwardUpdateRequestToWorker(req.getStoreName(), RequestType.REMOVE_PRODUCT, req);
                }
                else if (requestObject instanceof GetStoreProductSalesRequest) {
                    GetStoreProductSalesRequest req = (GetStoreProductSalesRequest) requestObject;
                    logger.info("Received GET_STORE_PRODUCT_SALES request for store: " + req.getStoreName());
                    forwardUpdateRequestToWorker(req.getStoreName(), RequestType.GET_STORE_PRODUCT_SALES, req);
                }
                else {
                    logger.warning("Received unknown object type: " + requestObject.getClass().getName());
                    sendResponse(new Response(Response.Status.FAILED, "Unknown request type", null));
                }
            }
        } catch (EOFException | SocketException e) {
              if (!Thread.currentThread().isInterrupted()) { logger.info("Client disconnected: " + clientSocket.getInetAddress() + " (" + e.getMessage() + ")"); } else { logger.info("ClientHandler interrupted, likely during shutdown."); }
        } catch (IOException | ClassNotFoundException e) {
               if (!Thread.currentThread().isInterrupted()) { logger.log(Level.SEVERE, "Error handling client " + clientSocket.getInetAddress(), e); } else { logger.info("ClientHandler interrupted, likely during shutdown."); }
        } finally {
            closeConnection();
            logger.fine("ClientHandler thread finished for: " + clientSocket.getInetAddress());
        }
    }

     private void forwardUpdateRequestToWorker(String storeName, RequestType type, Serializable requestData) { 
         WorkerInfo targetWorker = master.getWorkerForStore(storeName);
         if (targetWorker == null) {
             logger.severe("No workers available for update: " + storeName);
             sendResponse(new Response(Response.Status.FAILED, "Internal Server Error: No workers available", null));
             return;
         }
         logger.info("Forwarding " + type + " for " + storeName + " to Worker " + targetWorker.getId());
         try (Socket workerSocket = new Socket(targetWorker.getHost(), targetWorker.getPort());
              ObjectOutputStream workerOut = new ObjectOutputStream(workerSocket.getOutputStream());
              ObjectInputStream workerIn = new ObjectInputStream(workerSocket.getInputStream()))
         {
              workerOut.writeObject(type);
              workerOut.writeObject(requestData);
              workerOut.flush();
              Response workerResponse = (Response) workerIn.readObject();
              logger.info("Received response from Worker " + targetWorker.getId() + " for " + type + ": " + workerResponse);
              sendResponse(workerResponse);
         } catch (IOException | ClassNotFoundException e) {
              logger.log(Level.SEVERE, "Failed communication with Worker " + targetWorker.getId() + " for update on " + storeName, e);
              sendResponse(new Response(Response.Status.FAILED, "Error communicating with storage node for update", null));
         }
     }
      private void handleStoreUpdateRequest(Store storeData, RequestType type) { 
           storeData.calculateAndSetPriceCategory();
           logger.info("Calculated price category for " + storeData.getStoreName() + ": " + storeData.getPriceCategory());
           WorkerInfo targetWorker = master.getWorkerForStore(storeData.getStoreName());
           if (targetWorker == null) {
               logger.severe("No workers available for store: " + storeData.getStoreName());
               sendResponse(new Response(Response.Status.FAILED, "Internal Server Error: No workers available", null));
               return;
           }
           logger.info("Assigning store " + storeData.getStoreName() + " to Worker " + targetWorker.getId());
           try (Socket workerSocket = new Socket(targetWorker.getHost(), targetWorker.getPort());
                ObjectOutputStream workerOut = new ObjectOutputStream(workerSocket.getOutputStream());
                ObjectInputStream workerIn = new ObjectInputStream(workerSocket.getInputStream()))
           {
                workerOut.writeObject(type);
                workerOut.writeObject(storeData);
                workerOut.flush();
                Response workerResponse = (Response) workerIn.readObject();
                logger.info("Received response from Worker " + targetWorker.getId() + ": " + workerResponse);
                sendResponse(workerResponse);
           } catch (IOException | ClassNotFoundException e) {
                logger.log(Level.SEVERE, "Failed communication with Worker " + targetWorker.getId() + " for store " + storeData.getStoreName(), e);
                sendResponse(new Response(Response.Status.FAILED, "Error communicating with storage node", null));
           }
      }
       private void handleSearchRequest(FilterCriteria criteria) {
            List<WorkerInfo> workers = master.getWorkers();
            if (workers.isEmpty()) { sendResponse(new Response(Response.Status.FAILED, "No workers available for search", null)); return; }
            final int totalWorkers = workers.size();
            final List<Store> finalResults = new ArrayList<>();
            workersFinishedCount[0] = 0;
            logger.info("Initiating Map phase across " + totalWorkers + " workers.");
            List<Thread> workerThreads = new ArrayList<>();
            for (WorkerInfo worker : workers) {
                Runnable workerTask = () -> {
                    List<Store> workerResults = null;
                    try (Socket workerSocket = new Socket(worker.getHost(), worker.getPort());
                         ObjectOutputStream workerOut = new ObjectOutputStream(workerSocket.getOutputStream());
                         ObjectInputStream workerIn = new ObjectInputStream(workerSocket.getInputStream()))
                    {
                        workerOut.writeObject(RequestType.SEARCH_STORES);
                        workerOut.writeObject(criteria);
                        workerOut.flush();
                        Object resultObject = workerIn.readObject();
                        if (resultObject instanceof List) {
                            @SuppressWarnings("unchecked") List<Store> tempResults = (List<Store>) resultObject;
                            workerResults = tempResults;
                            logger.fine("Worker " + worker.getId() + " returned " + tempResults.size() + " results.");
                        } else { logger.warning("Worker " + worker.getId() + " returned unexpected type: " + resultObject.getClass().getName()); }
                    } catch (IOException | ClassNotFoundException e) { logger.log(Level.SEVERE, "Failed communication with Worker " + worker.getId() + " during search", e); }
                    finally {
                         synchronized (searchLock) {
                             if (workerResults != null) { synchronized (finalResults) { finalResults.addAll(workerResults); } }
                             workersFinishedCount[0]++;
                             searchLock.notify();
                         }
                    }
                };
                Thread t = new Thread(workerTask);
                t.setName("SearchWorker-" + worker.getId());
                workerThreads.add(t);
                t.start();
            }
            long startTime = System.currentTimeMillis();
            long timeToWait = SEARCH_TIMEOUT_MS;
            boolean allWorkersFinished = false;
            synchronized (searchLock) {
                while (workersFinishedCount[0] < totalWorkers && timeToWait > 0) {
                    try { logger.fine("Waiting for workers... (" + workersFinishedCount[0] + "/" + totalWorkers + ")"); searchLock.wait(timeToWait); }
                    catch (InterruptedException e) { logger.log(Level.WARNING, "Search interrupted.", e); Thread.currentThread().interrupt(); interruptWorkerThreads(workerThreads); sendResponse(new Response(Response.Status.FAILED, "Search interrupted.", null)); return; }
                    long elapsedTime = System.currentTimeMillis() - startTime; timeToWait = SEARCH_TIMEOUT_MS - elapsedTime;
                }
                allWorkersFinished = (workersFinishedCount[0] == totalWorkers);
            }
            if (allWorkersFinished) { logger.info("Map phase complete. Aggregated " + finalResults.size() + " results."); sendResponse(new Response(Response.Status.SUCCESS, "Search complete", new ArrayList<>(finalResults))); }
            else { logger.warning("Search timeout! (" + workersFinishedCount[0] + "/" + totalWorkers + ")."); interruptWorkerThreads(workerThreads); sendResponse(new Response(Response.Status.PARTIAL_SUCCESS, "Search timed out.", new ArrayList<>(finalResults))); }
       }
        private void interruptWorkerThreads(List<Thread> workerThreads) { 
             logger.info("Interrupting " + workerThreads.size() + " search worker threads."); for (Thread t : workerThreads) { if (t.isAlive()) { t.interrupt(); } }
        }
         private void handlePurchaseRequest(PurchaseRequest purchase) { 
             WorkerInfo targetWorker = master.getWorkerForStore(purchase.getStoreName());
             if (targetWorker == null) { logger.severe("No workers for purchase: " + purchase.getStoreName()); sendResponse(new Response(Response.Status.FAILED, "Internal Server Error", null)); return; }
             logger.info("Forwarding purchase for " + purchase.getStoreName() + " to Worker " + targetWorker.getId());
             try (Socket workerSocket = new Socket(targetWorker.getHost(), targetWorker.getPort());
                  ObjectOutputStream workerOut = new ObjectOutputStream(workerSocket.getOutputStream());
                  ObjectInputStream workerIn = new ObjectInputStream(workerSocket.getInputStream()))
             {
                  workerOut.writeObject(RequestType.PURCHASE_PRODUCTS);
                  workerOut.writeObject(purchase);
                  workerOut.flush();
                  Response workerResponse = (Response) workerIn.readObject();
                  logger.info("Received purchase response from Worker " + targetWorker.getId() + ": " + workerResponse);
                  sendResponse(workerResponse);
             } catch (IOException | ClassNotFoundException e) { logger.log(Level.SEVERE, "Failed communication with Worker " + targetWorker.getId() + " for purchase at " + purchase.getStoreName(), e); sendResponse(new Response(Response.Status.FAILED, "Error processing purchase", null)); }
         }
          private void sendResponse(Response response) { 
              if (!clientSocket.isClosed() && out != null) { try { out.writeObject(response); out.flush(); } catch (IOException e) { logger.log(Level.SEVERE, "Failed to send response", e); closeConnection(); } }
          }
           private void closeConnection() { 
               try { if (in != null) in.close(); if (out != null) out.close(); if (clientSocket != null && !clientSocket.isClosed()) clientSocket.close(); } catch (IOException e) { logger.log(Level.SEVERE, "Error closing client resources", e); }
           }

} 