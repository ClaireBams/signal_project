package data_management.websocket;

import com.cardio_generator.SimpleVersWebSocket;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

public class WebSocketErrorHandlingTest {

    @Test
    public void testOnErrorDoesNotCrash() throws Exception {
        SimpleVersWebSocket client = new SimpleVersWebSocket(new URI("ws://localhost:8887"));
        assertDoesNotThrow(() -> client.onError(new Exception("Simulated error")));
    }

    @Test
    public void testGracefulHandlingOfConnectionClose() throws Exception {
        SimpleVersWebSocket client = new SimpleVersWebSocket(new URI("ws://localhost:8887"));
        assertDoesNotThrow(() -> client.onClose(1000, "Test closure", true));
    }
}