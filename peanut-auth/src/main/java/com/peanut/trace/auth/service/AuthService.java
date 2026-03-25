package com.peanut.trace.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.peanut.trace.auth.dto.LoginRequest;
import com.peanut.trace.auth.dto.LoginResponse;
import com.peanut.trace.common.entity.UserInfo;
import com.peanut.trace.common.exception.BusinessException;
import com.peanut.trace.common.mapper.UserInfoMapper;
import com.peanut.trace.common.result.ResultCode;
import com.peanut.trace.common.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 认证服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService extends ServiceImpl<UserInfoMapper, UserInfo> {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    /**
     * 用户登录
     */
    public LoginResponse login(LoginRequest req) {
        // 1. 查询用户
        UserInfo user = getOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getUsername, req.getUsername())
                .eq(UserInfo::getIsDeleted, 0));
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        // 2. 校验状态
        if (user.getStatus() == 0) {
            throw new BusinessException(ResultCode.USER_DISABLED);
        }
        // 3. 校验密码
        if (!passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.USER_PASSWORD_ERROR);
        }
        // 4. 生成 JWT
        String token = jwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
        // 5. 更新最后登录时间
        user.setLastLogin(LocalDateTime.now());
        updateById(user);

        log.info("用户登录成功: username={}, role={}", user.getUsername(), user.getRole());

        LoginResponse resp = new LoginResponse();
        resp.setToken(token);
        resp.setUsername(user.getUsername());
        resp.setRealName(user.getRealName());
        resp.setRole(user.getRole());
        resp.setUserId(user.getId());
        resp.setExpireIn(86400L);
        return resp;
    }

    /**
     * 根据用户名加载用户（供 Spring Security 使用）
     */
    public UserInfo loadByUsername(String username) {
        return getOne(new LambdaQueryWrapper<UserInfo>()
                .eq(UserInfo::getUsername, username)
                .eq(UserInfo::getIsDeleted, 0));
    }

    /**
     * 修改密码
     */
    public void changePassword(Long userId, String oldPwd, String newPwd) {
        UserInfo user = getById(userId);
        if (user == null) throw new BusinessException(ResultCode.USER_NOT_EXIST);
        if (!passwordEncoder.matches(oldPwd, user.getPassword())) {
            throw new BusinessException("原密码错误");
        }
        user.setPassword(passwordEncoder.encode(newPwd));
        updateById(user);
    }
}
