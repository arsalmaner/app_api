package com.app_api.exception;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler
        extends ResponseEntityExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(value = {
            IllegalArgumentException.class,
            IllegalStateException.class,
            ResponseStatusException.class})
    protected @ResponseBody ResponseEntity<Object> handleConflict(
            ResponseStatusException ex) {
        JSONObject obj = new JSONObject();
        obj.put("detail", ex.getReason());
        Map<String, Object> responseBody = obj.toMap();
        logger.error(ex.getReason());
        return new ResponseEntity<>(responseBody, HttpStatus.NOT_ACCEPTABLE);
    }

}