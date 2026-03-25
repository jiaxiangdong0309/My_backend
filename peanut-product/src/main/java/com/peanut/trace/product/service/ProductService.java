package com.peanut.trace.product.service;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.peanut.trace.common.entity.PeanutProduct;
import com.peanut.trace.common.exception.BusinessException;
import com.peanut.trace.common.mapper.PeanutProductMapper;
import com.peanut.trace.common.result.ResultCode;
import com.peanut.trace.common.utils.SHA256HashUtil;
import com.peanut.trace.product.dto.ProductImportDTO;
import com.peanut.trace.product.dto.ProductQueryRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 产品信息服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService extends ServiceImpl<PeanutProductMapper, PeanutProduct> {

    private final SHA256HashUtil sha256HashUtil;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 单条录入产品信息
     */
    public PeanutProduct addProduct(PeanutProduct product) {
        // 批次号唯一性校验
        if (getByBatchNo(product.getBatchNo()) != null) {
            throw new BusinessException(ResultCode.BATCH_NO_DUPLICATE);
        }
        product.setChainStatus(0); // 待上链
        save(product);
        log.info("产品录入成功: batchNo={}", product.getBatchNo());
        return product;
    }

    /**
     * 根据批次号查询产品
     */
    public PeanutProduct getByBatchNo(String batchNo) {
        return getOne(new LambdaQueryWrapper<PeanutProduct>()
                .eq(PeanutProduct::getBatchNo, batchNo)
                .eq(PeanutProduct::getIsDeleted, 0));
    }

    /**
     * 多条件分页查询
     */
    public IPage<PeanutProduct> queryPage(ProductQueryRequest req) {
        LambdaQueryWrapper<PeanutProduct> wrapper = new LambdaQueryWrapper<PeanutProduct>()
                .eq(PeanutProduct::getIsDeleted, 0);

        if (StringUtils.hasText(req.getBatchNo())) {
            wrapper.like(PeanutProduct::getBatchNo, req.getBatchNo());
        }
        if (StringUtils.hasText(req.getProductName())) {
            wrapper.like(PeanutProduct::getProductName, req.getProductName());
        }
        if (StringUtils.hasText(req.getFarmerId())) {
            wrapper.eq(PeanutProduct::getFarmerId, req.getFarmerId());
        }
        if (StringUtils.hasText(req.getFactoryId())) {
            wrapper.eq(PeanutProduct::getFactoryId, req.getFactoryId());
        }
        if (StringUtils.hasText(req.getInspectResult())) {
            wrapper.eq(PeanutProduct::getInspectResult, req.getInspectResult());
        }
        if (req.getChainStatus() != null) {
            wrapper.eq(PeanutProduct::getChainStatus, req.getChainStatus());
        }
        if (req.getProcessTimeStart() != null) {
            wrapper.ge(PeanutProduct::getProcessTime, req.getProcessTimeStart());
        }
        if (req.getProcessTimeEnd() != null) {
            wrapper.le(PeanutProduct::getProcessTime, req.getProcessTimeEnd());
        }
        wrapper.orderByDesc(PeanutProduct::getCreateTime);

        Page<PeanutProduct> page = new Page<>(req.getPageNum(), req.getPageSize());
        return page(page, wrapper);
    }

    /**
     * 批量导入（Excel/CSV）
     * 返回 {success: N, fail: N, errors: [...]}
     */
    public Map<String, Object> batchImport(MultipartFile file) {
        List<String> errors = new ArrayList<>();
        int successCount = 0;
        int failCount = 0;

        try {
            List<ProductImportDTO> dtoList = EasyExcel
                    .read(file.getInputStream())
                    .head(ProductImportDTO.class)
                    .sheet()
                    .doReadSync();

            for (int i = 0; i < dtoList.size(); i++) {
                ProductImportDTO dto = dtoList.get(i);
                try {
                    // 校验批次号唯一性
                    if (!StringUtils.hasText(dto.getBatchNo())) {
                        throw new IllegalArgumentException("批次号不能为空");
                    }
                    if (getByBatchNo(dto.getBatchNo()) != null) {
                        throw new IllegalArgumentException("批次号已存在: " + dto.getBatchNo());
                    }
                    PeanutProduct product = convertToProduct(dto);
                    save(product);
                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    errors.add("第" + (i + 2) + "行: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new BusinessException("文件解析失败: " + e.getMessage());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("success", successCount);
        result.put("fail", failCount);
        result.put("errors", errors);
        log.info("批量导入完成: success={}, fail={}", successCount, failCount);
        return result;
    }

    private PeanutProduct convertToProduct(ProductImportDTO dto) {
        PeanutProduct p = new PeanutProduct();
        p.setBatchNo(dto.getBatchNo());
        p.setProductName(dto.getProductName());
        p.setVariety(dto.getVariety());
        p.setFarmerId(dto.getFarmerId());
        p.setFactoryId(dto.getFactoryId());
        p.setInspectResult(dto.getInspectResult());
        p.setAflatoxin(dto.getAflatoxin());
        p.setInspectNo(dto.getInspectNo());
        p.setChainStatus(0);
        if (StringUtils.hasText(dto.getProcessTime())) {
            p.setProcessTime(LocalDateTime.parse(dto.getProcessTime(), dtf));
        }
        return p;
    }
}
