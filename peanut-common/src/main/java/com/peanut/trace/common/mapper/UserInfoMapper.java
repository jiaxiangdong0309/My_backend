package com.peanut.trace.common.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.peanut.trace.common.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}
