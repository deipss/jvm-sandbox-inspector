package me.deipss.jvm.sandbox.inspector.debug.consumer.dal.classicmodel;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.deipss.jvm.sandbox.inspector.debug.consumer.dal.entity.classicmodel.Employees;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmployeesMapper extends BaseMapper<Employees> {
    int updateBatch(List<Employees> list);

    int batchInsert(@Param("list") List<Employees> list);

    int insertOrUpdate(Employees record);

    int insertOrUpdateSelective(Employees record);
}