package com.ecom.sale.controller;

import com.ecom.sale.dto.UserDto;
import com.ecom.sale.dto.UserSearchCriteria;
import com.ecom.sale.dto.request.UserUpdateRequest;
import com.ecom.sale.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management API")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get user by ID", description = "Returns user details by given user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @Operation(summary = "Search users with filters", description = "Search users by username, email, phone or role with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search result",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/filter")
    public ResponseEntity<Page<UserDto>> getUsers(
            @ModelAttribute UserSearchCriteria criteria,
            @PageableDefault Pageable pageable
    ) {
        return ResponseEntity.ok(userService.searchUser(criteria, pageable));
    }

    @Operation(summary = "Update user", description = "Update email, phone or bio of a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long id,
            @RequestBody @Valid UserUpdateRequest request
    ) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @Operation(summary = "Delete user", description = "Deletes user by ID. Allowed only for admins.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "User ID", example = "1")
            @PathVariable Long id
    ) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

}
