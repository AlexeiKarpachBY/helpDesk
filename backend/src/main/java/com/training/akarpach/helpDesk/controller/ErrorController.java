package com.training.akarpach.helpDesk.controller;

import com.training.akarpach.helpDesk.exception.UserNotFoundException;
import com.training.akarpach.helpDesk.exception.WrongDesiredDateException;
import com.training.akarpach.helpDesk.exception.WrongFileFormatException;
import com.training.akarpach.helpDesk.exception.WrongFileSizeException;
import com.training.akarpach.helpDesk.model.ErrorModel;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ErrorController {

    @ExceptionHandler(EntityNotFoundException.class)
    private ResponseEntity<ErrorModel> handleEntityNotFound(EntityNotFoundException ex) {

        ErrorModel error = new ErrorModel(BAD_REQUEST, "Entity not found", ex.getMessage());

        return new ResponseEntity<>(error, BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    private ResponseEntity<ErrorModel> handle404NotFound(NoHandlerFoundException ex) {

        ErrorModel error = new ErrorModel(NOT_FOUND, "404", ex.getMessage());

        return new ResponseEntity<>(error, NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    private ResponseEntity<ErrorModel> handleRuntimeException(RuntimeException ex) {

        ErrorModel error = new ErrorModel(INTERNAL_SERVER_ERROR, ex.getMessage(), ex.toString());

        return new ResponseEntity<>(error, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserNotFoundException.class)
    private ResponseEntity<ErrorModel> handleUserNotFoundException(RuntimeException ex) {

        ErrorModel error = new ErrorModel(UNAUTHORIZED, ex.getMessage(), ex.toString());

        return new ResponseEntity<>(error, UNAUTHORIZED);
    }

    @ExceptionHandler(WrongDesiredDateException.class)
    private ResponseEntity<ErrorModel> handleWrongDateException(RuntimeException ex) {

        ErrorModel error = new ErrorModel(BAD_REQUEST, ex.getMessage(), ex.toString());

        return new ResponseEntity<>(error, BAD_REQUEST);
    }

    @ExceptionHandler(WrongFileFormatException.class)
    private ResponseEntity<ErrorModel> handleWrongFileFormatException(RuntimeException ex) {

        ErrorModel error = new ErrorModel(BAD_REQUEST, ex.getMessage(), ex.toString());

        return new ResponseEntity<>(error, BAD_REQUEST);
    }

    @ExceptionHandler(WrongFileSizeException.class)
    private ResponseEntity<ErrorModel> handleWrongFileSizeException(RuntimeException ex) {

        ErrorModel error = new ErrorModel(BAD_REQUEST, ex.getMessage(), ex.toString());

        return new ResponseEntity<>(error, BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ErrorModel> handleValidationException(MethodArgumentNotValidException ex) {

        ErrorModel error = new ErrorModel();
        for (ObjectError e : ex.getBindingResult().getAllErrors()) {
            error.setMessage(error.getMessage().concat(" ").concat(Objects.requireNonNull(e.getDefaultMessage())));
        }
        error.setHttpStatus(BAD_REQUEST);
        error.setDetails(error.getDetails());

        return ResponseEntity.status(BAD_REQUEST).body(error);
    }

}
