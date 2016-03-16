# elasticsrarch-hbase-indexer
hbase数据通过RegionServer同步到elasticsearch中
version:
	+ hadoop2.2.0
	+ hbase-0.96.2
	+ elasticsearch-2.2.0

环境搭建不说了，搭得是单机的hadoop+hbase+elasticsearch环境

hbase
	+ 表为employee
	+ 列族empinfo

elasticsearch
	+ indexer changhongit
	+ type employee
	
employee中的内容
	+ id
	+ first_name
	+ last_name
	+ age
	+ about
	
程序中一部分是写死的。针对hbase表来实现的。使用了hbase的协处理

关于使用到的命令
hadoop
	+ 上传jar文件 hadoop fs -put sourcepath targetpath
	+ 查看jar文件 hadoop fs -ls /
	+ 删除jar文件 hadoop fs -rm /targetpath
	+ 其他命令请查看hadoop的官方手册
hbase
	+ bin目录下 ./hbase shell
	+ 查看数据库表 list
	+ 查看数据库表描述 describe 'table'
	+ 添加协处理器jar
		例子：alter 'employee','coprocessor'=>'hdfs://master:9000/hbase-observer.jar|com.changhongit.SyncDataHbaseObserver||'
	+ 删除协处理jar
		alter 'table', METHOD => 'table_att_unset', NAME => 'coprocessor$1'
	+ 针对hbase协处理操作时，得先disable表和enable表
	  disable 'table'
	  协处理器操作
	  enable 'table'
代码仅供参考.
		


