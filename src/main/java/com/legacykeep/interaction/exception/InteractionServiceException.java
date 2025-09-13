package com.legacykeep.interaction.exception;

/**
 * Interaction Service Exception
 * 
 * Base exception class for the Interaction Service.
 * Provides common exception handling for interaction-related operations.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
public class InteractionServiceException extends RuntimeException {
    
    private final String errorCode;
    private final String userMessage;
    
    public InteractionServiceException(String message) {
        super(message);
        this.errorCode = "INTERACTION_ERROR";
        this.userMessage = "An error occurred while processing your request";
    }
    
    public InteractionServiceException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "INTERACTION_ERROR";
        this.userMessage = "An error occurred while processing your request";
    }
    
    public InteractionServiceException(String errorCode, String message, String userMessage) {
        super(message);
        this.errorCode = errorCode;
        this.userMessage = userMessage;
    }
    
    public InteractionServiceException(String errorCode, String message, String userMessage, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.userMessage = userMessage;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getUserMessage() {
        return userMessage;
    }
}
