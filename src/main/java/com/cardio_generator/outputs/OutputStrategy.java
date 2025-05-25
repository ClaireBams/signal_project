package com.cardio_generator.outputs;


/**
 * A way to send out patient data.
 * <p>
 * Classes that use this interface decide how and where the data is sent,
 * like printing it to the screen, saving to a file, or sending over a network.
 * </p>
 */
public interface OutputStrategy {

    /**
     * Sends out one piece of patient data.
     *
     * @param patientId  The ID of the patient.
     * @param timestamp  When the data was created (in milliseconds).
     * @param label      The type of data (like "ECG" or "Alert").
     * @param data       The actual value or message.
     */
    void output(int patientId, long timestamp, String label, String data);
}
