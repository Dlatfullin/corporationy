package kz.aitu.corporationy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Version")
@RestController
@RequestMapping("/api/version")
public class VersionController {

    @GetMapping
    @Operation(summary = "Application version")
    public String getVersion() {
        return "0.0.1";
    }
}