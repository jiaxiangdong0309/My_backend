package com.peanut.trace.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.peanut.trace.common.entity.ProcessingRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface ProcessingRecordMapper extends BaseMapper<ProcessingRecord> {

    @Select("SELECT * FROM processing_record WHERE batch_no = #{batchNo} AND is_deleted = 0 ORDER BY process_time DESC, id DESC")
    List<ProcessingRecord> selectByBatchNo(@Param("batchNo") String batchNo);
}
