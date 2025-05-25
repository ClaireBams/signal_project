package data_management.websocket;

import com.cardio_generator.SimpleVersWebSocket;
import com.data_management.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleWebSocketClientTest {

    private SimpleVersWebSocket client;
    private DataStorage storage;

    @BeforeEach
    public void setup() throws Exception {
        storage = new DataStorage();
        DataStorage.setInstance(storage);
        client = new SimpleVersWebSocket(new URI("ws://localhost:8887"));
    }

    @Test
    public void testValidMessageParsing() {
        String message = "1, 1744113766180, HeartRate, 85.0";
        client.onMessage(message);
        assertEquals(1, storage.getAllPatients().size());
        assertEquals(1, storage.getPatient(1).getRecords(0, Long.MAX_VALUE).size());
    }

    @Test
    public void testInvalidMessageFormat() {
        String message = "not,a,valid,message";
        client.onMessage(message);
        assertEquals(0, storage.getAllPatients().size());
    }

    @Test
    public void testNonNumericValue() {
        String message = "1, 1744113766180, HeartRate, eighty";
        client.onMessage(message);
        assertEquals(0, storage.getAllPatients().size());
    }
}