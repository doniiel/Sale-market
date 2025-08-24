package com.ecom.sale.service.impl;

import com.ecom.sale.dto.UserDto;
import com.ecom.sale.dto.UserSearchCriteria;
import com.ecom.sale.dto.request.UserUpdateRequest;
import com.ecom.sale.exception.CustomException;
import com.ecom.sale.mapper.UserMapper;
import com.ecom.sale.model.User;
import com.ecom.sale.repository.UserRepository;
import com.ecom.sale.repository.specification.builder.UserSpecificationBuilder;
import com.ecom.sale.service.UserService;
import com.ecom.sale.util.SecurityUtils;
import com.ecom.sale.util.UpdateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UpdateUtils updateUtils;
    private final SecurityUtils securityUtils;
    private final UserMapper userMapper;

    private static final String API = "/users";

    @Override
    @Transactional(readOnly = true)
    public UserDto getUser(Long id) {
        var user = getUserOrThrow(id);
        log.info("Fetched user: id={}", id);
        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> searchUser(UserSearchCriteria criteria, Pageable pageable) {
        var spec = new UserSpecificationBuilder()
                .withUsername(criteria.getUsername())
                .withEmail(criteria.getEmail())
                .withPhone(criteria.getPhone())
                .withRole(criteria.getRole())
                .build();

        var users = userRepository.findAll(spec, pageable).map(userMapper::toDto);
        log.info("Fetched {} users by search criteria", users.getTotalElements());
        return users;
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN') or #id == authentication.principal.id")
    public UserDto updateUser(Long id, UserUpdateRequest request) {
        var currentUser = securityUtils.getCurrentUser(API);
        securityUtils.validateAccess(currentUser, id, API);

        var user = getUserOrThrow(id);
        checkUnique(request, user);

        updateUtils.updateIfChanged(user::getEmail, user::setEmail, request.getEmail());
        updateUtils.updateIfChanged(user::getPhone, user::setPhone, request.getPhone());
        updateUtils.updateIfChanged(user::getBio, user::setBio, request.getBio());

        var updated = userRepository.save(user);
        log.info("Updated user: id={}", id);
        return userMapper.toDto(updated);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteUser(Long id) {
        var user = getUserOrThrow(id);
        userRepository.delete(user);
        log.info("Deleted user: id={}", id);
    }

    private User getUserOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> exception("User with id=" + id + " not found", HttpStatus.NOT_FOUND));
    }

    private void checkUnique(UserUpdateRequest request, User user) {
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw exception("Email " + request.getEmail() + " already in use", HttpStatus.BAD_REQUEST);
        }
        if (request.getPhone() != null && !request.getPhone().equals(user.getPhone())
                && userRepository.existsByPhone(request.getPhone())) {
            throw exception("Phone " + request.getPhone() + " already in use", HttpStatus.BAD_REQUEST);
        }
    }

    private CustomException exception(String message, HttpStatus status) {
        return new CustomException(API, status, message, LocalDateTime.now());
    }
}
