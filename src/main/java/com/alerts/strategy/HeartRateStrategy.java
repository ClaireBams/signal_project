package com.alerts.strategy;

import com.alerts.Alert;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Strategy for monitoring heart rate data and triggering alerts:
 * - High Heart Rate Alert (above 120 bpm)
 * - Low Heart Rate Alert (below 50 bpm)
 */
public class HeartRateStrategy implements AlertStrategy {

    private static final long ONE_DAY_MS = 24 * 60 * 60 * 1000;

    @Override
    public void checkAlert(Patient patient) {
        long now = System.currentTimeMillis();
        long oneDayAgo = now - ONE_DAY_MS;

        List<PatientRecord> allRecords = patient.getRecords(oneDayAgo, now);
        List<PatientRecord> heartRateRecords = filterRecordsByType(allRecords, "HeartRate");

        checkHighHeartRate(heartRateRecords);
        checkLowHeartRate(heartRateRecords);
    }

    private void checkHighHeartRate(List<PatientRecord> records) {
        for (PatientRecord record : records) {
            if (record.getMeasurementValue() > 120) {
                triggerAlert(record.getPatientId(), "High Heart Rate Alert", record.getTimestamp());
            }
        }
    }

    private void checkLowHeartRate(List<PatientRecord> records) {
        for (PatientRecord record : records) {
            if (record.getMeasurementValue() < 50) {
                triggerAlert(record.getPatientId(), "Low Heart Rate Alert", record.getTimestamp());
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