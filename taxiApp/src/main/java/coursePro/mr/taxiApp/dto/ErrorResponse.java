package coursePro.mr.taxiApp.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    
    // Constructeurs
    public ErrorResponse() {
    }
    
    public ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }
    
    // Getters et Setters
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public int getStatus() {
        return status;
    }
    
    public void setStatus(int status) {
        this.status = status;
    }
    
    public String getError() {
        return error;
    }
    
    public void setError(String error) {
        this.error = error;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getPath() {
        return path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }
    
    // Builder Pattern
    public static Builder builder() {
        return new Builder();
    }
    
    public static class Builder {
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
        private String path;
        
        public Builder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }
        
        public Builder status(int status) {
            this.status = status;
            return this;
        }
        
        public Builder error(String error) {
            this.error = error;
            return this;
        }
        
        public Builder message(String message) {
            this.message = message;
            return this;
        }
        
        public Builder path(String path) {
            this.path = path;
            return this;
        }
        
        public ErrorResponse build() {
            ErrorResponse errorResponse = new ErrorResponse();
            errorResponse.setTimestamp(this.timestamp);
            errorResponse.setStatus(this.status);
            errorResponse.setError(this.error);
            errorResponse.setMessage(this.message);
            errorResponse.setPath(this.path);
            return errorResponse;
        }
    }
    
    // equals, hashCode et toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ErrorResponse that = (ErrorResponse) o;
        return status == that.status &&
               Objects.equals(timestamp, that.timestamp) &&
               Objects.equals(error, that.error) &&
               Objects.equals(message, that.message) &&
               Objects.equals(path, that.path);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(timestamp, status, error, message, path);
    }
    
    @Override
    public String toString() {
        return "ErrorResponse{" +
                "timestamp=" + timestamp +
                ", status=" + status +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
