package com.peanut.trace.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.peanut.trace.common.entity.WarehouseStock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface WarehouseStockMapper extends BaseMapper<WarehouseStock> {

    @Select("SELECT * FROM warehouse_stock WHERE batch_no = #{batchNo} AND is_deleted = 0 ORDER BY in_time DESC, id DESC")
    List<WarehouseStock> selectByBatchNo(@Param("batchNo") String batchNo);
}
