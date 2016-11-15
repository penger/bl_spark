# -*-coding:UTF-8-*-
'''
Created on 2015-10-21

@author: shichao
'''
import string

import MySQLdb
from _mysql_exceptions import Error

from config import getinfo
from log import Logger as Logger


class Mysql(object):
    def __init__(self):
        self.mysqlinfo=getinfo("mysql_task_info")
   
    def insert_task_log(self, values):
        try:
            con = MySQLdb.connect(host=self.mysqlinfo["host"], port=string.atoi(self.mysqlinfo["port"]), user=self.mysqlinfo["user"], passwd=self.mysqlinfo["passwd"], db=self.mysqlinfo["dbname"])
            cursor = con.cursor()
            sql = "insert into task_log(task_id,task_type,start_time,end_time,cost,ifnormal,run_date) values(%s,%s,%s,%s,%s,%s,%s)"
            cursor.execute(sql, values)
            con.commit()
        except Error, e:
            Logger.error("mysql error insert_task_log" + e.args[0].__str__() + ":" + e.args[1].__str__())
            print "mysql error insert_task_log %d: %s" % (e.args[0], e.args[1])
        finally:
            con.close()
        
    def select_task_sceduling(self,run_date):
        try:
            con = MySQLdb.connect(host=self.mysqlinfo["host"], port=string.atoi(self.mysqlinfo["port"]), user=self.mysqlinfo["user"], passwd=self.mysqlinfo["passwd"], db=self.mysqlinfo["dbname"])
            cursor = con.cursor()
            sql = "select task_id,runstatus,ifnormal from task_sceduling where date(run_date)='"+ run_date +"'"
            cursor.execute(sql)
            results = cursor.fetchall()
            return results
        except Error, e:
            Logger.error("mysql error select_task_sceduling" + e.args[0].__str__() + ":" + e.args[1].__str__())
            print "mysql error select_task_sceduling%d: %s" % (e.args[0], e.args[1])
        finally:
            con.close()
        
    def init_task_sceduling(self,taskinfo,run_date):
        try:
            con = MySQLdb.connect(host=self.mysqlinfo["host"], port=string.atoi(self.mysqlinfo["port"]), user=self.mysqlinfo["user"], passwd=self.mysqlinfo["passwd"], db=self.mysqlinfo["dbname"])
            cursor = con.cursor()
            for task_id in taskinfo.keys():
                values=[run_date,task_id,False,True,]
                sql = "insert into task_sceduling(run_date,task_id,runstatus,ifnormal ) values(%s,%s,%s,%s)"
                cursor.execute(sql, values)
            con.commit()
        except Error, e:
            Logger.error("mysql error insert_task_log" + e.args[0].__str__() + ":" + e.args[1].__str__())
            print "mysql error insert_task_log %d: %s" % (e.args[0], e.args[1])
        finally:
            con.close()
# INSERT table (auto_id, auto_name) values (1, ‘yourname') ON DUPLICATE KEY UPDATE auto_name='yourname'
    def update_task_runstatus(self,task_id,run_date,runstatus):
        try:
            con = MySQLdb.connect(host=self.mysqlinfo["host"], port=string.atoi(self.mysqlinfo["port"]), user=self.mysqlinfo["user"], passwd=self.mysqlinfo["passwd"], db=self.mysqlinfo["dbname"])
            cursor = con.cursor()
            sql = "update task_sceduling set runstatus=%s where task_id=%s and run_date=%s"
            if runstatus:
                cursor.execute(sql,["1",task_id,run_date])
            else:
                cursor.execute(sql,["0",task_id,run_date])
            con.commit()
        except Error, e:
            Logger.error("mysql error update_task_sceduling" + e.args[0].__str__() + ":" + e.args[1].__str__())
            print "mysql error update_task_sceduling%d: %s" % (e.args[0], e.args[1])
        finally:
            con.close()
            
    def update_task_infnormal(self,task_id,run_date,ifnormal):
        try:
            con = MySQLdb.connect(host=self.mysqlinfo["host"], port=string.atoi(self.mysqlinfo["port"]), user=self.mysqlinfo["user"], passwd=self.mysqlinfo["passwd"], db=self.mysqlinfo["dbname"])
            cursor = con.cursor()
            sql = "update task_sceduling set ifnormal=%s where task_id=%s and run_date=%s"
            if ifnormal:
                cursor.execute(sql,["1",task_id,run_date])
            else:
                cursor.execute(sql,["0",task_id,run_date])
            con.commit()
        except Error, e:
            Logger.error("mysql error update_task_sceduling" + e.args[0].__str__() + ":" + e.args[1].__str__())
            print "mysql error update_task_sceduling%d: %s" % (e.args[0], e.args[1])
        finally:
            con.close()
            
    def select_task_impala_load_ready(self):
        try:
            con = MySQLdb.connect(host=self.mysqlinfo["host"], port=string.atoi(self.mysqlinfo["port"]), user=self.mysqlinfo["user"], passwd=self.mysqlinfo["passwd"], db=self.mysqlinfo["dbname"])
            cursor = con.cursor()
            task_str = self.select_task_impala_taskname_list()
	    sql = "select task_name,node_num,batch_id,h_db_name,h_table_name,h_table_part,i_db_name,i_table_name,i_table_part,insert_type,count(1) as num \
                    from task_impala_load where status='READY' \
		    and task_name in {task_str} \
                    group by task_name,node_num,batch_id,h_db_name,h_table_name,h_table_part,i_db_name,i_table_name,i_table_part,insert_type having num=node_num;"
	    sql = sql.replace("{task_str}", task_str)
            cursor.execute(sql)
            results = cursor.fetchall()
            return results
        except Error, e:
            Logger.error("mysql error select_task_impala_load_ready" + e.args[0].__str__() + ":" + e.args[1].__str__())
            print "mysql error select_task_impala_load_ready%d: %s" % (e.args[0], e.args[1])
        finally:
            con.close()
    

    def select_task_impala_taskname_list(self):
        try:
            con=MySQLdb.connect(host=self.mysqlinfo["host"], port=string.atoi(self.mysqlinfo["port"]), user=self.mysqlinfo["user"], passwd=self.mysqlinfo["passwd"], db=self.mysqlinfo["dbname"])
            cursor = con.cursor()
            sql="select task_name from task_impala_sql"
            cursor.execute(sql)
            taskname_list = cursor.fetchall()
	    task_str="("
            for i in range(0,len(taskname_list)):
                for j in range(0,len(taskname_list[i])):
                    if(i==(len(taskname_list)-1)):
                        task_str = task_str + "'"+str(taskname_list[i][j])+"'"
                    else:
                        task_str = str(task_str +"'" + taskname_list[i][j])+"'" +","
            task_str = task_str +")"
            return task_str
        except Error, e:
            Logger.error("select_task_impala_taskname_list" + e.args[0].__str__() + ":" + e.args[1].__str__())
            print "mysql error select_task_impala_taskname_list%d: %s" % (e.args[0], e.args[1])
        finally:
            con.close()

        
    def select_task_impala_load_not_ready(self):
        try:
            con = MySQLdb.connect(host=self.mysqlinfo["host"], port=string.atoi(self.mysqlinfo["port"]), user=self.mysqlinfo["user"], passwd=self.mysqlinfo["passwd"], db=self.mysqlinfo["dbname"])
            cursor = con.cursor()
            task_str = self.select_task_impala_taskname_list()
	    sql = "select task_name,node_num,batch_id,h_db_name,h_table_name,h_table_part,i_db_name,i_table_name,i_table_part,insert_type,count(1) as num \
                    from task_impala_load where status!='SUCCESS' \
		    and task_name in {task_str}\
                    group by task_name,node_num,batch_id,h_db_name,h_table_name,h_table_part,i_db_name,i_table_name,i_table_part,insert_type having num=node_num;"
            sql = sql.replace("{task_str}", task_str)
            print("select_task_impala_load_not_ready functions run! select sql => "+ sql )
            cursor.execute(sql)
            results = cursor.fetchall()
	    if(len(results)!=0):
	    	print("select_task_impala_load_not_ready  sql result : \n r(0): " + str(results[0]))
	    else:
		print("result is null! ")
            return results
        except Error, e:
            Logger.error("mysql error select_task_impala_load_ready" + e.args[0].__str__() + ":" + e.args[1].__str__())
            print "mysql error select_task_impala_load_ready%d: %s" % (e.args[0], e.args[1])
        finally:
            con.close()
    
    def update_task_impala_load_satus(self,vals):
        try:
            con = MySQLdb.connect(host=self.mysqlinfo["host"], port=string.atoi(self.mysqlinfo["port"]), user=self.mysqlinfo["user"], passwd=self.mysqlinfo["passwd"], db=self.mysqlinfo["dbname"])
            cursor = con.cursor()
            sql = "update task_impala_load set status=%s where task_name=%s and batch_id=%s and h_db_name=%s and h_table_name=%s \
            and h_table_part=%s and i_db_name=%s and i_table_name=%s and i_table_part=%s and insert_type=%s"
            cursor.execute(sql,vals)
            con.commit()
        except Error, e:
            Logger.error("mysql error update_task_impala_load_satus" + e.args[0].__str__() + ":" + e.args[1].__str__())
            print "mysql error update_task_impala_load_satus%d: %s" % (e.args[0], e.args[1])
        finally:
            con.close()
            
    def select_task_impala_sql(self,task_name):
        try:
            con = MySQLdb.connect(host=self.mysqlinfo["host"], port=string.atoi(self.mysqlinfo["port"]), user=self.mysqlinfo["user"], passwd=self.mysqlinfo["passwd"], db=self.mysqlinfo["dbname"])
            cursor = con.cursor()
            sql = "select sub_sql from task_impala_sql where task_name='"+task_name+"'"
            print("impala_sql"+sql)
            cursor.execute(sql)
            result = cursor.fetchall()
            return result
        except Error, e:
            Logger.error("mysql error select_task_impala_sql" + e.args[0].__str__() + ":" + e.args[1].__str__())
            print "mysql error select_task_impala_sql%d: %s" % (e.args[0], e.args[1])
        finally:
            con.close()
