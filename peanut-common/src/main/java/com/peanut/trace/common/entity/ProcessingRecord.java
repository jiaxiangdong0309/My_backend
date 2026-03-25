package com.peanut.trace.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 加工记录实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("processing_record")
public class ProcessingRecord extends BaseEntity {
    private String recordId;
    private String batchNo;
    private String factoryId;
    private String factoryName;
    private String processType;
    private LocalDateTime processTime;
    private BigDecimal temperature;
    private Integer durationMin;
    private String operatorId;
    private String operatorName;
    private String qualityResult;
    private String dataHash;  // SHA-256哈希，用于区块链校验
}
