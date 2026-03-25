package com.peanut.trace.product.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 产品信息录入/导入 DTO
 */
@Data
public class ProductImportDTO {

    @ExcelProperty("批次号")
    @NotBlank(message = "批次号不能为空")
    private String batchNo;

    @ExcelProperty("产品名称")
    private String productName;

    @ExcelProperty("品种")
    private String variety;

    @ExcelProperty("农户ID")
    @NotBlank(message = "农户ID不能为空")
    private String farmerId;

    @ExcelProperty("地块编号")
    private String fieldNo;

    @ExcelProperty("播种日期")
    private String plantingDate;

    @ExcelProperty("收获日期")
    private String harvestDate;

    @ExcelProperty("加工厂ID")
    private String factoryId;

    @ExcelProperty("加工日期")
    private String processTime;

    @ExcelProperty("烘烤温度(℃)")
    private BigDecimal temperature;

    @ExcelProperty("质检结果")
    private String inspectResult;

    @ExcelProperty("黄曲霉毒素(μg/kg)")
    private BigDecimal aflatoxin;

    @ExcelProperty("质检报告编号")
    private String inspectNo;
}
