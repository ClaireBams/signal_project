package com.alerts.decorator;

import com.alerts.Alert;

/**
 * A concrete decorator that adds priority information to an existing Alert.
 * <p>
 * This class extends AlertDecorator and appends a priority level (e.g., HIGH, MEDIUM, LOW)
 * to the alert condition text.
 */
public class PriorityAlertDecorator extends AlertDecorator {

    // The priority level to add (e.g., "HIGH", "MEDIUM", "LOW")
    private String priority;

    /**
     * Constructs a PriorityAlertDecorator.
     *
     * @param decoratedAlert the original Alert to decorate
     * @param priority       the priority level to add to the alert
     */
    public PriorityAlertDecorator(Alert decoratedAlert, String priority) {
        super(decoratedAlert); // Calls the constructor of AlertDecorator
        this.priority = priority;
    }

    /**
     * Returns the condition string with the priority appended.
     * Overrides the original getCondition method to enhance it.
     *
     * @return the condition with priority info added
     */
    @Override
    public String getCondition() {
        return decoratedAlert.getCondition() + " - Priority: " + priority;
    }
}