#!/bin/bash 
task_date=`date -d "-1 days" +"%Y%m%d"` 
if [ $# -eq 1 ]
	then
	task_date=$1
fi

echo $task_date

exit_code=0

SQL_PATH="/home/bl/ETL/sql"

LOG_PATH="/home/bl/ETL/log/$task_date"
LOG_FILE="$LOG_PATH/m_uec_jr_all_jr.log"
LOG_ERROR="$LOG_PATH/error.log"

mkdir -p $LOG_PATH

check_if_not_zero_exit(){
	$@ >> $LOG_FILE
	exit_code=$?
	time=`date +"%Y-%m-%d %H:%M:%S"`
	echo  $time $@  >> $LOG_FILE
	echo  " ----------------------$exit_code" >> $LOG_FILE
	if [ ! $exit_code -eq 0 ]
	then
		echo  " run failed !!! " >> $LOG_FILE
		echo $time $@ "error">>$LOG_ERROR
		exit
	fi
}

#检查hdfs文件

#执行hive语句
check_if_not_zero_exit  hive -hivevar SDATE=${task_date} -f $SQL_PATH/m_uec_jr_all_jr.q


#删除oracle中的数据 增量删除 inc_*   
check_if_not_zero_exit  ssh oracle@10.201.48.18 "/home/oracle/customize_clearoracletb.sh m_uec_jr_all_jr cdate ${task_date} "
#数据从hive导入到oracle
check_if_not_zero_exit  sqoop export --connect jdbc:oracle:thin:@10.201.48.18:1521:report --username idmdata --password bigdata915 --table IDMDATA.M_UEC_JR_ALL_JR  --export-dir /user/hive/warehouse/idmdata.db/m_uec_jr_all_jr/dt=${task_date}/  --fields-terminated-by '\t' --input-null-string '\\N' --input-null-non-string '\\N' --m 1

