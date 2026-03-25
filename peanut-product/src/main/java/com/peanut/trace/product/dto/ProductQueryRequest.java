package com.peanut.trace.product.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 产品多条件查询请求 DTO
 */
@Data
public class ProductQueryRequest {

    /** 批次号（模糊匹配） */
    private String batchNo;

    /** 产品名称（模糊匹配） */
    private String productName;

    /** 农户ID */
    private String farmerId;

    /** 加工企业ID */
    private String factoryId;

    /** 质检结果 */
    private String inspectResult;

    /** 所在地区 */
    private String region;

    /** 加工开始时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime processTimeStart;

    /** 加工结束时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime processTimeEnd;

    /** 上链状态 */
    private Integer chainStatus;

    /** 分页：当前页（从1开始） */
    private Integer pageNum = 1;

    /** 分页：每页数量 */
    private Integer pageSize = 20;
}
