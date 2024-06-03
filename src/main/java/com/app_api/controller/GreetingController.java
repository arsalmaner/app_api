package com.app_api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
@RequestMapping("/api/v1/hello")
@RequiredArgsConstructor
public class GreetingController {

    @GetMapping
    String hello() {
        return "Timestamp : " + new Timestamp(System.currentTimeMillis());
    }
}
