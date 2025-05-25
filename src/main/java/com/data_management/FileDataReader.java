package com.data_management;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileDataReader implements DataReader {

    private final String path;

    public FileDataReader(String path) {
        this.path = path;
    }

    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        FileReader fileReader = new FileReader(path);
        BufferedReader reader = new BufferedReader(fileReader);

        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            int patientId = Integer.parseInt(parts[0].split(": ")[1]);
            long patientTimestamp = Long.parseLong(parts[1].split(": ")[1]);
            String patientLabel = parts[2].split(": ")[1];
            double patientData;
            if (patientLabel.equals("Saturation")) {
                String s = parts[3].split(": ")[1];
                patientData = Double.parseDouble(s.substring(0, s.length() - 1));
            } else {
                patientData = Double.parseDouble(parts[3].split(": ")[1]);
            }
            dataStorage.addPatientData(patientId, patientData, patientLabel, patientTimestamp);
        }
    }
}
