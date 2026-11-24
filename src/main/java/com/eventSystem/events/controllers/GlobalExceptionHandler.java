package com.eventSystem.events.controllers;

import com.eventSystem.events.domain.dtos.ErrorDto;
import com.eventSystem.events.exceptions.*;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<ErrorDto> handleTicketNotFoundException(TicketNotFoundException ex){
        log.error("Caught TicketNotFoundException:"+ex.getMessage());
        ErrorDto errorDto=new ErrorDto();
        errorDto.setErrorMessage("Ticket not found");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(TicketSoldOutException.class)
    public ResponseEntity<ErrorDto> handleTicketSoldOutException(TicketSoldOutException ex){
        log.error("Caught TicketSoldOutException:"+ex.getMessage());
        ErrorDto errorDto=new ErrorDto();
        errorDto.setErrorMessage("Tickets sold out");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(QrCodeNotFoundException.class)
    public ResponseEntity<ErrorDto> handleQrCodeNotFoundException(QrCodeNotFoundException ex){
        log.error("Caught QrCodeNotFoundException:"+ex.getMessage());
        ErrorDto errorDto=new ErrorDto();
        errorDto.setErrorMessage("Unable to find QR Code");
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(QrCodeGenerationException.class)
    public ResponseEntity<ErrorDto> handleQrCodeGenerationException(QrCodeGenerationException ex){
        log.error("Caught QrCodeGenerationException:"+ex.getMessage());
        ErrorDto errorDto=new ErrorDto();
        errorDto.setErrorMessage("Unable to generate QR Code");
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException(UserNotFoundException ex){
        log.error("Caught UserNotFoundException:"+ex.getMessage());
        ErrorDto errorDto=new ErrorDto();
        errorDto.setErrorMessage("User not found");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EventUpdateException.class)
    public ResponseEntity<ErrorDto> handleEventUpdateException(EventUpdateException ex){
        log.error("Caught EventUpdateException:"+ex.getMessage());
        ErrorDto errorDto=new ErrorDto();
        errorDto.setErrorMessage("Can't update event");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ErrorDto> handleEventNotFoundException(EventNotFoundException ex){
        log.error("Caught EventNotFoundException:"+ex.getMessage());
        ErrorDto errorDto=new ErrorDto();
        errorDto.setErrorMessage("Event not found");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TicketTypeNotFoundException.class)
    public ResponseEntity<ErrorDto> handleTicketTypeNotFoundException(TicketTypeNotFoundException ex){
        log.error("Caught TicketTypeNotFoundException:"+ex.getMessage());
        ErrorDto errorDto=new ErrorDto();
        errorDto.setErrorMessage("TicketType not found");
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        log.error("MethodArgumentNotValidException",ex);
        ErrorDto errorDto=new ErrorDto();

        BindingResult bindingResult=ex.getBindingResult();
        List<FieldError> fieldErrors=bindingResult.getFieldErrors();
        String errorMessage=fieldErrors.stream().findFirst().map(fieldError -> fieldError.getField()+":"+fieldError.getDefaultMessage()).orElse("Validation Failed");

        errorDto.setErrorMessage(errorMessage);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> handleConstraintViolation(ConstraintViolationException e)
    {
        log.error("ConstraintViolationException",e);
        ErrorDto errorDto=new ErrorDto();

        String violationStringError=e.getConstraintViolations().stream().findFirst().map(violation->violation.getPropertyPath()+":"+violation.getMessage()).orElse("ConstraintViolation occured");

        errorDto.setErrorMessage(violationStringError);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException(Exception e) {
        log.error(e.getMessage());
        ErrorDto errorDto=new ErrorDto();
        errorDto.setErrorMessage("An unknown error occured");
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
