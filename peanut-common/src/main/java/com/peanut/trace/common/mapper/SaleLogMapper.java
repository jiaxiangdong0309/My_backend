package com.peanut.trace.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.peanut.trace.common.entity.SaleLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface SaleLogMapper extends BaseMapper<SaleLog> {

    @Select("SELECT * FROM sale_log WHERE batch_no = #{batchNo} AND is_deleted = 0 ORDER BY sale_time DESC, id DESC")
    List<SaleLog> selectByBatchNo(@Param("batchNo") String batchNo);
}
