package com.horizon.ecotrack.platform.analytics.application;

/**
 * Raised when a comparison or a report cannot be produced because the organization has no results to use.
 */
public class InsufficientDataException extends RuntimeException {
    public InsufficientDataException(String message) {
        super(message);
    }
}
