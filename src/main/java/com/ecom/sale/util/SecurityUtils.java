package com.ecom.sale.util;

import com.ecom.sale.enums.Role;
import com.ecom.sale.exception.CustomException;
import com.ecom.sale.model.User;
import com.ecom.sale.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final UserRepository userRepository;

    public User getCurrentUser(String apiPath) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomException(
                    apiPath, HttpStatus.UNAUTHORIZED,
                    "User is not authenticated", LocalDateTime.now()
            );
        }
        var principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new CustomException(
                            apiPath, HttpStatus.NOT_FOUND,
                            "User not found in database", LocalDateTime.now()
                    ));
        }
        throw new CustomException(
                apiPath, HttpStatus.BAD_REQUEST,
                "Principal is not an instance of UserDetails", LocalDateTime.now()
        );
    }

    public boolean hasRole(String roleName, String apiPath) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomException(
                    apiPath, HttpStatus.UNAUTHORIZED,
                    "User is not authenticated", LocalDateTime.now()
            );
        }
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(roleName));
    }

    public String getCurrentUsername(String apiPath) {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new CustomException(
                    apiPath, HttpStatus.UNAUTHORIZED,
                    "User is not authenticated", LocalDateTime.now()
            );
        }
        var principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }
        if (principal instanceof String username) {
            return username;
        }
        throw new CustomException(
                apiPath, HttpStatus.BAD_REQUEST,
                "Principal type is not supported", LocalDateTime.now()
        );
    }

    public void validateAccess(User currentUser, Long id, String api) {
        if (currentUser == null) {
            throw exception("User not authenticated", HttpStatus.UNAUTHORIZED, api);
        }
        if (!hasRole(Role.ROLE_ADMIN.name(), api) && !currentUser.getId().equals(id)) {
            throw exception("Access denied to update profile", HttpStatus.FORBIDDEN, api);
        }
    }

    public void hasPermission(User currentUser, User user, String api) {
        if (!currentUser.getId().equals(user.getId())) {
            throw exception("You do not have permission to do this", HttpStatus.FORBIDDEN,  api);
        }
    }

    private CustomException exception(String message, HttpStatus status, String api) {
        return new CustomException(api, status, message, LocalDateTime.now());
    }

    public boolean isAuthenticated() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }
}