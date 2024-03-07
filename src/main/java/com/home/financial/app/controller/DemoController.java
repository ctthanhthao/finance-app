package com.home.financial.app.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/demo-controller")
@OpenAPIDefinition(info = @Info(title = "Demo API", version = "v1"))
public class DemoController {

    @GetMapping
    public ResponseEntity sayHello() {
        return ResponseEntity.ok("Hello from secured endpoint");
    }
}
