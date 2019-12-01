package com.example.usersapi.exception;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ControllerAdvice
public class MyExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(SignUpException.class)
  public ResponseEntity<ErrorResponse> handleSignupException(SignUpException e){
    List<String> details = Arrays.asList(e.getMessage());
    String causeMessage = (e.getCause() == null) ? "" : e.getMessage();
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.resolve(500), details, causeMessage);
    return new ResponseEntity<>(errorResponse, HttpStatus.resolve(500));
  }

  @ExceptionHandler(LoginException.class)
  public ResponseEntity<ErrorResponse> handleLoginException(LoginException e){
    List<String> details = Arrays.asList(e.getMessage());
    String causeMessage = (e.getCause() == null) ? "" : e.getMessage();
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.resolve(401), details, causeMessage);
    return new ResponseEntity<>(errorResponse, HttpStatus.resolve(401));
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
      HttpHeaders headers, HttpStatus status,
      WebRequest request) {

    List<String> details = new ArrayList<>();
    for(ObjectError error : ex.getBindingResult().getAllErrors()) {
      details.add(error.getDefaultMessage());
    }
    ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST, details);

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

}

class ErrorResponse {

  private HttpStatus httpStatus;
  private List<String> message;
  private String cause;
  private String timestamp;

  public ErrorResponse(HttpStatus httpStatus, List<String> message, String cause) {
    super();
    this.message = message;
    this.httpStatus = httpStatus;
    this.cause = cause;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    this.timestamp = LocalDateTime.now().format(formatter);
  }

  public ErrorResponse(HttpStatus httpStatus, List<String> message) {
    super();
    this.httpStatus = httpStatus;
    this.message = message;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    this.timestamp = LocalDateTime.now().format(formatter);
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }

  public List<String> getMessage() {
    return message;
  }

  public String getCause() {
    return cause;
  }

  public String getTimestamp() {
    return timestamp;
  }
}
