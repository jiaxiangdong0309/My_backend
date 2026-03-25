package com.peanut.trace.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 农户信息实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("farmer_info")
public class FarmerInfo extends BaseEntity {
    private String farmerId;
    private String farmerName;
    private String phone;        // AES-256 加密存储
    private String idCard;       // AES-256 加密存储
    private String cooperativeId;
    private String region;
    private String address;
}
