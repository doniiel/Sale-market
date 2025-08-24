package com.ecom.sale.service.impl;

import com.ecom.sale.dto.AuthDto;
import com.ecom.sale.dto.request.ChangePasswordRequest;
import com.ecom.sale.dto.request.LoginRequest;
import com.ecom.sale.dto.request.RefreshTokenRequest;
import com.ecom.sale.dto.request.RegisterRequest;
import com.ecom.sale.enums.Role;
import com.ecom.sale.exception.CustomException;
import com.ecom.sale.model.RefreshToken;
import com.ecom.sale.model.User;
import com.ecom.sale.repository.RefreshTokenRepository;
import com.ecom.sale.repository.RoleRepository;
import com.ecom.sale.repository.UserRepository;
import com.ecom.sale.service.AuthService;
import com.ecom.sale.util.JwtUtils;
import com.ecom.sale.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final SecurityUtils securityUtils;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpirationInMs;

    private static final String API = "/auth";

    @Override
    @Transactional
    public AuthDto login(LoginRequest request) {
        final var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        var userDetails = (UserDetails) authentication.getPrincipal();
        var accessToken = jwtUtils.generateAccessToken(userDetails);
        var refreshToken = createAndSaveRefreshToken(userDetails);

        log.info("User {} logged in successfully", userDetails.getUsername());
        return AuthDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throwException(HttpStatus.BAD_REQUEST, "Username already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throwException(HttpStatus.BAD_REQUEST, "Email already taken");
        }

        var userRole = roleRepository.findByName(Role.ROLE_USER.name())
                .orElseThrow(() -> exception(HttpStatus.NOT_FOUND, "Default role not found"));

        var user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRoles(Set.of(userRole));

        userRepository.save(user);
        log.info("User {} registered successfully", user.getUsername());
    }

    @Override
    @Transactional
    public AuthDto refreshToken(RefreshTokenRequest request) {
        var refreshToken = request.getRefreshToken();
        var storedToken = refreshTokenRepository.findByToken(refreshToken)
                .orElseThrow(() -> exception(HttpStatus.UNAUTHORIZED, "Invalid refresh token"));

        if (storedToken.isRevoked() || storedToken.getExpiryDate().isBefore(now())) {
            throwException(HttpStatus.UNAUTHORIZED, "Refresh token is revoked or expired");
        }

        var userDetails = userRepository.findByUsername(jwtUtils.extractUsername(refreshToken, true))
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        user.getRoles().stream()
                                .map(role -> new org.springframework.security.core.authority.SimpleGrantedAuthority(role.getName()))
                                .toList()))
                .orElseThrow(() -> exception(HttpStatus.NOT_FOUND, "User not found"));

        if (!jwtUtils.isTokenValid(refreshToken, userDetails, true)) {
            throwException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }

        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);

        var newAccessToken = jwtUtils.generateAccessToken(userDetails);
        var newRefreshToken = createAndSaveRefreshToken(userDetails);

        log.info("Refreshed tokens for user {}", userDetails.getUsername());
        return AuthDto.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }


    @Override
    @Transactional
    public void changePassword(ChangePasswordRequest request) {
        var currentUser = securityUtils.getCurrentUser(API);

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(currentUser.getUsername(), request.getCurrentPassword())
            );
        } catch (BadCredentialsException e) {
            throwException(HttpStatus.UNAUTHORIZED, "Current password is incorrect");
        }

        currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(currentUser);

        refreshTokenRepository.deleteByUserId(currentUser.getId());
        log.info("Password changed successfully for user {}", currentUser.getUsername());
    }

    @Override
    @Transactional
    public void logout() {
        var currentUser = securityUtils.getCurrentUser(API);

        refreshTokenRepository.deleteByUserId(currentUser.getId());
        SecurityContextHolder.clearContext();

        log.info("User {} logged out successfully", currentUser.getUsername());
    }

    private String createAndSaveRefreshToken(UserDetails userDetails) {
        var user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> exception(HttpStatus.NOT_FOUND, "User not found"));

        refreshTokenRepository.deleteByUserId(user.getId());

        var refreshTokenValue = jwtUtils.generateRefreshToken(userDetails);

        var refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setToken(refreshTokenValue);
        refreshTokenEntity.setUser(user);
        refreshTokenEntity.setExpiryDate(now().plusSeconds(refreshTokenExpirationInMs / 1000));
        refreshTokenEntity.setRevoked(false);

        refreshTokenRepository.save(refreshTokenEntity);

        return refreshTokenValue;
    }


    private void throwException(HttpStatus status, String message) {
        throw exception(status, message);
    }

    private CustomException exception(HttpStatus status, String message) {
        return new CustomException(API, status, message, now());
    }

    private LocalDateTime now() {
        return LocalDateTime.now();
    }
}
