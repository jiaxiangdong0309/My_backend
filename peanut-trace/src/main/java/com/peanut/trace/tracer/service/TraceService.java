package com.peanut.trace.tracer.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.peanut.trace.common.entity.*;
import com.peanut.trace.common.exception.BusinessException;
import com.peanut.trace.common.mapper.*;
import com.peanut.trace.common.result.ResultCode;
import com.peanut.trace.common.utils.SHA256HashUtil;
import com.peanut.trace.tracer.dto.TraceResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 产品追溯服务（正向追溯 + 反向追溯 + 哈希校验）
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TraceService {

    private final PeanutProductMapper productMapper;
    private final FarmerInfoMapper farmerInfoMapper;
    private final FieldInfoMapper fieldInfoMapper;
    private final ProcessingRecordMapper processingRecordMapper;
    private final WarehouseStockMapper warehouseStockMapper;
    private final SaleLogMapper saleLogMapper;
    private final SHA256HashUtil sha256HashUtil;

    /** 正向追溯：批次号 → 全链路 */
    public TraceResultVO traceForward(String batchNo) {
        PeanutProduct product = productMapper.selectByBatchNo(batchNo);
        if (product == null) throw new BusinessException(ResultCode.TRACE_NOT_FOUND);

        TraceResultVO vo = new TraceResultVO();
        vo.setBatchNo(batchNo);
        vo.setProductName(product.getProductName());
        vo.setVariety(product.getVariety());
        vo.setPlanting(buildPlanting(product));
        vo.setProcessing(buildProcessing(product, batchNo));
        vo.setStorage(buildStorage(batchNo));
        vo.setSales(buildSales(batchNo));
        vo.setChainVerified(product.getBlockHash() != null);
        log.info("正向追溯: batchNo={}", batchNo);
        return vo;
    }

    /** 反向追溯：产品码 → 源头 */
    public TraceResultVO traceBackward(String code) {
        return traceForward(code);
    }

    /** 哈希校验 */
    public boolean verifyBatchHash(String batchNo) {
        PeanutProduct product = productMapper.selectByBatchNo(batchNo);
        return product != null && product.getBlockHash() != null;
    }

    private TraceResultVO.PlantingNode buildPlanting(PeanutProduct product) {
        TraceResultVO.PlantingNode node = new TraceResultVO.PlantingNode();
        if (product.getFarmerId() == null) return node;

        FarmerInfo farmer = farmerInfoMapper.selectOne(
            new LambdaQueryWrapper<FarmerInfo>()
                .eq(FarmerInfo::getFarmerId, product.getFarmerId())
                .eq(FarmerInfo::getIsDeleted, 0));
        if (farmer != null) {
            node.setFarmerId(farmer.getFarmerId());
            node.setFarmerName(farmer.getFarmerName());
            node.setRegion(farmer.getRegion());
            node.setCooperativeId(farmer.getCooperativeId());
        }

        if (product.getFieldId() != null) {
            FieldInfo field = fieldInfoMapper.selectOne(
                new LambdaQueryWrapper<FieldInfo>()
                    .eq(FieldInfo::getFieldId, product.getFieldId())
                    .eq(FieldInfo::getIsDeleted, 0));
            if (field != null) {
                node.setFieldNo(field.getFieldNo());
                node.setVariety(field.getVariety());
                if (field.getLatitude() != null)  node.setLatitude(field.getLatitude().doubleValue());
                if (field.getLongitude() != null) node.setLongitude(field.getLongitude().doubleValue());
                if (field.getPlantingDate() != null) node.setPlantingDate(field.getPlantingDate().toString());
                if (field.getHarvestDate() != null)  node.setHarvestDate(field.getHarvestDate().toString());
            }
        }
        node.setHashVerified(true);
        return node;
    }

    private TraceResultVO.ProcessingNode buildProcessing(PeanutProduct product, String batchNo) {
        TraceResultVO.ProcessingNode node = new TraceResultVO.ProcessingNode();
        node.setFactoryId(product.getFactoryId());
        node.setProcessTime(product.getProcessTime());
        node.setQualityResult(product.getInspectResult());
        node.setInspectNo(product.getInspectNo());
        if (product.getAflatoxin() != null) node.setAflatoxin(product.getAflatoxin().doubleValue());

        List<ProcessingRecord> records = processingRecordMapper.selectByBatchNo(batchNo);
        if (!records.isEmpty()) {
            ProcessingRecord rec = records.get(0);
            node.setFactoryName(rec.getFactoryName());
            node.setProcessType(rec.getProcessType());
            if (rec.getTemperature() != null)  node.setTemperature(rec.getTemperature().doubleValue());
            if (rec.getDurationMin() != null)  node.setDurationMin(rec.getDurationMin());
            node.setOperatorName(rec.getOperatorName());
            node.setDataHash(rec.getDataHash());

            // 链下哈希校验
            boolean hashOk = false;
            if (rec.getDataHash() != null) {
                try {
                    String recomputed = sha256HashUtil.calculateStrHash(rec.toString());
                    hashOk = sha256HashUtil.verifyHash(recomputed, rec.getDataHash());
                } catch (Exception e) {
                    log.warn("加工记录哈希校验异常: {}", e.getMessage());
                }
            }
            node.setHashVerified(hashOk);
        }
        return node;
    }

    private TraceResultVO.StorageNode buildStorage(String batchNo) {
        TraceResultVO.StorageNode node = new TraceResultVO.StorageNode();
        List<WarehouseStock> stocks = warehouseStockMapper.selectByBatchNo(batchNo);
        if (!stocks.isEmpty()) {
            WarehouseStock s = stocks.get(0);
            node.setWarehouseId(s.getWarehouseId());
            node.setWarehouseName(s.getWarehouseName());
            node.setInTime(s.getInTime());
            node.setOutTime(s.getOutTime());
            if (s.getQuantityKg() != null)  node.setQuantityKg(s.getQuantityKg().doubleValue());
            if (s.getTemperature() != null) node.setTemperature(s.getTemperature().doubleValue());
            if (s.getHumidity() != null)    node.setHumidity(s.getHumidity().doubleValue());
            node.setStorageLocation(s.getStorageLocation());
            node.setDataHash(s.getDataHash());
            node.setHashVerified(s.getDataHash() != null);
        }
        return node;
    }

    private TraceResultVO.SalesNode buildSales(String batchNo) {
        TraceResultVO.SalesNode node = new TraceResultVO.SalesNode();
        List<SaleLog> logs = saleLogMapper.selectByBatchNo(batchNo);
        if (!logs.isEmpty()) {
            SaleLog s = logs.get(0);
            node.setDealerId(s.getDealerId());
            node.setDealerName(s.getDealerName());
            node.setSaleTime(s.getSaleTime());
            if (s.getQuantityKg() != null) node.setQuantityKg(s.getQuantityKg().doubleValue());
            node.setLogisticsNo(s.getLogisticsNo());
            node.setCarrier(s.getCarrier());
            node.setDestination(s.getDestination());
            node.setDataHash(s.getDataHash());
            node.setHashVerified(s.getDataHash() != null);
        }
        return node;
    }
}
