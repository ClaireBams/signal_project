package data_management.alert;

import com.alerts.Alert;
import com.alerts.factory.AlertFactory;
import com.alerts.factory.BloodOxygenAlertFactory;
import com.alerts.factory.BloodPressureAlertFactory;
import com.alerts.factory.ECGAlertFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlertFactoryTest {

    @Test
    void testBloodOxygenAlertFactoryCreatesCorrectAlert() {
        AlertFactory factory = new BloodOxygenAlertFactory();
        Alert alert = factory.createAlert("123", "Low Oxygen", 1000L);

        assertNotNull(alert);
        assertEquals("123", alert.getPatientId());
        assertEquals("Low Oxygen", alert.getCondition());
        assertEquals(1000L, alert.getTimestamp());
    }

    @Test
    void testBloodPressureAlertFactoryCreatesCorrectAlert() {
        AlertFactory factory = new BloodPressureAlertFactory();
        Alert alert = factory.createAlert("456", "High Blood Pressure", 2000L);

        assertNotNull(alert);
        assertEquals("456", alert.getPatientId());
        assertEquals("High Blood Pressure", alert.getCondition());
        assertEquals(2000L, alert.getTimestamp());
    }

    @Test
    void testECGAlertFactoryCreatesCorrectAlert() {
        AlertFactory factory = new ECGAlertFactory();
        Alert alert = factory.createAlert("789", "ECG Spike", 3000L);

        assertNotNull(alert);
        assertEquals("789", alert.getPatientId());
        assertEquals("ECG Spike", alert.getCondition());
        assertEquals(3000L, alert.getTimestamp());
    }
}