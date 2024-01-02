package me.deipss.jvm.sandbox.inspector.web.dal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.deipss.jvm.sandbox.inspector.web.dal.entity.InvocationDO;

public interface InvocationMapper extends BaseMapper<InvocationDO> {

    int insertSelective(InvocationDO invocationDO);



}
