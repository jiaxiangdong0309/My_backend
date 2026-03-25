package com.peanut.trace.auth.controller;

import com.peanut.trace.auth.dto.LoginRequest;
import com.peanut.trace.auth.dto.LoginResponse;
import com.peanut.trace.auth.service.AuthService;
import com.peanut.trace.common.result.R;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 认证接口
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 用户登录
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public R<LoginResponse> login(@Valid @RequestBody LoginRequest req) {
        return R.ok(authService.login(req));
    }

    /**
     * 用户登出（前端清除Token即可，服务端无状态）
     * POST /api/auth/logout
     */
    @PostMapping("/logout")
    public R<?> logout() {
        return R.ok("登出成功");
    }

    /**
     * 获取当前用户信息
     * GET /api/auth/me
     */
    @GetMapping("/me")
    public R<?> me(@RequestHeader("Authorization") String auth) {
        // Token 已由 JwtAuthFilter 解析，直接从 SecurityContext 获取
        org.springframework.security.core.Authentication authentication =
            org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();
        return R.ok(authentication.getName());
    }
}
