package data_management;

import static org.junit.jupiter.api.Assertions.*;

import com.alerts.AlertGenerator;
import com.data_management.DataReader;
import com.data_management.Patient;
import org.junit.jupiter.api.Test;

import com.data_management.DataStorage;
import com.data_management.PatientRecord;

import java.util.List;

class DataStorageTest {

    @Test
    void testAddAndGetRecords() {
        DataStorage storage = new DataStorage();

        // Add two records for patient 1
        long ts1 = 1714376789050L;
        long ts2 = 1714376789051L;

        storage.addPatientData(1, 100.0, "WhiteBloodCells", ts1);
        storage.addPatientData(1, 200.0, "WhiteBloodCells", ts2);

        // Retrieve records within full range
        List<PatientRecord> records = storage.getRecords(1, ts1 - 1, ts2 + 1);

        assertEquals(2, records.size(), "Should retrieve 2 records");
        assertEquals(100.0, records.get(0).getMeasurementValue(), 0.001);
        assertEquals(200.0, records.get(1).getMeasurementValue(), 0.001);
    }

    @Test
    void testAlertEvaluationDoesNotCrash() {
        DataStorage storage = new DataStorage();
        storage.addPatientData(1, 90.0, "OxygenSaturation", System.currentTimeMillis());

        Patient patient = storage.getPatient(1);
        assertNotNull(patient);

        AlertGenerator generator = new AlertGenerator(storage);
        assertDoesNotThrow(() -> generator.evaluateData(patient));
    }


}


