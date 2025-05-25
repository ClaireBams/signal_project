package data_management.alert;

import com.alerts.AlertGenerator;
import com.alerts.strategy.*;
import com.data_management.InMemoryDataStorage;
import com.data_management.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class AlertStrategyTest {

    private InMemoryDataStorage dataStorage;
    private AlertGenerator generator;
    private Patient patient;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        dataStorage = new InMemoryDataStorage();
        generator = new AlertGenerator(dataStorage);
        patient = new Patient(1);
        dataStorage.addPatient(patient);

        // Redirect System.out to capture alert output
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    void testHeartRateStrategy_HighAndLowAlerts() {
        long now = System.currentTimeMillis();

        // Add low heart rate record (< 50 bpm)
        patient.addRecord(45.0, "HeartRate", now - 300_000); // 5 min ago

        // Add high heart rate record (> 120 bpm)
        patient.addRecord(125.0, "HeartRate", now - 100_000); // 2 min ago

        AlertStrategy strategy = new HeartRateStrategy();
        strategy.checkAlert(patient);

        String output = outputStream.toString();
        assertTrue(output.contains("Low Heart Rate Alert"), "Expected low heart rate alert");
        assertTrue(output.contains("High Heart Rate Alert"), "Expected high heart rate alert");
    }

    @Test
    void testHeartRateStrategy_NoAlert() {
        long now = System.currentTimeMillis();

        // Add normal heart rate records
        patient.addRecord(70.0, "HeartRate", now - 400_000); // 6 min ago
        patient.addRecord(80.0, "HeartRate", now - 200_000); // 3 min ago

        AlertStrategy strategy = new HeartRateStrategy();
        strategy.checkAlert(patient);

        String output = outputStream.toString();
        assertFalse(output.contains("Heart Rate Alert"), "No heart rate alerts should be triggered");
    }


    @Test
    void testOxygenSaturationStrategy_LowSaturationAndRapidDrop() {
        long now = System.currentTimeMillis();

        // Add low saturation record (below 92%)
        patient.addRecord(90.5, "Saturation", now - 5000);

        // Add saturation records with rapid drop (>5% in 10 mins)
        patient.addRecord(98.0, "Saturation", now - 600_000); // 10 min ago
        patient.addRecord(91.0, "Saturation", now - 300_000); // 5 min ago

        AlertStrategy strategy = new OxygenSaturationStrategy();
        strategy.checkAlert(patient);

        String output = outputStream.toString();

        assertTrue(output.contains("Low Saturation Alert"), "Expected low saturation alert");
        assertTrue(output.contains("Rapid Drop of Blood Saturation"), "Expected rapid drop alert");
    }

    @Test
    void testOxygenSaturationStrategy_NoAlert() {
        long now = System.currentTimeMillis();

        // All readings are normal and stable
        patient.addRecord(96.0, "Saturation", now - 500_000);
        patient.addRecord(95.0, "Saturation", now - 100_000);

        AlertStrategy strategy = new OxygenSaturationStrategy();
        strategy.checkAlert(patient);

        String output = outputStream.toString();
        assertFalse(output.contains("Alert"), "No alerts should be triggered");
    }
}