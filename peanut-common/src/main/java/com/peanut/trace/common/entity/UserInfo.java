package com.peanut.trace.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户信息实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("user_info")
public class UserInfo extends BaseEntity {

    private String username;
    private String password;
    private String realName;
    private String phone;
    private String email;
    /** 角色: ADMIN / ENTERPRISE / SUPERVISOR / CONSUMER */
    private String role;
    private String enterpriseId;
    /** 状态: 1-正常, 0-禁用 */
    private Integer status;
    private java.time.LocalDateTime lastLogin;
}
