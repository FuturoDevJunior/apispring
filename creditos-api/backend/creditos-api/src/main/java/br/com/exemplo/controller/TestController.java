package br.com.exemplo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        return "API funcionando!";
    }
    
    @GetMapping("/")
    public String root() {
        return "Creditos API v1.0.0";
    }
}
