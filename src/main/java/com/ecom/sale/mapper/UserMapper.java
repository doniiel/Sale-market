package com.ecom.sale.mapper;

import com.ecom.sale.dto.UserDto;
import com.ecom.sale.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDto(User user);

}
