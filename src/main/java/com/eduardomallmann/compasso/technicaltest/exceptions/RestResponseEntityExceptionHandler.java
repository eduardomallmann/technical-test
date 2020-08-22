package com.eduardomallmann.compasso.technicaltest.exceptions;

import com.eduardomallmann.compasso.technicaltest.utils.Response;
import com.eduardomallmann.compasso.technicaltest.utils.ResponseContent;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Class responsible for filtering the errors and response them in a global standard to the origin.
 *
 * @author eduardomallmann
 * @since 0.0.1
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles unique targetName constraint exception conflict response to the origin.
     *
     * @param ex exception thrown
     *
     * @return Error message to the origin.
     */
/*
    @ExceptionHandler(value = UniqueException.class)
    protected ResponseEntity<Response<ResponseContent>> handleUniqueNameConstraintConflict(final UniqueException ex) {
        return ResponseEntity.status(ex.getErrorMessage().getStatus())
                       .body(Response.of(ResponseContent.builder()
                                                 .status(HttpStatus.valueOf(ex.getErrorMessage().getStatus()).getReasonPhrase())
                                                 .errorMessage(ex.getErrorMessage())
                                                 .build()));
    }

    */
    /**
     * Handles object search not found exception conflict response to the origin.
     *
     * @param ex exception thrown
     *
     * @return Error message to the origin.
     */
/*

    @ExceptionHandler(value = ObjectNotFoundException.class)
    protected ResponseEntity<Response<ResponseContent>> handleObjectSearchNotFoundConflict(final ObjectNotFoundException ex) {
        return ResponseEntity.status(ex.getErrorMessage().getStatus())
                       .body(Response.of(ResponseContent.builder()
                                                 .status(HttpStatus.valueOf(ex.getErrorMessage().getStatus()).getReasonPhrase())
                                                 .errorMessage(ex.getErrorMessage())
                                                 .build()));
    }
*/

    /**
     * Handles the global exceptions.
     *
     * @param ex exception thrown
     *
     * @return the exceptions in a error message standard inside a response entity
     */
    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<Response<ResponseContent>> handleExceptions(final Exception ex) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setMessage(ex.getMessage());
        errorMessage.setStatus(HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(Response.of(ResponseContent.builder()
                                                        .status(HttpStatus.valueOf(errorMessage.getStatus()).getReasonPhrase())
                                                        .errorMessage(errorMessage)
                                                        .build()), HttpStatus.BAD_REQUEST);
    }
}
