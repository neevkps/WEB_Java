package com.wearetrying.space_cats_market.service.exception;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.List;

@ControllerAdvice
public class AllExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex,
            WebRequest request
    ) {
        List<FieldError> errors = ex.getBindingResult().getFieldErrors();
        String detail = errors.isEmpty()
                ? "Validation failed."
                : errors.stream()
                .map(error -> String.format("Field '%s' %s", error.getField(), error.getDefaultMessage()))
                .reduce((a, b) -> a + "; " + b)
                .orElse("Validation failed.");

        ErrorResponse errorResponse = new ErrorResponse(
                "validation_error",
                "Bad Request",
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed for object '" + ex.getBindingResult().getObjectName() + "': " + detail,
                getRequestPath(request)
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // ValidationException
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleCustomValidation(
            ValidationException ex,
            WebRequest request
    ) {
        String detail = String.join("; ", ex.getValidationErrors());

        ErrorResponse errorResponse = new ErrorResponse(
                "validation_error",
                "Bad Request",
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed for fields: " + detail,
                getRequestPath(request)
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // ProductNotFoundException
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFound(
            ProductNotFoundException ex,
            WebRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse(
                "entity_not_found",
                "Not Found",
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                getRequestPath(request)
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(
            Exception ex,
            WebRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse(
                "internal_error",
                "Internal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                getRequestPath(request)
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    private String getRequestPath(WebRequest request) {
        String description = request.getDescription(false);
        return description.replace("uri=", "");
    }
}
