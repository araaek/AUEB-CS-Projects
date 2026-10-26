package worker;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Worker {
    private static final Logger logger = Logger.getLogger(Worker.class.getName());
    private final int port;
    private final StoreRepository storeRepository;
    private ServerSocket serverSocket;
    private volatile boolean running = true; 

    private final List<Thread> masterHandlerThreads = Collections.synchronizedList(new ArrayList<>());


    public Worker(int port) {
        this.port = port;
        this.storeRepository = new StoreRepository();
         logger.info("Worker initialized. Store repository created.");
    }

    public void startServer() {
         try {
            serverSocket = new ServerSocket(port);
            logger.info("Worker server started on port: " + port);
            running = true;

            while (running) { 
                try {
                    Socket masterSocket = serverSocket.accept(); // Wait for Master connection
                    logger.info("Master connected: " + masterSocket.getInetAddress());

                    // Create a handler for the Master connection
                    MasterHandler handlerRunnable = new MasterHandler(masterSocket, storeRepository);
                    // Create and start a new Thread for the handler
                    Thread handlerThread = new Thread(handlerRunnable);
                    handlerThread.setName("MasterHandler-" + masterSocket.getInetAddress()); 
                    masterHandlerThreads.add(handlerThread); 
                    handlerThread.start();

                    masterHandlerThreads.removeIf(t -> !t.isAlive());

                } catch (IOException e) {
                    if (!running || serverSocket.isClosed()) {
                        logger.info("Worker server socket closed or shutting down, stopping accept loop.");
                        break; 
                    }
                    logger.log(Level.SEVERE, "Error accepting Master connection", e);
                }
            }
        } catch (IOException e) {
            if (running) {
                logger.log(Level.SEVERE, "Could not start worker server on port " + port, e);
            }
        } finally {
            if (!running) {
                logger.info("Worker accept loop finished due to shutdown signal.");
            }
            closeServerSocket(); 
        }
    }

     public void stopServer() {
        logger.info("Shutting down Worker server...");
        running = false; 
        closeServerSocket(); 

        logger.info("Interrupting active master handler threads...");
         synchronized (masterHandlerThreads) {
            for (Thread t : masterHandlerThreads) {
                if (t.isAlive()) {
                    t.interrupt();
                }
            }
         }
         

         logger.info("Worker server shut down signal sent.");
    }

    private void closeServerSocket() {
        if (serverSocket != null && !serverSocket.isClosed()) {
            try {
                serverSocket.close();
                logger.info("Worker server socket closed.");
            } catch (IOException e) {
                logger.log(Level.SEVERE, "Error closing worker server socket", e);
            }
        }
    }


    public static void main(String[] args) {
         if (args.length < 1) {
            System.err.println("Usage: java worker.Worker <port>");
            System.exit(1);
        }
         try {
            int port = Integer.parseInt(args[0]);
            Worker worker = new Worker(port);

            // Add shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                logger.info("Shutdown hook triggered. Stopping worker...");
                worker.stopServer();
                logger.info("Worker shutdown hook finished.");
            }));

            worker.startServer();

        } catch (NumberFormatException e) {
            System.err.println("Invalid port number: " + args[0]);
            System.exit(1);
        }
        logger.info("Worker process finishing.");
    }
}