package com.peanut.trace.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 销售流通实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sale_log")
public class SaleLog extends BaseEntity {
    private String saleId;
    private String batchNo;
    private String dealerId;
    private String dealerName;
    private LocalDateTime saleTime;
    private BigDecimal quantityKg;
    private String logisticsNo;
    private String carrier;
    private String destination;
    private String dataHash;
}
