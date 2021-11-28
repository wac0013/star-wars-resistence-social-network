package br.com.swrsn.exeption;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler {

    public static final String CONSTRAINT_VALIDATION_FAILED = "Constraint validation failed";

    @ExceptionHandler({ Throwable.class })
    public ResponseEntity<Object> handleException(Throwable eThrowable) {

        return DefaultControllerException.builder()
                .httpStatusCode(HttpStatus.INTERNAL_SERVER_ERROR)
                .message(Optional.ofNullable(eThrowable.getMessage()).orElse(eThrowable.toString()))
                .description(ExceptionResolver.getRootException(eThrowable))
                .build()
                .getResponse();

    }

    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException exMethod) {

        var error = exMethod.getName() + " should be " + Objects.requireNonNull(exMethod.getRequiredType()).getName();

        return DefaultControllerException.builder()
                .httpStatusCode(HttpStatus.BAD_REQUEST)
                .message(CONSTRAINT_VALIDATION_FAILED)
                .description(error)
                .build()
                .getResponse();
    }

    @ExceptionHandler({ ConstraintViolationException.class })
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException exMethod, WebRequest request) {
        var errors = new ArrayList<String>();

        for (var violation : exMethod.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": "
                    + violation.getMessage());
        }

        return DefaultControllerException.builder()
                .httpStatusCode(HttpStatus.BAD_REQUEST)
                .message(CONSTRAINT_VALIDATION_FAILED)
                .description(errors.toString())
                .build()
                .getResponse();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> validationError(MethodArgumentNotValidException exMethod) {

        var bindingResult = exMethod.getBindingResult();
        var fieldErrors = bindingResult.getFieldErrors();
        var fieldErrorDtos = fieldErrors.stream()
                .map(f -> f.getField()
                        .concat(":")
                        .concat(Objects.requireNonNullElse(f.getDefaultMessage(), "")))
                .map(String::new)
                .collect(Collectors.toList());

        return DefaultControllerException.builder()
                .httpStatusCode(HttpStatus.BAD_REQUEST)
                .message(CONSTRAINT_VALIDATION_FAILED)
                .description(fieldErrorDtos.toString())
                .build()
                .getResponse();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> validationError(HttpMessageNotReadableException exMethod) {

        var mostSpecificCause = exMethod.getMostSpecificCause();
        return DefaultControllerException.builder()
                .httpStatusCode(HttpStatus.BAD_REQUEST)
                .message(CONSTRAINT_VALIDATION_FAILED)
                .description(mostSpecificCause.getMessage())
                .build()
                .getResponse();
    }

    @ExceptionHandler({ MissingServletRequestParameterException.class })
    public ResponseEntity<Object> handleException(MissingServletRequestParameterException e) {

        return DefaultControllerException.builder()
                .httpStatusCode(HttpStatus.BAD_REQUEST)
                .message(Optional.ofNullable(e.getMessage()).orElse(e.toString()))
                .description(ExceptionResolver.getRootException(e))
                .build()
                .getResponse();

    }

    @ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
    public ResponseEntity<Object> handleException(HttpRequestMethodNotSupportedException e) {

        return DefaultControllerException.builder()
                .httpStatusCode(HttpStatus.METHOD_NOT_ALLOWED)
                .message(Optional.ofNullable(e.getMessage()).orElse(e.toString()))
                .description(ExceptionResolver.getRootException(e))
                .build()
                .getResponse();
    }
}
