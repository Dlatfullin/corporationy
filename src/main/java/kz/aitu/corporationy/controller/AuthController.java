package kz.aitu.corporationy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.aitu.corporationy.config.OpenApiConfig;
import kz.aitu.corporationy.dto.*;
import kz.aitu.corporationy.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication", description = "Endpoints for user login, registration, and retrieving profile information.")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Authenticate user and return token")
    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public GeneratedToken login(@RequestBody LoginRequest loginRequest) {
        return authService.login(loginRequest);
    }

    @Operation(summary = "Register a new user")
    @PostMapping(path = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> register(@RequestBody @Valid RegistrationRequest registrationRequest) {
        var user = authService.registration(registrationRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @Operation(summary = "Get authenticated user info",
    security = @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME))
    @GetMapping(path = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse info(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return authService.getAuthenticatedUser(authenticatedUser);
    }
}