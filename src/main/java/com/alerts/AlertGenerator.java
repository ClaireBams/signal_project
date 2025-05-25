package com.alerts;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    public static final int MILLISECONDS_ONE_DAY = 86400000; // 86400000 is equivalent to the amount of milliseconds in one day. So we take all the data of the past day
    public static final int TEN_MIN_MS = 600000; // 86400000 is equivalent to the amount of milliseconds in one day. So we take all the data of the past day
    private DataStorage dataStorage;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        long endTime = System.currentTimeMillis();
        long startTime = System.currentTimeMillis() - MILLISECONDS_ONE_DAY;
        List<PatientRecord> records = patient.getRecords(startTime, endTime);

        List<PatientRecord> systolicRecords = filterRecordsByType(records, "SystolicPressure");
        List<PatientRecord> diastolicRecords = filterRecordsByType(records, "DiastolicPressure");

        bloodPressureAlert(systolicRecords, "SystolicPressure");
        bloodPressureAlert(diastolicRecords, "DiastolicPressure");

        List<PatientRecord> saturationRecords = filterRecordsByType(records, "Saturation");
        bloodSaturation(saturationRecords);

        hypotensiveHypoxemiaAlert(patient);

        List<PatientRecord> ecgRecords = filterRecordsByType(records, "ECG");
        ecgAlert(ecgRecords);
    }

    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        alert.triggerAlert();
    }

    public List<PatientRecord> filterRecordsByType(List<PatientRecord> records, String type) {
        List<PatientRecord> filterRecord = new ArrayList<>();

        for (int i = 0; i < records.size(); i++) {
            PatientRecord record = records.get(i);
            if (record.getRecordType().equals(type)) {
                filterRecord.add(record);
            }
        }
        return filterRecord;
    }

    public void bloodPressureAlert(List<PatientRecord> records, String type) {
        bloodPressureTrendAlert(records, type);
        bloodPressureCriticalThresholdAlert(records, type);
    }

    public void bloodPressureTrendAlert(List<PatientRecord> records, String type) {
        if (records.size() < 3) {
            return;
        }
        int consecutiveTrend = 0;
        boolean increase = false;
        boolean decrease = false;
        for (int i = 1; i < records.size(); i++) {
            double previousValue = records.get(i - 1).getMeasurementValue();
            double currentValue = records.get(i).getMeasurementValue();
            double valueChange = currentValue - previousValue;

            if (valueChange >= 10) {
                if (increase) {
                    consecutiveTrend++;
                } else {
                    increase = true;
                    decrease = false;
                    consecutiveTrend = 1;
                }

            } else if (valueChange <= -10) {
                if (decrease) {
                    consecutiveTrend++;
                } else {
                    consecutiveTrend = 1;
                    increase = false;
                    decrease = true;
                }
            } else {
                consecutiveTrend = 0;
                increase = false;
                decrease = false;
            }

            if (consecutiveTrend >= 2) {
                Alert alert = new Alert(String.valueOf(records.get(i).getPatientId()), type + " Trend Alert " + (increase ? "increase" : "decrease"), records.get(i).getTimestamp());
                triggerAlert(alert);
            }
        }
    }

    public void bloodPressureCriticalThresholdAlert(List<PatientRecord> records, String type) {
        if (!records.isEmpty()) {
            String patientId = String.valueOf(records.get(0).getPatientId());


            for (PatientRecord record : records) {
                double value = record.getMeasurementValue();
                if (type.equals("DiastolicPressure")) {
                    if (value > 120 || value < 60) {
                        triggerAlert(new Alert(patientId, type + " Critical Threshold Alert", record.getTimestamp()));
                    }
                } else if (type.equals("SystolicPressure")) {
                    if (value > 180 || value < 90) {
                        triggerAlert(new Alert(patientId, type + " Critical Threshold Alert", record.getTimestamp()));
                    }
                }
            }
        }
    }

    public void bloodSaturation(List<PatientRecord> records) {
        bloodSaturationLowSaturationAlert(records);
        bloodSaturationRapidDropAlert(records);
    }

    public void bloodSaturationRapidDropAlert(List<PatientRecord> records) {
        if (!records.isEmpty()) {
            String patientId = String.valueOf(records.get(0).getPatientId());

            for (int i = 0; i < records.size(); i++) {
                for (int j = i; j < records.size(); j++) {
                    PatientRecord r1 = records.get(i);
                    PatientRecord r2 = records.get(j);
                    long time_diff = r2.getTimestamp() - r1.getTimestamp();
                    if (time_diff > TEN_MIN_MS) {
                        break;
                    } else {
                        double valDiff = r2.getMeasurementValue() - r1.getMeasurementValue();
                        if (valDiff < -5.0) {
                            triggerAlert(new Alert(patientId, "Rapid Drop of Blood Saturation", r2.getTimestamp()));
                        }
                    }
                }
            }
        }
    }

    public void bloodSaturationLowSaturationAlert(List<PatientRecord> records) {
        if (!records.isEmpty()) {
            String patientId = String.valueOf(records.get(0).getPatientId());
            for (PatientRecord record : records) {

                double value = record.getMeasurementValue();
                if (value < 92.0) {
                    triggerAlert(new Alert(patientId, "Low Saturation Alert", record.getTimestamp()));
                }
            }
        }
    }

    public void hypotensiveHypoxemiaAlert(Patient patient) {
        long endTime = System.currentTimeMillis();
        long startTime = System.currentTimeMillis() - MILLISECONDS_ONE_DAY;
        List<PatientRecord> records = patient.getRecords(startTime, endTime);

        List<PatientRecord> systolicRecords = filterRecordsByType(records, "SystolicPressure");
        List<PatientRecord> saturation = filterRecordsByType(records, "Saturation");

        for (int i = 0; i < systolicRecords.size(); i++) {
            PatientRecord systolicRecord = systolicRecords.get(i);
            if (systolicRecord.getMeasurementValue() > 90) {
                continue;
            }
            long currentTime = systolicRecord.getTimestamp();
            long nextTimeStamp = (i + 1 >= systolicRecords.size()) ? Long.MAX_VALUE : systolicRecords.get(i + 1).getTimestamp();
            for (int j = 0; j < saturation.size(); j++) {
                PatientRecord saturationRecord = saturation.get(j);
                if (saturationRecord.getTimestamp() > nextTimeStamp) {
                    break;
                } else if (saturationRecord.getTimestamp() < currentTime) {
                    continue;
                }

                if (saturationRecord.getMeasurementValue() < 92.0) {
                    String patientId = String.valueOf(records.get(0).getPatientId());
                    triggerAlert(new Alert(patientId, "Hypotensive Hypoxemia Alert", saturationRecord.getTimestamp()));
                }

            }
        }
    }

    public void ecgAlert(List<PatientRecord> records) {
        int windowSize = 5;
        double thresholdMult = 1.5;

        for (int i = 0; i < records.size() - windowSize; i++) {
            double valSum = 0;
            for (int j = 0; j < windowSize; j++) {
                valSum += records.get(i + j).getMeasurementValue();
            }
            double avg = valSum / windowSize;
            double nextPoint = records.get(i + windowSize).getMeasurementValue();
            if (nextPoint > thresholdMult * avg) {
                String patientId = String.valueOf(records.get(0).getPatientId());
                triggerAlert(new Alert(patientId, "ECG Irregularity", records.get(i + windowSize).getTimestamp()));
            }
        }
    }

    public void triggeredAlert(String patientId, long timeStamp) {
        triggerAlert(new Alert(patientId, "Alert was triggered by nurse or patient", timeStamp));
    }

}
