package com.peanut.trace.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 种植地块实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("field_info")
public class FieldInfo extends BaseEntity {
    private String fieldId;
    private String fieldNo;
    private String farmerId;
    private String cooperativeId;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal areaMu;
    private String soilType;
    private String variety;
    private LocalDate plantingDate;
    private LocalDate harvestDate;
    private String batchNo;
    private String pesticideInfo;   // JSON格式农药记录
    private String fertilizerInfo;  // JSON格式施肥记录
}
