package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * An interface for generating simulated patient data.
 * <p>
 * Implementing classes should define how specific types of data
 * are generated and output for individual patients using the given output strategy.
 * </p>
 */
public interface PatientDataGenerator {

    /**
     * Generates and outputs data for a given patient.
     *
     * @param patientId       The unique ID of the patient.
     * @param outputStrategy  The output strategy used to send or store the generated data.
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
