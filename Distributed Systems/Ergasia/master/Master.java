// File: master/Master.java
package master;

import shared.WorkerInfo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Master {

    private static final Logger logger = Logger.getLogger(Master.class.getName());
    private final int port;
    private final List<WorkerInfo> workers = Collections.synchronizedList(new ArrayList<>());
    private ServerSocket serverSocket;
    private volatile boolean running = true; // Flag to control the main loop and handlers

    // Keep track of client handler threads for potential shutdown management (optional)
    private final List<Thread> clientHandlerThreads = Collections.synchronizedList(new ArrayList<>());

    public Master(int port, String workerConfigPath) {
        this.port = port;
        // Removed: this.clientExecutor = Executors.newFixedThreadPool(10);
        loadWorkerConfiguration(workerConfigPath);
    }

    // loadWorkerConfiguration remains the same...
    private void loadWorkerConfiguration(String configPath) {
        logger.info("Loading worker configuration from: " + configPath);
        try (BufferedReader reader = new BufferedReader(new FileReader(configPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue; // Skip empty lines and comments
                }
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    try {
                        int id = Integer.parseInt(parts[0].trim());
                        String host = parts[1].trim();
                        int workerPort = Integer.parseInt(parts[2].trim());
                        WorkerInfo info = new WorkerInfo(id, host, workerPort);
                        workers.add(info);
                        logger.info("Loaded worker: " + info);
                    } catch (NumberFormatException e) {
                        logger.warning("Skipping invalid line in config: " + line + " - " + e.getMessage());
                    }
                } else {
                     logger.warning("Skipping malformed line in config: " + line);
                }
            }
             // Sort workers by ID for consistent hashing modulo
            workers.sort((w1, w2) -> Integer.compare(w1.getId(), w2.getId()));

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to load worker configuration: " + configPath, e);
            throw new RuntimeException("Worker configuration loading failed.", e);
        }
        if (workers.isEmpty()) {
            logger.severe("No workers loaded! Master cannot function properly.");
             throw new RuntimeException("No workers configured.");
        }
         logger.info("Total workers loaded: " + workers.size());
    }


    public void startServer() {
        try {
            serverSocket = new ServerSocket(port);
            logger.info("Master server started on port: " + port);
            running = true;

            while (running) { // Use the flag to control the loop
                try {
                    Socket clientSocket = serverSocket.accept(); // Wait for a client connection
                    logger.info("Client connected: " + clientSocket.getInetAddress());

                    // Create a handler for the client
                    ClientHandler clientHandlerRunnable = new ClientHandler(clientSocket, this);
                    // Create a new Thread for each client and start it
                    Thread clientThread = new Thread(clientHandlerRunnable);
                    clientThread.setName("ClientHandler-" + clientSocket.getInetAddress()); // Optional: name thread
                    clientHandlerThreads.add(clientThread); // Add to list (optional)
                    clientThread.start();

                    // Optional: Clean up finished threads from the list periodically
                    clientHandlerThreads.removeIf(t -> !t.isAlive());


                } catch (IOException e) {
                    if (!running || serverSocket.isClosed()) {
                        logger.info("Server socket closed or shutting down, stopping accept loop.");
                        break; // Exit loop if socket is intentionally closed or shutting down
                    }
                    logger.log(Level.SEVERE, "Error accepting client connection", e);
                }
            }
        } catch (IOException e) {
            // Log error only if we weren't intentionally stopping
            if (running) {
                logger.log(Level.SEVERE, "Could not start server on port " + port, e);
            }
        } finally {
             // Ensure cleanup happens even if loop exits unexpectedly
             if (!running) { // If stopServer was called
                  logger.info("Accept loop finished due to shutdown signal.");
             }
             // Consider additional cleanup if needed
              closeServerSocket(); // Make sure server socket is closed here
        }
    }

    public void stopServer() {
        logger.info("Shutting down Master server...");
        running = false; // Signal the accept loop to stop

        closeServerSocket(); // Close the server socket to interrupt accept()

        
        logger.info("Interrupting active client handler threads...");
        synchronized (clientHandlerThreads) { // Synchronize access to the list
            for (Thread t : clientHandlerThreads) {
                if (t.isAlive()) {
                    t.interrupt(); // Signal the thread to stop (handlers need to check Thread.interrupted())
                }
            }
        }
       

         logger.info("Master server shut down signal sent.");
         
    }

     // Helper method to close server socket safely
    private void closeServerSocket() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                logger.info("Server socket closed.");
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error closing server socket", e);
            }
        }
    }


    // getWorkers remains the same...
    public List<WorkerInfo> getWorkers() {
        return Collections.unmodifiableList(workers);
    }

    // getWorkerForStore remains the same...
    public WorkerInfo getWorkerForStore(String storeName) {
        if (workers.isEmpty()) {
            return null;
        }
        int hashCode = storeName.hashCode();
        int positiveHashCode = hashCode == Integer.MIN_VALUE ? 0 : Math.abs(hashCode);
        int workerIndex = positiveHashCode % workers.size();
        return workers.get(workerIndex);
    }

    // main method remains the same...
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java master.Master <port> <worker_config_file_path>");
            System.exit(1);
        }

        try {
            int port = Integer.parseInt(args[0]);
            String configPath = args[1];

            Master master = new Master(port, configPath);

            // Add shutdown hook - stopServer() will now be called on Ctrl+C etc.
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                 logger.info("Shutdown hook triggered. Stopping server...");
                 master.stopServer();
                 logger.info("Shutdown hook finished.");
             }));

            master.startServer(); // This will block until stopServer is called or an error occurs

        } catch (NumberFormatException e) {
            System.err.println("Invalid port number: " + args[0]);
            System.exit(1);
        } catch (RuntimeException e) {
             System.err.println("Failed to initialize Master: " + e.getMessage());
            e.printStackTrace();
             System.exit(1);
        }
        logger.info("Master process finishing."); // Should only be reached after server stops
    }
}