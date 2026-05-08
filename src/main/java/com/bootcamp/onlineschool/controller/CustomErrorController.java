package com.bootcamp.onlineschool.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Error Controller for handling errors
 */
@RestController
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<String> handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        Exception exception = (Exception) request.getAttribute("jakarta.servlet.error.exception");

        if (statusCode == HttpStatus.NOT_FOUND.value()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("The requested resource was not found.");
        } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An internal server error occurred.");
        } else {
            return ResponseEntity.status(statusCode != null ? statusCode : 500)
                    .body("An error occurred.");
        }
    }
}