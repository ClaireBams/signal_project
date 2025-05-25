package data_management.websocket;

import com.data_management.DataStorage;
import com.data_management.MyWebSocketDataReader;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class MyWebSocketDataReaderTest {

    @Test
    public void testMalformedWebSocketUrlThrowsIOException() {
        MyWebSocketDataReader reader = new MyWebSocketDataReader("::::");
        assertThrows(IOException.class, () -> reader.readData(new DataStorage()));
    }

    @Test
    public void testValidUrlDoesNotThrow() {
        // NOTE: This assumes a running WebSocket server at ws://localhost:8887
        // You can comment this out if no server is running
        /*
        MyWebSocketDataReader reader = new MyWebSocketDataReader("ws://localhost:8887");
        assertDoesNotThrow(() -> reader.readData(DataStorage.getInstance()));
        */
    }
}