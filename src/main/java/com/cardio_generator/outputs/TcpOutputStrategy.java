package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;


/**
 * An output strategy that sends patient data to a TCP client.
 * <p>
 * The server listens on a specific port and sends each message
 * to the first client that connects.
 */
public class TcpOutputStrategy implements OutputStrategy {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;

    /**
     * Starts a TCP server that listens for one client on the given port.
     * When a client connects, it will receive all patient data messages.
     *
     * @param port The port number to listen on (e.g. 8080).
     */
    public TcpOutputStrategy(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("TCP Server started on port " + port);

            // Accept clients in a new thread to not block the main thread
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    clientSocket = serverSocket.accept();
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends patient data to the connected TCP client.
     *
     * @param patientId  The ID of the patient.
     * @param timestamp  The time when the data was generated (in ms).
     * @param label      The type of data (e.g. "ECG", "Alert").
     * @param data       The actual value or message to send.
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (out != null) {
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
            out.println(message);
        }
    }
}
