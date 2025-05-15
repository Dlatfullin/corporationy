package kz.aitu.corporationy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.aitu.corporationy.config.OpenApiConfig;
import kz.aitu.corporationy.dto.UserResponse;
import kz.aitu.corporationy.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "User")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @PageableAsQueryParam
    @Operation(summary = "Get All Users", security = @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME))
    public List<UserResponse> getUsers(@Parameter(hidden = true) Pageable pageable) {
        return userService.getUsers(pageable);
    }

    @Operation(summary = "Get user by id", security = @SecurityRequirement(name = OpenApiConfig.SECURITY_SCHEME_NAME))
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserResponse getUser(@PathVariable long id) {
        return userService.getUser(id);
    }
}