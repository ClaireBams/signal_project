package com.data_management;

import com.data_management.DataStorage;
import com.data_management.Patient;

import java.util.ArrayList;
import java.util.List;

public class InMemoryDataStorage extends DataStorage {
    private final List<Patient> patients = new ArrayList<>();

    @Override
    public void addPatientData(int patientId, double measurementValue, String recordType, long timestamp) {
        Patient patient = getPatient(patientId);
        if (patient == null) {
            patient = new Patient(patientId);
            patients.add(patient);
        }
        patient.addRecord(measurementValue, recordType, timestamp);
    }

    @Override
    public Patient getPatient(int id) {
        for (Patient patient : patients) {
            if (patient.getPatientId() == id) {
                return patient;
            }
        }
        return null;
    }

    @Override
    public List<Patient> getAllPatients() {
        return patients;
    }

    public void addPatient(Patient patient) {
        if (getPatient(patient.getPatientId()) == null) {
            patients.add(patient);
        }
    }
}


//public class InMemoryDataStorage extends DataStorage {
//    public InMemoryDataStorage() {
//        super();
//    }
//}