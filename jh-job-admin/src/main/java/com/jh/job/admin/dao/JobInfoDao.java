package com.jh.job.admin.dao;

import com.jh.job.admin.model.JobInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface JobInfoDao {
	
	public int save(JobInfo info);

	public JobInfo loadById(@Param("id") int id);

	public int update(JobInfo jobInfo);

	public int delete(@Param("id") long id);

	public List<JobInfo> scheduleJobQuery(@Param("maxNextTime") long maxNextTime);

	public int scheduleUpdate(JobInfo jobInfo);
}
