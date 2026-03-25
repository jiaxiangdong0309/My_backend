package com.peanut.trace.tracer.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 溯源结果 VO —— 完整链路展示
 */
@Data
public class TraceResultVO {

    private String batchNo;
    private String productName;
    private String variety;

    /** 区块链校验状态: true-数据完整, false-数据异常 */
    private Boolean chainVerified;

    /** 种植节点 */
    private PlantingNode planting;
    /** 加工节点 */
    private ProcessingNode processing;
    /** 仓储节点 */
    private StorageNode storage;
    /** 销售节点 */
    private SalesNode sales;

    // ---- 嵌套 VO ----

    @Data
    public static class PlantingNode {
        private String farmerId;
        private String farmerName;
        private String fieldNo;
        private String region;
        private Double latitude;
        private Double longitude;
        private String variety;
        private String plantingDate;
        private String harvestDate;
        private String cooperativeId;
        private String dataHash;
        private Boolean hashVerified;
    }

    @Data
    public static class ProcessingNode {
        private String factoryId;
        private String factoryName;
        private String processType;
        private LocalDateTime processTime;
        private Double temperature;
        private Integer durationMin;
        private String operatorName;
        private String qualityResult;
        private String inspectNo;
        private Double aflatoxin;  // μg/kg，符合 GB 2761-2017
        private String dataHash;
        private Boolean hashVerified;
    }

    @Data
    public static class StorageNode {
        private String warehouseId;
        private String warehouseName;
        private LocalDateTime inTime;
        private LocalDateTime outTime;
        private Double quantityKg;
        private Double temperature;
        private Double humidity;
        private String storageLocation;
        private String dataHash;
        private Boolean hashVerified;
    }

    @Data
    public static class SalesNode {
        private String dealerId;
        private String dealerName;
        private LocalDateTime saleTime;
        private Double quantityKg;
        private String logisticsNo;
        private String carrier;
        private String destination;
        private String dataHash;
        private Boolean hashVerified;
    }
}
