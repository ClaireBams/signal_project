package com.cardio_generator.outputs;

import org.java_websocket.WebSocket;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

/**
 * This class implements the OutputStrategy interface and provides a WebSocket-based output mechanism.
 * It starts a WebSocket server and broadcasts patient data messages to all connected WebSocket clients.
 */
public class WebSocketOutputStrategy implements OutputStrategy {

    private WebSocketServer server;

    /**
     * Constructs the WebSocketOutputStrategy and starts a WebSocket server on the given port.
     *
     * @param port the port number to run the WebSocket server on
     */
    public WebSocketOutputStrategy(int port) {
        server = new SimpleWebSocketServer(new InetSocketAddress(port));
        System.out.println("WebSocket server created on port: " + port + ", listening for connections...");
        server.start();
    }

    /**
     * Sends patient data as a formatted string to all connected WebSocket clients.
     *
     * @param patientId the ID of the patient
     * @param timestamp the time the data was recorded
     * @param label     the type of data (e.g., HeartRate, ECG)
     * @param data      the actual measurement value
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
        // Broadcast the message to all connected clients
        for (WebSocket conn : server.getConnections()) {
            conn.send(message);
        }
    }

    /**
     * Inner class that extends WebSocketServer to handle client events.
     */
    private static class SimpleWebSocketServer extends WebSocketServer {


        /**
         * Constructs the WebSocket server on the given address.
         *
         * @param address the address and port to bind the server to
         */
        public SimpleWebSocketServer(InetSocketAddress address) {
            super(address);
        }

        /**
         * Called when a new client connects to the server.
         *
         * @param conn      the WebSocket connection object
         * @param handshake the handshake data
         */
        @Override
        public void onOpen(WebSocket conn, org.java_websocket.handshake.ClientHandshake handshake) {
            System.out.println("New connection: " + conn.getRemoteSocketAddress());
        }

        /**
         * Called when a client disconnects from the server.
         *
         * @param conn   the WebSocket connection object
         * @param code   the closure code
         * @param reason the reason for disconnection
         * @param remote true if the disconnection was initiated by the remote client
         */
        @Override
        public void onClose(WebSocket conn, int code, String reason, boolean remote) {
            System.out.println("Closed connection: " + conn.getRemoteSocketAddress());
        }

        /**
         * Called when a message is received from a client.
         * (Not used in this implementation since server only sends messages.)
         *
         * @param conn    the connection that sent the message
         * @param message the message received
         */
        @Override
        public void onMessage(WebSocket conn, String message) {
            // Not used in this context
        }

        /**
         * Called when an error occurs on the server or a connection.
         *
         * @param conn the connection where the error occurred (may be null)
         * @param ex   the exception that was thrown
         */
        @Override
        public void onError(WebSocket conn, Exception ex) {
            ex.printStackTrace();
        }

        /**
         * Called when the server successfully starts.
         */
        @Override
        public void onStart() {
            System.out.println("Server started successfully");
        }
    }
}
