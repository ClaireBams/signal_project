package com.alerts.strategy;

import com.alerts.AlertGenerator;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.List;

public class BloodPressureStrategy implements AlertStrategy {

    private AlertGenerator generator;

    /**
     * Constructor to pass the generator instance.
     *
     * @param generator the main alert generator to access helper methods
     */
    public BloodPressureStrategy(AlertGenerator generator) {
        this.generator = generator;
    }

    @Override
    public void checkAlert(Patient patient) {
        long endTime = System.currentTimeMillis();
        long startTime = endTime - AlertGenerator.MILLISECONDS_ONE_DAY;

        List<PatientRecord> records = patient.getRecords(startTime, endTime);

        List<PatientRecord> systolicRecords = generator.filterRecordsByType(records, "SystolicPressure");
        List<PatientRecord> diastolicRecords = generator.filterRecordsByType(records, "DiastolicPressure");

        generator.bloodPressureAlert(systolicRecords, "SystolicPressure");
        generator.bloodPressureAlert(diastolicRecords, "DiastolicPressure");
    }
}