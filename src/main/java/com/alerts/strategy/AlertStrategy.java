package com.alerts.strategy;

import com.data_management.Patient;

public interface AlertStrategy {
    void checkAlert(Patient patient);
}
