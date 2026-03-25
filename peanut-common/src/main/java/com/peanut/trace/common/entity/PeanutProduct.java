package com.peanut.trace.common.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 花生产品核心实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("peanut_product")
public class PeanutProduct extends BaseEntity {

    /** 产品批次号（全局唯一，如 PN-20260301-001） */
    private String batchNo;
    private String productName;
    private String variety;
    private String fieldId;
    private String farmerId;
    private String factoryId;
    private LocalDateTime processTime;
    private String inspectNo;
    /** 黄曲霉毒素B1含量(μg/kg)，GB 2761-2017 标准 ≤20 */
    private BigDecimal aflatoxin;
    /** 质检结果: 合格/不合格/待检 */
    private String inspectResult;
    private String warehouseId;
    private String dealerId;
    private LocalDateTime saleTime;
    private String qrCode;
    /** 区块链哈希存证值（SHA-256，64位十六进制） */
    private String blockHash;
    /** 上链状态: 0-待上链, 1-已上链, 2-上链失败 */
    private Integer chainStatus;
    /** 扩展字段（JSON格式，存储非结构化数据） */
    private String extJson;
}
