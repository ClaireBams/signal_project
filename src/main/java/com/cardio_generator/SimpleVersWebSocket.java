package com.cardio_generator;

import com.data_management.DataStorage;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Scanner;

/**
 * PatientDataReceiver connects to a WebSocket server and listens for real-time
 * health data messages from patients. It parses each message and stores the data
 * into a singleton DataStorage instance.
 * <p>
 * Expected message format:
 * patientId,timestamp,label,value
 * Example:
 * 12,1744113766180,HeartRate,85.0
 * <p>
 * Notes:
 * - Assumes all messages follow the expected CSV format.
 * - Messages with missing or malformed data are safely ignored.
 */
public class SimpleVersWebSocket extends WebSocketClient {

    // Reference to the shared data storage singleton
    private final DataStorage storage = DataStorage.getInstance();

    /**
     * Constructs a WebSocket client for the given server URI.
     *
     * @param endpoint the URI of the WebSocket server (e.g. ws://localhost:8887)
     */
    public SimpleVersWebSocket(URI endpoint) {
        super(endpoint);
    }

    /**
     * Called once the connection to the WebSocket server is successfully opened.
     *
     * @param handshakeData server handshake information (unused here)
     */
    @Override
    public void onOpen(ServerHandshake handshakeData) {
        System.out.println(">> Connected to: " + getURI());
    }

    /**
     * Called when a message is received from the WebSocket server.
     * Delegates parsing and processing to a separate method.
     *
     * @param msg the received message string
     */
    @Override
    public void onMessage(String msg) {
        System.out.println(">> Incoming: " + msg);
        processMessage(msg); // Delegate to parsing method
    }

    /**
     * Parses and processes a message in CSV format.
     * Expected format: "patientId,timestamp,label,value"
     * If valid, stores the extracted data in the data storage.
     *
     * @param msg the raw message string
     */
    private void processMessage(String msg) {
        String[] tokens = msg.split(",", 4); // Limit to 4 parts to handle malformed input

        // Check for correct number of fields
        if (tokens.length != 4) {
            System.out.println("!! Invalid format: " + msg);
            return;
        }

        try {
            // Parse individual fields
            int id = Integer.parseInt(tokens[0].trim()); // patient ID
            long time = Long.parseLong(tokens[1].trim()); // timestamp
            String type = tokens[2].trim(); // measurement label (e.g. HeartRate)
            double val = Double.parseDouble(tokens[3].trim().replace("%", "")); // numeric value

            // Store data into the shared DataStorage instance
            storage.addPatientData(id, val, type, time);

        } catch (NumberFormatException nf) {
            // Handles invalid number formats (e.g. "NaN" or bad ID/timestamp)
            System.err.println("!! Number error in: " + msg);
            nf.printStackTrace();

        } catch (Exception ex) {
            // Catch-all for unexpected issues (e.g. nulls, array index issues)
            System.err.println("!! Unexpected error while handling message:");
            ex.printStackTrace();
        }
    }

    /**
     * Called when the WebSocket connection is closed.
     *
     * @param statusCode code indicating why connection was closed
     * @param reason     textual reason for closure
     * @param byRemote   true if server closed the connection
     */
    @Override
    public void onClose(int statusCode, String reason, boolean byRemote) {
        System.out.printf(">> Disconnected (%s): %s%n", byRemote ? "server" : "client", reason);
    }

    /**
     * Called if an exception occurs during WebSocket communication.
     *
     * @param error the exception that occurred
     */
    @Override
    public void onError(Exception error) {
        System.err.println("!! WebSocket error occurred:");
        error.printStackTrace();
    }

    /**
     * Optional: Main method to run the client directly from the console.
     * Connects to the server, allows sending manual messages, and exits on 'exit'.
     *
     * @param args unused command-line arguments
     * @throws Exception if URI is invalid or connection fails
     */
    public static void main(String[] args) throws Exception {
        URI server = new URI("ws://localhost:8887"); // Replace with your WebSocket server URI
        SimpleVersWebSocket client = new SimpleVersWebSocket(server);

        client.connectBlocking(); // Wait until connection is established

        // Allow user to type and send messages manually
        try (Scanner input = new Scanner(System.in)) {
            System.out.println("Enter text to send (type 'exit' to disconnect):");

            while (true) {
                String line = input.nextLine();
                if ("exit".equalsIgnoreCase(line)) break;
                client.send(line);
            }
        }

        // Clean shutdown
        client.close();
    }
}