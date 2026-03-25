package com.peanut.trace.stats.dto;

import lombok.Data;

import java.util.List;

/**
 * 统计分析响应 VO
 */
@Data
public class StatsVO {

    /** 区域产量统计 */
    @Data
    public static class RegionStats {
        private String region;
        private Long batchCount;
        private Double totalKg;
    }

    /** 质检合格率统计 */
    @Data
    public static class QualifyRateStats {
        private String region;
        private Long totalCount;
        private Long passCount;
        private Double passRate;  // 百分比，如 92.3
    }

    /** 月度趋势统计 */
    @Data
    public static class MonthTrend {
        private String month;     // 如 2026-01
        private Long batchCount;
        private Double passRate;
    }
}
