package com.ecom.sale.service;

import com.ecom.sale.dto.UserDto;
import com.ecom.sale.dto.UserSearchCriteria;
import com.ecom.sale.dto.request.UserUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserDto getUser(Long id);

    Page<UserDto> searchUser(UserSearchCriteria criteria, Pageable pageable);

    UserDto updateUser(Long id, UserUpdateRequest request);

    void deleteUser(Long id);
}
