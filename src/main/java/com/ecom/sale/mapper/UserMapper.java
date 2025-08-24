package com.ecom.sale.mapper;

import com.ecom.sale.dto.UserDto;
import com.ecom.sale.model.User;

public interface UserMapper {

    UserDto toDto(User user);

}
