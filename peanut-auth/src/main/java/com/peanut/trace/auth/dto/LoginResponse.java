package com.peanut.trace.auth.dto;

import lombok.Data;

/**
 * 登录响应 DTO
 */
@Data
public class LoginResponse {
    private String token;
    private String username;
    private String realName;
    private String role;
    private Long userId;
    private Long expireIn; // token有效期（秒）
}
