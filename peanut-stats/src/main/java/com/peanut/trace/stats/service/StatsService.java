package com.peanut.trace.stats.service;

import com.peanut.trace.common.mapper.PeanutProductMapper;
import com.peanut.trace.stats.dto.StatsVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 统计分析服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class StatsService {

    private final PeanutProductMapper productMapper;

    /**
     * 区域产量统计
     */
    public List<StatsVO.RegionStats> regionStats() {
        return productMapper.selectRegionStats().stream().map(row -> {
            StatsVO.RegionStats s = new StatsVO.RegionStats();
            s.setRegion((String) row.get("region"));
            s.setBatchCount(toLong(row.get("batchCount")));
            s.setTotalKg(toDouble(row.get("totalKg")));
            return s;
        }).collect(Collectors.toList());
    }

    /**
     * 质检合格率统计（各区域）
     */
    public List<StatsVO.QualifyRateStats> qualifyRateStats(String region) {
        return productMapper.selectQualifyRateStats(region).stream().map(row -> {
            StatsVO.QualifyRateStats s = new StatsVO.QualifyRateStats();
            s.setRegion((String) row.get("region"));
            s.setTotalCount(toLong(row.get("totalCount")));
            s.setPassCount(toLong(row.get("passCount")));
            s.setPassRate(toDouble(row.get("passRate")));
            return s;
        }).collect(Collectors.toList());
    }

    /**
     * 月度趋势统计
     */
    public List<StatsVO.MonthTrend> monthTrend(String year) {
        return productMapper.selectMonthTrend(year).stream().map(row -> {
            StatsVO.MonthTrend s = new StatsVO.MonthTrend();
            s.setMonth((String) row.get("month"));
            s.setBatchCount(toLong(row.get("batchCount")));
            s.setPassRate(toDouble(row.get("passRate")));
            return s;
        }).collect(Collectors.toList());
    }

    private Long toLong(Object val) {
        if (val == null) return 0L;
        if (val instanceof Long) return (Long) val;
        return ((Number) val).longValue();
    }

    private Double toDouble(Object val) {
        if (val == null) return 0.0;
        if (val instanceof Double) return (Double) val;
        return ((Number) val).doubleValue();
    }
}
