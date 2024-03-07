package com.home.financial.app.controller;

import com.home.financial.app.dto.*;
import com.home.financial.app.exception.ErrorResponse;
import com.home.financial.app.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;

import static com.home.financial.app.common.util.ConstantApiPath.USER_API;

@RestController
@RequestMapping(value=USER_API)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(
            description = "Create user.",
            responses = {
                    @ApiResponse(
                            description = "Successfully created.",
                            responseCode = "201",
                            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserRegisterResponse.class)) }
                    ),
                    @ApiResponse(
                            description = "Username could not be found.",
                            responseCode = "401",
                            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)) }
                    ),
                    @ApiResponse(
                            description = "User already exists.",
                            responseCode = "403",
                            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)) }
                    )
            }

    )
    @PostMapping(produces =  MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole(T(com.home.financial.app.domain.Role).ADMIN)")
    public ResponseEntity<UserDto> create(
            @RequestBody UserCreatedRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @Operation(
            description = "Get all users in the application.",
            responses = {
                    @ApiResponse(
                            description = "Successfully returned result.",
                            responseCode = "200",
                            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Page.class)) }
                    ),
                    @ApiResponse(
                            description = "User doesn't have this permission.",
                            responseCode = "403",
                            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)) }
                    )
            }

    )
    @GetMapping()
    @PreAuthorize("hasAnyRole(" +
            "T(com.home.financial.app.domain.Role).ADMIN," +
            "T(com.home.financial.app.domain.Role).MANAGER)")


    public ResponseEntity<Page<UserDto>> getAllUsers(
            @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize
    ) {
        return ResponseEntity.ok(userService.getAll(pageNum, pageSize));
    }

    @Operation(
            description = "Get user by username.",
            responses = {
                    @ApiResponse(
                            description = "Successfully returned result.",
                            responseCode = "200",
                            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Page.class)) }
                    ),
                    @ApiResponse(
                            description = "User doesn't have this permission.",
                            responseCode = "403",
                            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)) }
                    ),
                    @ApiResponse(
                            description = "Username could not be found",
                            responseCode = "401",
                            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)) }
                    )
            }

    )
    @GetMapping("/{username}")
    @PreAuthorize("hasAnyRole(" +
            "T(com.home.financial.app.domain.Role).ADMIN," +
            "T(com.home.financial.app.domain.Role).MANAGER," +
            "T(com.home.financial.app.domain.Role).USER)")
    public ResponseEntity<UserDto> getUser(
            @PathVariable(value = "username") String username
    ) {
        return ResponseEntity.ok(userService.getUser(username));
    }

    @Operation(
            description = "Update user. This can be done only by ADMIN or MANAGER",
            responses = {
                    @ApiResponse(
                            description = "Successfully updated.",
                            responseCode = "204"
                    ),
                    @ApiResponse(
                            description = "User doesn't have this permission.",
                            responseCode = "403",
                            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)) }
                    ),
                    @ApiResponse(
                            description = "Username could not be found.",
                            responseCode = "401",
                            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)) }
                    )
            }

    )
    @PutMapping("/{username}")
    @PreAuthorize("hasAnyRole(" +
            "T(com.home.financial.app.domain.Role).ADMIN," +
            "T(com.home.financial.app.domain.Role).MANAGER)")
    public ResponseEntity<Void> updateUser(
            @PathVariable("username") String username,
            @RequestBody UserUpdateRequest userUpdateRequest
    ) {
        userService.updateUser(username, userUpdateRequest);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            description = "Delete user.",
            responses = {
                    @ApiResponse(
                            description = "Successfully deleted.",
                            responseCode = "204"
                    ),
                    @ApiResponse(
                            description = "User doesn't have this permission.",
                            responseCode = "403",
                            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)) }
                    ),
                    @ApiResponse(
                            description = "Username could not be found.",
                            responseCode = "401",
                            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)) }
                    )
            }

    )
    @DeleteMapping("/{username}")
    @PreAuthorize("hasAnyRole(" +
            "T(com.home.financial.app.domain.Role).ADMIN)")
    public ResponseEntity<Void> deleteUser(
            @PathVariable(value = "username") String username
    ) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            description = "This method allows current user to change his/her password.",
            responses = {
                    @ApiResponse(
                            description = "Successfully updated.",
                            responseCode = "204"
                    ),
                    @ApiResponse(
                            description = "Old password is not correct.",
                            responseCode = "401",
                            content = { @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ErrorResponse.class)) }
                    )
            }

    )
    @PostMapping("/changePassword")
    public ResponseEntity<Void> changePassword(
            @RequestBody ChangePasswordRequest req
            ) {
        userService.changePassword(req);
        return ResponseEntity.noContent().build();
    }
}
