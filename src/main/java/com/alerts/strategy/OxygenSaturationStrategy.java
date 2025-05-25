package com.alerts.strategy;

import com.alerts.Alert;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Strategy for detecting oxygen saturation alerts.
 * Triggers:
 * - Low Saturation Alert (< 92%)
 * - Rapid Drop Alert (> 5% drop in 10 minutes)
 */
public class OxygenSaturationStrategy implements AlertStrategy {

    private static final long TEN_MINUTES_MS = 10 * 60 * 1000;

    @Override
    public void checkAlert(Patient patient) {
        long endTime = System.currentTimeMillis();
        long startTime = endTime - (24 * 60 * 60 * 1000); // Last 24 hours

        List<PatientRecord> allRecords = patient.getRecords(startTime, endTime);
        List<PatientRecord> saturationRecords = filterRecordsByType(allRecords, "Saturation");

        checkLowSaturation(saturationRecords);
        checkRapidDrop(saturationRecords);
    }

    private void checkLowSaturation(List<PatientRecord> records) {
        for (PatientRecord record : records) {
            if (record.getMeasurementValue() < 92.0) {
                triggerAlert(record.getPatientId(), "Low Saturation Alert", record.getTimestamp());
            }
        }
    }

    private void checkRapidDrop(List<PatientRecord> records) {
        for (int i = 0; i < records.size(); i++) {
            for (int j = i + 1; j < records.size(); j++) {
                long timeDiff = records.get(j).getTimestamp() - records.get(i).getTimestamp();
                if (timeDiff > TEN_MINUTES_MS) {
                    break;
                }

                double diff = records.get(j).getMeasurementValue() - records.get(i).getMeasurementValue();
                if (diff < -5.0) {
                    triggerAlert(records.get(j).getPatientId(), "Rapid Drop of Blood Saturation", records.get(j).getTimestamp());
                    break; // Optional: only trigger once per window
                }
            }
        }
    }

    private void triggerAlert(int patientId, String message, long timestamp) {
        Alert alert = new Alert(String.valueOf(patientId), message, timestamp);
        alert.triggerAlert();
    }

    private List<PatientRecord> filterRecordsByType(List<PatientRecord> records, String type) {
        List<PatientRecord> filtered = new ArrayList<>();
        for (PatientRecord record : records) {
            if (record.getRecordType().equals(type)) {
                filtered.add(record);
            }
        }
        return filtered;
    }
}