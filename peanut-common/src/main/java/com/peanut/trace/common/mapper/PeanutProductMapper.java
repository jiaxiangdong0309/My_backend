package com.peanut.trace.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.peanut.trace.common.entity.PeanutProduct;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface PeanutProductMapper extends BaseMapper<PeanutProduct> {

    @Select("SELECT * FROM peanut_product WHERE batch_no = #{batchNo} AND is_deleted = 0")
    PeanutProduct selectByBatchNo(@Param("batchNo") String batchNo);

    /** 区域产量统计（XML实现） */
    List<Map<String, Object>> selectRegionStats();

    /** 质检合格率统计（XML实现） */
    List<Map<String, Object>> selectQualifyRateStats(@Param("region") String region);

    /** 月度趋势统计（XML实现） */
    List<Map<String, Object>> selectMonthTrend(@Param("year") String year);
}
