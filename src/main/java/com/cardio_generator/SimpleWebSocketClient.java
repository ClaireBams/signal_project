//package com.cardio_generator;
//
//import com.data_management.DataStorage;
//import org.java_websocket.client.WebSocketClient;
//import org.java_websocket.handshake.ServerHandshake;
//
//import java.net.URI;
//
///**
// * A simple WebSocket client that connects to a WebSocket server and listens for patient data.
// * <p>
// * Message Format:
// * <pre>
// * patientId, timestamp, label, value
// * </pre>
// * Example:
// * <pre>
// * 12, 1744113766180, HeartRate, 85.0
// * </pre>
// * Parsed data is stored into the provided {@link DataStorage} instance.
// */
//public class SimpleWebSocketClient extends WebSocketClient {
//
//    private final DataStorage dataStorage;
//
//    /**
//     * Constructs a SimpleWebSocketClient with the specified server URI and data storage reference
//     *
//     * @param serverUri the URI of the WebSocket server to connect to
//     */
//    public SimpleWebSocketClient(URI serverUri) {
//        super(serverUri);
//        this.dataStorage = DataStorage.getInstance();
//    }
//
//    /**
//     * Called when the connection to the server is successfully opened.
//     */
//    @Override
//    public void onOpen(ServerHandshake handshake) {
//        System.out.println("Connected to WebSocket server");
//    }
//
//    /**
//     * Called when a message is received from the server.
//     * The message is parsed and, if valid, stored into the {@link DataStorage}.
//     *
//     * @param message the string received
//     */
//    @Override
//    public void onMessage(String message) {
//        System.out.println("Received: " + message);
//        try {
//            // splits message into parts based on commas
//            String[] parts = message.split(",", 4);
//            if (parts.length != 4) {
//                System.err.println("Invalid message format: " + message);
//                return;
//            }
//            // parses each part into respective variables
//            int patientId = Integer.parseInt(parts[0].trim());
//            long timestamp = Long.parseLong(parts[1].trim());
//            String label = parts[2].trim();
//            String rawValue = parts[3].trim().replace("%", "");
//            double measurementValue = Double.parseDouble(rawValue);
//            // adds the data to the storage
//            dataStorage.addPatientData(patientId, measurementValue, label, timestamp);
//        } catch (Exception e) {
//            System.err.println("Failed to parse message: " + message);
//            e.printStackTrace();
//        }
//    }
//
//
//    /**
//     * Called when the WebSocket connection is closed.
//     *
//     * @param code   the closure code
//     * @param reason the reason for closure
//     * @param remote true if the closure was initiated by the remote peer
//     */
//    @Override
//    public void onClose(int code, String reason, boolean remote) {
//        System.out.println("WebSocket connection closed: " + reason);
//    }
//
//    /**
//     * Called when an error occurs during communication with the WebSocket server.
//     *
//     * @param ex the exception that occurred
//     */
//    @Override
//    public void onError(Exception ex) {
//        System.err.println("WebSocket error:");
//        ex.printStackTrace();
//    }
//}

package com.cardio_generator;

import com.data_management.DataStorage;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Scanner;

/**
 * SimpleWebSocketClient connects to a WebSocket server to receive patient monitoring data.
 * It parses incoming messages and stores the data using the {@link DataStorage} singleton.
 *
 * <p>Expected message format: "patientId,timestamp,label,value"</p>
 * <p>Example: "12,1744113766180,HeartRate,85.0"</p>
 */
public class SimpleWebSocketClient extends WebSocketClient {

    private final DataStorage dataStorage;

    /**
     * Constructs a SimpleWebSocketClient with the given server URI.
     *
     * @param serverUri the URI of the WebSocket server (e.g. ws://localhost:8887)
     */
    public SimpleWebSocketClient(URI serverUri) {
        super(serverUri);
        this.dataStorage = DataStorage.getInstance();
    }

    /**
     * Called when the WebSocket connection is successfully established.
     *
     * @param handshake the server handshake data
     */
    @Override
    public void onOpen(ServerHandshake handshake) {
        System.out.println("[WebSocket] Connected to server: " + getURI());
    }

    /**
     * Called when a message is received from the server.
     * Parses the message and stores it if valid.
     *
     * @param message the raw message string in the format "patientId,timestamp,label,value"
     */
    @Override
    public void onMessage(String message) {
        System.out.println("[WebSocket] Received: " + message);

        if (!message.matches("\\d+\\s*,\\s*\\d+\\s*,\\s*[^,]+\\s*,\\s*[^,]+")) {
            System.out.println("[WebSocket] Skipping invalid message: " + message);
            return;
        }

        try {
            String[] parts = message.split(",", 4);

            int patientId = Integer.parseInt(parts[0].trim());
            long timestamp = Long.parseLong(parts[1].trim());
            String label = parts[2].trim();
            String valueStr = parts[3].trim().replace("%", "");
            double value = Double.parseDouble(valueStr);

            dataStorage.addPatientData(patientId, value, label, timestamp);

        } catch (NumberFormatException e) {
            System.err.println("[WebSocket] Error parsing number in message: " + message);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("[WebSocket] Unexpected error while processing message: " + message);
            e.printStackTrace();
        }
    }

    /**
     * Called when the WebSocket connection is closed.
     *
     * @param code   the status code
     * @param reason the reason for closure
     * @param remote true if closed remotely by the server
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.printf("[WebSocket] Connection closed (%s): %s%n", remote ? "remote" : "local", reason);
    }

    /**
     * Called when an error occurs during WebSocket communication.
     *
     * @param ex the exception that occurred
     */
    @Override
    public void onError(Exception ex) {
        System.err.println("[WebSocket] Error occurred:");
        ex.printStackTrace();
    }

    /**
     * Optional main method to run and test the WebSocket client interactively.
     *
     * @param args command-line arguments (not used)
     * @throws Exception if connection or URI initialization fails
     */
    public static void main(String[] args) throws Exception {
        URI serverUri = new URI("ws://localhost:8887");
        SimpleWebSocketClient client = new SimpleWebSocketClient(serverUri);
        client.connectBlocking();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Type a message to send (type 'exit' to quit):");

        while (true) {
            String input = scanner.nextLine();
            if ("exit".equalsIgnoreCase(input)) {
                break;
            }
            client.send(input);
        }

        client.close();
    }
}