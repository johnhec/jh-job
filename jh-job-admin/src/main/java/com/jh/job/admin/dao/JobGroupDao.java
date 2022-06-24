package com.jh.job.admin.dao;

import com.jh.job.admin.model.JobGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface JobGroupDao {

    public int save(JobGroup jobGroup);

    public JobGroup load(@Param("id") int id);

}
