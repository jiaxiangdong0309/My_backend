package com.peanut.trace.stats.controller;

import com.peanut.trace.common.result.R;
import com.peanut.trace.stats.dto.StatsVO;
import com.peanut.trace.stats.service.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 统计分析接口
 */
@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    /**
     * 区域产量统计（热力图数据源）
     * GET /api/stats/region
     */
    @GetMapping("/region")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public R<List<StatsVO.RegionStats>> regionStats() {
        return R.ok(statsService.regionStats());
    }

    /**
     * 质检合格率统计（柱状图数据源）
     * GET /api/stats/qualify-rate
     */
    @GetMapping("/qualify-rate")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public R<List<StatsVO.QualifyRateStats>> qualifyRate(
            @RequestParam(required = false) String region) {
        return R.ok(statsService.qualifyRateStats(region));
    }

    /**
     * 月度趋势统计（折线图数据源）
     * GET /api/stats/trend
     */
    @GetMapping("/trend")
    @PreAuthorize("hasAnyRole('ADMIN','SUPERVISOR')")
    public R<List<StatsVO.MonthTrend>> trend(
            @RequestParam(defaultValue = "2026") String year) {
        return R.ok(statsService.monthTrend(year));
    }
}
