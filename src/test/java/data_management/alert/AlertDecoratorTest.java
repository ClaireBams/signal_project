package data_management.alert;

import com.alerts.Alert;
import com.alerts.decorator.PriorityAlertDecorator;
import com.alerts.decorator.RepeatedAlertDecorator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlertDecoratorTest {

    @Test
    void testPriorityAlertDecoratorModifiesCondition() {
        Alert baseAlert = new Alert("123", "Low Oxygen", 1000L);
        Alert decorated = new PriorityAlertDecorator(baseAlert, "High");

        assertEquals("123", decorated.getPatientId());
        assertEquals("Low Oxygen - Priority: High", decorated.getCondition());
        assertEquals(1000L, decorated.getTimestamp());
    }

    @Test
    void testRepeatedAlertDecoratorModifiesCondition() {
        Alert baseAlert = new Alert("456", "ECG Spike", 2000L);
        Alert decorated = new RepeatedAlertDecorator(baseAlert, 3);

        assertEquals("456", decorated.getPatientId());
        assertEquals("ECG Spike (Repeated 3 times)", decorated.getCondition());
        assertEquals(2000L, decorated.getTimestamp());
    }

    @Test
    void testRepeatedAlertRepeatCheckDoesNotThrow() {
        Alert baseAlert = new Alert("789", "High BP", 3000L);
        RepeatedAlertDecorator decorator = new RepeatedAlertDecorator(baseAlert, 2);

        // Call repeatCheck() and just ensure it completes (can also capture output if needed)
        decorator.repeatCheck();
    }
}