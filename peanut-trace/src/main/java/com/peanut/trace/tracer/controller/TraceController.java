package com.peanut.trace.tracer.controller;

import com.peanut.trace.common.result.R;
import com.peanut.trace.tracer.dto.TraceResultVO;
import com.peanut.trace.tracer.service.TraceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 产品追溯接口
 */
@RestController
@RequestMapping("/api/trace")
@RequiredArgsConstructor
public class TraceController {

    private final TraceService traceService;

    /**
     * 正向追溯：种植户 → 消费者
     * GET /api/trace/forward/{batchNo}
     */
    @GetMapping("/forward/{batchNo}")
    public R<TraceResultVO> forward(@PathVariable String batchNo) {
        return R.ok(traceService.traceForward(batchNo));
    }

    /**
     * 反向追溯：产品条码 → 源头农田
     * GET /api/trace/backward/{code}
     */
    @GetMapping("/backward/{code}")
    public R<TraceResultVO> backward(@PathVariable String code) {
        return R.ok(traceService.traceBackward(code));
    }

    /**
     * 哈希校验：验证数据完整性
     * GET /api/trace/verify/{batchNo}
     */
    @GetMapping("/verify/{batchNo}")
    public R<Map<String, Object>> verify(@PathVariable String batchNo) {
        boolean verified = traceService.verifyBatchHash(batchNo);
        Map<String, Object> result = Map.of(
            "batchNo", batchNo,
            "verified", verified,
            "message", verified ? "数据完整，未被篡改" : "数据异常，请联系管理员"
        );
        return R.ok(result);
    }
}
