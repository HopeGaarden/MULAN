package com.example.back.api.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "test!!";
    }

    @GetMapping("/auth/test")
    public String authTest() {
        return "test!!";
    }
    @PostMapping("/auth/test")
    public ResponseEntity<String> authTest(@RequestPart(name = "request") String request) {
        return ResponseEntity.ok(request);
    }
}
