# Redis关键参数

## maxclients - 客户端最大连接数

### 可能引发的错误信息 ：max number of clients reached

> `maxclients` 默认为0，即不限制 ， 一般不需要修改。 所以客户端连接限制，取决于操作系统连接参数 ulimit -n （max open files） ，
可通过修改`/etc/security/limit.conf`文件已永久生效。


## repl-ping-slave-period/repl-timeout - 主从响应策略

### 可能的错误信息 ： scheduled to be closed ASAP for overcoming of output buffer limits

> slave会每隔`repl-ping-slave-period`（默认10秒）ping一次master，如果超过`repl-timeout`（默认60秒）都没有收到响应，就会认为master挂了。
如果master明明没挂，但被阻塞了也会报这个错误，可以适当调大`repl-timeout`。

## client-output-buffer-limit - 客户端输出缓冲区

> 该参数有三种场景策略，主要是第二种slave场景。当使用主从复制时，性能压测下，数据量会急剧增长，导致从节点需要复制的数据很大，消耗时长增加。
slave没挂但被阻塞住了，比如正在loading master发过来的RDB，master的指令不能立刻发送给slave，就会放在output buffer中（oll是命令输了，omem是大小）。
在配置文件中有如下配置：`client-output-buffer-limit 256mb 64mb 60` ,这是负责发送数据给slave的client，如果buffer超过256m或者连续60秒超过64mb，
就会被立刻强行关闭。所以此时应该相应调大数值，否则就会出现循环发送的情况：master发送一个很大的RDB给slave，slave努力的装载，但还没装载完，master对
client的缓存满了，又重新再来一次。

> 平时可以在master执行redis-cli client list找cmd=sync，flag=S的client，注意omem的变化。

## loglevel/logfile - 日志级别和日志输出

> 生产环境可设置为warning级别，并重定向至某个文件中。

## 性能调优

### 可能的错误信息：cannot allocate memory

> Redis在主从复制时，需要fork子进程来进行操作，如果应用堆积了很大的数据在内存中，那么就需要针对这个子进程申请相应的内存空间，此时会受到操作系统的限制。
通过更改系统配置文件/etc/sysctl.conf的`vm.overcommit_memory=1`以永久生效。改参数有0、1、2三个值。1表示允许分配所有的物理内存。

## 客户端频繁获取连接限制

### 可能的错误信息 ：cannot assign requested address

> 频繁的连接服务器，但每次连接都在短时间内结束，导致很多的time_wait，以至于用光端口号，所以新连接没办法绑定端口。修改如下两个内核参数：

 - `sysctl -w net.ipv4.tcp_timestamps=1` 开启对于TCP时间戳的支持，若该项设置为0，则下面一项设置不起作用；
 - `sysctl -w net.ipv4.tcp_tw_recycle=1` 表示开启TCP连接中time_wait sockets的快速回收。
