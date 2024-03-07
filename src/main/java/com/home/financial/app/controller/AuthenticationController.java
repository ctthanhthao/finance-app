package com.home.financial.app.controller;

import com.home.financial.app.dto.*;
import com.home.financial.app.exception.ErrorResponse;
import com.home.financial.app.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.home.financial.app.common.util.ConstantApiPath.AUTH_API;


@RestController
@RequestMapping(
        value = AUTH_API,
        produces = MediaType.APPLICATION_JSON_VALUE
)
@SecurityRequirement(name = "basicAuth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(
            description = "Register user.",
            summary = "User will provide some necessary information to register.",
            responses = {
                    @ApiResponse(
                            description = "Successfully registered.",
                            responseCode = "200",
                            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserRegisterRequest.class)) }
                    ),
                    @ApiResponse(
                            description = "User already exists.",
                            responseCode = "403",
                            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)) }
                    )
            }

    )
    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserRegisterResponse> register(
            @RequestBody UserRegisterRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @Operation(
            description = "This helps user to log in the application.",
            summary = "This endpoint will require username/password correctly to log in.",
            responses = {
                    @ApiResponse(
                            description = "Successfully log in.",
                            responseCode = "200",
                            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AuthenticationResponse.class)) }
                    ),
                    @ApiResponse(
                            description = "Bad credentials or username does not exist.",
                            responseCode = "401",
                            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)) }
                    )
            }

    )
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @Operation(
            description = "End authenticated user session.",
            summary = "This endpoint will log user out of the application",
            responses = {
                    @ApiResponse(
                            description = "Successfully log out.",
                            responseCode = "204"
                    )
            }

    )
    @GetMapping(value = "/logout")
    public ResponseEntity<Void> logout() {
        authenticationService.logout();
        return ResponseEntity.noContent().build();
    }
}
