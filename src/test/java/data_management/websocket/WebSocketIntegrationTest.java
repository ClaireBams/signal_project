package data_management.websocket;

import com.alerts.AlertGenerator;
import com.cardio_generator.SimpleVersWebSocket;
import com.data_management.DataStorage;
import com.data_management.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

public class WebSocketIntegrationTest {

    private DataStorage storage;
    private AlertGenerator alertGenerator;

    @BeforeEach
    public void setup() {
        storage = new DataStorage();
        DataStorage.setInstance(storage);
        alertGenerator = new AlertGenerator(storage);
    }

    @Test
    public void testAlertTriggeringAfterDataIngestion() {
        SimpleVersWebSocket client = new SimpleVersWebSocket(URI.create("ws://localhost:8887"));
        client.onMessage("1, 1744113766180, SystolicPressure, 85.0");
        client.onMessage("1, 1744113766181, Saturation, 89.0");

        Patient p = storage.getPatient(1);
        assertNotNull(p);
        assertDoesNotThrow(() -> alertGenerator.evaluateData(p));
    }
}