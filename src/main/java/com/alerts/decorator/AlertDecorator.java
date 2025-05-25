package com.alerts.decorator;

import com.alerts.Alert;

/**
 * Abstract base class for decorators that extend the functionality of an Alert.
 * This follows the Decorator design pattern, allowing dynamic modification of
 * Alert behavior without modifying the original Alert class.
 */
public abstract class AlertDecorator extends Alert {
    protected Alert decoratedAlert;

    /**
     * Constructor for the AlertDecorator.
     * It initializes the decorator by copying the core alert properties
     * from the original Alert object.
     *
     * @param decoratedAlert the Alert instance to be decorated
     */
    public AlertDecorator(Alert decoratedAlert) {
        super(decoratedAlert.getPatientId(), decoratedAlert.getCondition(), decoratedAlert.getTimestamp());
        this.decoratedAlert = decoratedAlert;
    }

    /**
     * Returns the patient ID from the original (decorated) alert.
     *
     * @return the patient ID
     */
    @Override
    public String getPatientId() {
        return decoratedAlert.getPatientId();
    }

    /**
     * Returns the condition description from the original (decorated) alert.
     *
     * @return the alert condition string
     */
    @Override
    public String getCondition() {
        return decoratedAlert.getCondition();
    }

    /**
     * Returns the timestamp from the original (decorated) alert.
     *
     * @return the alert timestamp
     */
    @Override
    public long getTimestamp() {
        return decoratedAlert.getTimestamp();
    }
}
