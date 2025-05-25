package data_management.alert;

import com.alerts.AlertGenerator;
import com.data_management.InMemoryDataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class AlertGeneratorTest {

    private AlertGenerator alertGenerator;
    private InMemoryDataStorage dataStorage;
    private Patient patient;

    @BeforeEach
    void setUp() {
        dataStorage = new InMemoryDataStorage();
        alertGenerator = new AlertGenerator(dataStorage);
        patient = new Patient(1);
        dataStorage.addPatient(patient);
    }


    @Test
    void testSystolicPressureCriticalHighAlert() {
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(1, 190.0, "SystolicPressure", System.currentTimeMillis()));
        alertGenerator.bloodPressureCriticalThresholdAlert(records, "SystolicPressure");
    }

    @Test
    void testSystolicPressureCriticalLowAlert() {
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(1, 80.0, "SystolicPressure", System.currentTimeMillis()));
        alertGenerator.bloodPressureCriticalThresholdAlert(records, "SystolicPressure");
    }

    @Test
    void testDiastolicPressureCriticalHighAlert() {
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(1, 130.0, "DiastolicPressure", System.currentTimeMillis()));
        alertGenerator.bloodPressureCriticalThresholdAlert(records, "DiastolicPressure");
    }

    @Test
    void testDiastolicPressureCriticalLowAlert() {
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(1, 50.0, "DiastolicPressure", System.currentTimeMillis()));
        alertGenerator.bloodPressureCriticalThresholdAlert(records, "DiastolicPressure");
    }

    @Test
    void testNoAlertWhenValuesAreNormal() {
        List<PatientRecord> records = new ArrayList<>();
        records.add(new PatientRecord(1, 120.0, "SystolicPressure", System.currentTimeMillis()));
        records.add(new PatientRecord(1, 80.0, "DiastolicPressure", System.currentTimeMillis()));
        alertGenerator.bloodPressureCriticalThresholdAlert(records, "SystolicPressure");
        alertGenerator.bloodPressureCriticalThresholdAlert(records, "DiastolicPressure");
    }


    @Test
    void testEvaluateData_RapidSaturationDropAlert() {
        long now = System.currentTimeMillis();
        patient.addRecord(98.0, "Saturation", now);
        patient.addRecord(91.5, "Saturation", now + 5 * 60 * 1000); // drop in 5 mins
        alertGenerator.evaluateData(patient);
    }


    @Test
    void testEvaluateData_HypotensiveHypoxemiaAlert() {
        long now = System.currentTimeMillis();
        patient.addRecord(85.0, "SystolicPressure", now);
        patient.addRecord(89.0, "SystolicPressure", now + 1000);
        patient.addRecord(91.0, "Saturation", now + 2000);
        alertGenerator.evaluateData(patient);
    }


    @Test
    void testEvaluateData_ECGAbnormalDataAlert() {
        long now = System.currentTimeMillis();
        patient.addRecord(1.0, "ECG", now);
        patient.addRecord(1.0, "ECG", now + 1000);
        patient.addRecord(1.1, "ECG", now + 2000);
        patient.addRecord(0.9, "ECG", now + 3000);
        patient.addRecord(1.0, "ECG", now + 4000);
        patient.addRecord(2.0, "ECG", now + 5000); // spike
        alertGenerator.evaluateData(patient);
    }
}