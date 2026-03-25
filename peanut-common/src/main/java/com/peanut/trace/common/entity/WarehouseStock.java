package com.peanut.trace.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 仓储信息实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("warehouse_stock")
public class WarehouseStock extends BaseEntity {
    private String stockId;
    private String batchNo;
    private String warehouseId;
    private String warehouseName;
    private LocalDateTime inTime;
    private LocalDateTime outTime;
    private BigDecimal quantityKg;
    private String storageLocation;
    private BigDecimal temperature;
    private BigDecimal humidity;
    private String dataHash;
}
