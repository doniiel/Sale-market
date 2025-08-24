package com.ecom.sale.service;

import com.ecom.sale.dto.request.ChangePasswordRequest;
import com.ecom.sale.dto.request.LoginRequest;
import com.ecom.sale.dto.request.RefreshTokenRequest;
import com.ecom.sale.dto.request.RegisterRequest;

import java.util.Map;

public interface AuthService {

    Map<String, String> login(LoginRequest request);

    void register(RegisterRequest request);

    Map<String, String> refreshToken(RefreshTokenRequest request);

    void changePassword(ChangePasswordRequest request);

    void logout();

}
