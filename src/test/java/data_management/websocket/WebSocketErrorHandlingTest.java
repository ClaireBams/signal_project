package data_management.websocket;

import com.cardio_generator.SimpleWebSocketClient;
import com.data_management.DataStorage;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

public class WebSocketErrorHandlingTest {

    @Test
    public void testOnErrorDoesNotCrash() throws Exception {
        SimpleWebSocketClient client = new SimpleWebSocketClient(new URI("ws://localhost:8887"));
        assertDoesNotThrow(() -> client.onError(new Exception("Simulated error")));
    }

    @Test
    public void testGracefulHandlingOfConnectionClose() throws Exception {
        SimpleWebSocketClient client = new SimpleWebSocketClient(new URI("ws://localhost:8887"));
        assertDoesNotThrow(() -> client.onClose(1000, "Test closure", true));
    }
}