// package coursePro.mr.taxiApp.utils;


// public class ResourceNotFoundException extends RuntimeException {
    
//     public ResourceNotFoundException() {
//         super();
//     }
    
//     public ResourceNotFoundException(String message) {
//         super(message);
//     }
    
//     public ResourceNotFoundException(String message, Throwable cause) {
//         super(message, cause);
//     }
    
//     public ResourceNotFoundException(Throwable cause) {
//         super(cause);
//     }
// }


// public class BusinessException extends RuntimeException {
    
//     public BusinessException() {
//         super();
//     }
    
//     public BusinessException(String message) {
//         super(message);
//     }
    
//     public BusinessException(String message, Throwable cause) {
//         super(message, cause);
//     }
    
//     public BusinessException(Throwable cause) {
//         super(cause);
//     }
// }


// import com.taxiapp.dto.ErrorResponse;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.validation.FieldError;
// import org.springframework.web.bind.MethodArgumentNotValidException;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.RestControllerAdvice;

// import javax.servlet.http.HttpServletRequest;
// import java.time.LocalDateTime;
// import java.util.HashMap;
// import java.util.Map;

// @RestControllerAdvice
// public class GlobalExceptionHandler {
    
//     private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
//     @ExceptionHandler(ResourceNotFoundException.class)
//     public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
//             ResourceNotFoundException ex, HttpServletRequest request) {
//         logger.error("Resource not found: {}", ex.getMessage());
        
//         ErrorResponse errorResponse = ErrorResponse.builder()
//                 .timestamp(LocalDateTime.now())
//                 .status(HttpStatus.NOT_FOUND.value())
//                 .error("Resource Not Found")
//                 .message(ex.getMessage())
//                 .path(request.getRequestURI())
//                 .build();
                
//         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
//     }
    
//     @ExceptionHandler(BusinessException.class)
//     public ResponseEntity<ErrorResponse> handleBusinessException(
//             BusinessException ex, HttpServletRequest request) {
//         logger.error("Business exception: {}", ex.getMessage());
        
//         ErrorResponse errorResponse = ErrorResponse.builder()
//                 .timestamp(LocalDateTime.now())
//                 .status(HttpStatus.BAD_REQUEST.value())
//                 .error("Business Error")
//                 .message(ex.getMessage())
//                 .path(request.getRequestURI())
//                 .build();
                
//         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
//     }
    
//     @ExceptionHandler(MethodArgumentNotValidException.class)
//     public ResponseEntity<ErrorResponse> handleValidationException(
//             MethodArgumentNotValidException ex, HttpServletRequest request) {
//         logger.error("Validation error: {}", ex.getMessage());
        
//         Map<String, String> errors = new HashMap<>();
//         ex.getBindingResult().getAllErrors().forEach((error) -> {
//             String fieldName = ((FieldError) error).getField();
//             String errorMessage = error.getDefaultMessage();
//             errors.put(fieldName, errorMessage);
//         });
        
//         ErrorResponse errorResponse = ErrorResponse.builder()
//                 .timestamp(LocalDateTime.now())
//                 .status(HttpStatus.BAD_REQUEST.value())
//                 .error("Validation Failed")
//                 .message("Erreurs de validation: " + errors.toString())
//                 .path(request.getRequestURI())
//                 .build();
                
//         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
//     }
    
//     @ExceptionHandler(IllegalArgumentException.class)
//     public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
//             IllegalArgumentException ex, HttpServletRequest request) {
//         logger.error("Illegal argument: {}", ex.getMessage());
        
//         ErrorResponse errorResponse = ErrorResponse.builder()
//                 .timestamp(LocalDateTime.now())
//                 .status(HttpStatus.BAD_REQUEST.value())
//                 .error("Invalid Argument")
//                 .message(ex.getMessage())
//                 .path(request.getRequestURI())
//                 .build();
                
//         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
//     }
    
//     @ExceptionHandler(Exception.class)
//     public ResponseEntity<ErrorResponse> handleGenericException(
//             Exception ex, HttpServletRequest request) {
//         logger.error("Unexpected error: ", ex);
        
//         ErrorResponse errorResponse = ErrorResponse.builder()
//                 .timestamp(LocalDateTime.now())
//                 .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
//                 .error("Internal Server Error")
//                 .message("Une erreur inattendue s'est produite")
//                 .path(request.getRequestURI())
//                 .build();
                
//         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//     }
// }
