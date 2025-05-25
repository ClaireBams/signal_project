package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * A data generator that simulates alerts for patients.
 * <p>
 * Alerts can either be "triggered" or "resolved", based on a probability.
 * Alerts are rare and random, simulating real emergency conditions.
 */
public class AlertGenerator implements PatientDataGenerator {

    public static final Random randomGenerator = new Random();
    // baseDirectory --> Changed variable name to camelCase (it first was PascalCase)
    private boolean[] alertStates; // false = resolved, true = pressed

    /**
     * Creates a new alert generator for all patients.
     *
     * @param patientCount The total number of patients in the simulation.
     */
    public AlertGenerator(int patientCount) {
        alertStates = new boolean[patientCount + 1];
    }

    /**
     * Generates an alert for a specific patient.
     * <p>
     * If the patient already has an alert, there’s a 90% chance it will be resolved.
     * If there’s no alert, a new one might be triggered based on a small probability.
     *
     * @param patientId       The ID of the patient.
     * @param outputStrategy  The output method used to send the alert.
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {

        try {
            if (alertStates[patientId]) {
                if (randomGenerator.nextDouble() < 0.9) { // 90% chance to resolve
                    alertStates[patientId] = false;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                // lambda --> Changed variable name to camelCase (it first was PascalCase)
                double lambda = 0.1; // Average rate (alerts per period), adjust based on desired frequency
                double p = -Math.expm1(-lambda); // Probability of at least one alert in the period
                boolean alertTriggered = randomGenerator.nextDouble() < p;

                if (alertTriggered) {
                    alertStates[patientId] = true;
                    // Output the alert
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}
