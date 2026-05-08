package com.bootcamp.onlineschool.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Home Controller for root path
 */
@RestController
@RequestMapping("/")
public class HomeController {

    /**
     * Get welcome message
     */
    @GetMapping
    public ResponseEntity<String> home() {
        return ResponseEntity.ok("Welcome to Online School API. Available endpoints: /api/courses, /api/students");
    }
}