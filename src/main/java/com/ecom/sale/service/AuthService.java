package com.ecom.sale.service;

import com.ecom.sale.dto.AuthDto;
import com.ecom.sale.dto.request.ChangePasswordRequest;
import com.ecom.sale.dto.request.LoginRequest;
import com.ecom.sale.dto.request.RefreshTokenRequest;
import com.ecom.sale.dto.request.RegisterRequest;

public interface AuthService {

    AuthDto login(LoginRequest request);

    void register(RegisterRequest request);

    AuthDto refreshToken(RefreshTokenRequest request);

    void changePassword(ChangePasswordRequest request);

    void logout();

}
