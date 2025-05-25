package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

/**
 * An output strategy that writes patient data to text files.
 * <p>
 * Each type of data (like heart rate or alerts) is saved to a separate file
 * inside a given folder.
 */
public class FileOutputStrategy implements OutputStrategy {

    // baseDirectory --> Changed variable name to camelCase (it first was PascalCase)
    private String baseDirectory;

    // fileMap --> Changed variable name to camelCase (it first was snake_case)
    public final ConcurrentHashMap<String, String> fileMap = new ConcurrentHashMap<>();

    public FileOutputStrategy(String baseDirectory) {

        this.baseDirectory = baseDirectory;
    }

    @Override

    /**
     * Saves one line of patient data to the right file.
     * If the file doesn't exist yet, it will be created.
     *
     * @param patientId  The ID of the patient.
     * @param timestamp  The time when the data was recorded.
     * @param label      The type of data (e.g. "ECG", "Alert").
     * @param data       The actual data value to save.
     */
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the FilePath variable
        // filePath --> Changed variable name to camelCase (it first was PascalCase)
        String filePath = fileMap.computeIfAbsent(label, k -> Paths.get(baseDirectory, label + ".txt").toString());

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(filePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + filePath + ": " + e.getMessage());
        }
    }
}