# jh-job整体架构

jh-job是一个简易分布式任务执行器，分为调度中心和执行器两部分。

调度中心可对任务进行管理和调度，可创建任务、更新任务、查看任务、删除任务、启动任务、停止任务、执行任务。其中，执行任务可使任务立即执行、启动任务可使任务定时执行、停止任务可使任务停止定时执行。

执行器可对任务进行执行。具体的，调度中心手动触发任务执行，启动任务定时执行，会下发执行请求到执行器端，执行器接收到调度器的请求后，进行执行。

整体架构如下图所示

![jm-job架构](https://github.com/johnhec/jh-job/blob/main/jh-job%E6%9E%B6%E6%9E%84%E5%9B%BE.png)

注：因受限于时间，只实现了调度器调度任务到执行器执行，没有考虑诸如，心跳，执行器结果上报，执行器注册，日志等。

## 数据库设计
1、Group，执行器表

| 字段 | 字段注释 | 字段类型 | 备注         |
| ---- | -------- | -------- | ------------ |
| id   | 关键字   | int   | 自增长       |
| name | 组名     | varchar  |   not null      |
| address_list | 执行器地址列表，多地址逗号分隔     | text  |     not null     |

2、JobInfo，任务表

| 字段    | 字段注释     | 字段类型 | 备注            |
| ------- | ------------ | -------- | --------------- |
| id      | 关键字       | int   | 自增长          |
| job_group      | 执行器主键ID   | int  | not null        |
| cron    | cron调度配置   | varchar  |                 |
| executor_handler | 执行器任务handler | varchar      |  |
| executor_param | 执行器任务参数     | varchar   |                 |
| trigger_status    | 调度状态：0-停止，1-运行   | tinyint  |    not null     |
| trigger_last_time | 上次调度时间 | bigint      | not null |
| trigger_next_time | 下次调度时间     | bigint   | not null        |

# jh-job-admin 调度中心

执行器管理相关接口
com.jh.job.admin.controller.JobGroupController

```java
    // 创建执行器
    public ReturnT<String> save(@RequestBody JobGroup jobGroup);

    // 查看执行器
    public ReturnT<JobGroup> loadById(int id);
```
任务管理相关接口
com.jh.job.admin.controller.JobInfoController
```java
    // 创建任务
    public ReturnT<String> add(@RequestBody JobInfo jobInfo);
    
    // 更新任务
    public ReturnT<String> update(@RequestBody JobInfo jobInfo);
    
    // 删除任务
    public ReturnT<String> remove(int id);
    
    // 查询任务.
    public ReturnT<JobInfo> loadById(int id);
    
    // 执行任务
    public ReturnT<String> triggerJob(int id, String executorParam, String addressList);
    
    // 启动任务
    public ReturnT<String> start(int id);
    
    // 停止任务
    public ReturnT<String> pause(int id);
```

# jh-job-core 执行器
任务执行相关接口
com.jh.job.core.controller.JobController

```java
    public ReturnT<String> run(@RequestBody TriggerParam triggerParam)
```
这里需要说明的是：任务通过调度器调度后，会下发执行器执行，由于时间原因，这里直接集成了spring boot提供了web接口给调度器调用，这里更恰当的做法是使用netty等提供接口。

# 运行和测试
1、运行doc/db/jh_job.sql创建库和表以及插入部分测试数据

2、启动JhJobAdminApplication

3、启动ClientAdminApplication

4、调用启动任务接口(start接口，可以用id为1的任务)

这样就可以在执行器端每一分钟输出一条"Test Job handler execute"记录（id为1的job配置的cron是1分钟执行一次）