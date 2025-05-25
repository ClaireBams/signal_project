package com.alerts.decorator;

import com.alerts.Alert;

/**
 * A concrete decorator that adds repetition tracking to an Alert.
 * <p>
 * This decorator enhances an alert by indicating how many times it has been repeated
 * and optionally simulating repeated alert checks over time.
 */
public class RepeatedAlertDecorator extends AlertDecorator {

    // Number of times this alert is considered to have been repeated
    private int repeatCount;

    /**
     * Constructs a RepeatedAlertDecorator.
     *
     * @param decoratedAlert the original Alert to decorate
     * @param repeatCount    the number of times the alert has been repeated
     */
    public RepeatedAlertDecorator(Alert decoratedAlert, int repeatCount) {
        super(decoratedAlert); // Initialize the base AlertDecorator
        this.repeatCount = repeatCount;
    }

    /**
     * Returns the original condition message with a repeated count appended.
     *
     * @return the decorated condition string
     */
    @Override
    public String getCondition() {
        return decoratedAlert.getCondition() + " (Repeated " + repeatCount + " times)";
    }

    /**
     * Simulates performing repeated checks for this alert over time.
     * Waits 1 second between checks and logs each check to the console.
     */
    public void repeatCheck() {
        for (int i = 0; i < repeatCount; i++) {
            // Simulate a delay for each repeat
            try {
                Thread.sleep(1000); // 1 second delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Log the repeated alert check with a timestamp
            System.out.println("Rechecking alert: " + getCondition() + " at " + System.currentTimeMillis());
        }
    }
}