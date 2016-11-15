# -*- coding: utf-8 -*- 
'''
Created on 2015-08-12

@author: shichao.bigdata

'''
from ConfigParser import SafeConfigParser
from log import Logger
from string import Template
import ConfigParser
import log
import time
import string
from _mysql_exceptions import Error
from pbin.mail import send_mail
from anydbm import error
# 包含配置文件位置的配置文件
configFile = "../conf/config.cfg"
# 方法执行出错 返回错误代码“0”
errorCode = "0"
# sqoop任务失败错误代码
###############################################################
#    获取sqool语句组成的list
###############################################################
def getErrorcode():
    return errorCode

###############################################################
#    获取sqool语句组成的list
###############################################################
def getSqoopJobDict():
    jobDict = {}
    Logger = log.Logger
    conf = ConfigParser.RawConfigParser() 
    #获取sqoop配置文件名称
    sqoopConfFileName = getConfigFileName("sqoop")
    try:
        conf.read(sqoopConfFileName)
    except:
        Logger.error("read sqoop configuration file error " + sqoopConfFileName)
        return errorCode
    jobNames_str = conf.get("jobsName", "jobs")
    if(jobNames_str is None):
        Logger.info("there is no job in the configuration!")
        return errorCode
    jobNames = jobNames_str.split(",")
    for jobName in jobNames:
        sqoopcmd = getEnv("sqoop") + " "
        for (key, value) in conf.items(jobName):
            #如果数据来自sql语句查询的结果，那么sql语句放在子sql里面 防止sql中包含“=”号
#             if(str(key) == "-e" or str(key) == "--query"):
#                 value = getSqoopSubSql(value)
            sqoopcmd = sqoopcmd  + " " + key + " " + value
        jobDict[jobName]=sqoopcmd
    return jobDict

###############################################################
#    获取sqool的subsql
###############################################################
def getSqoopSubSql(value):
    value = getValueByKey("sqoop","subsql",value)
    return value
###############################################################
#    获取命令所在的环境变量
#    @param task:任务类型,如sqoop，spark，mapreduce，hive 
###############################################################
def getEnv(task):
    conf = ConfigParser.RawConfigParser() 
    #通过任务类型获取对应的配置文件
    ConfFileName = getConfigFileName(task)
    try:
        conf.read(ConfFileName)
    except:
        #email()
        Logger.error("failed to read sqoop config file!" + ConfFileName)
        return errorCode
    #获取该命令所在机器的环境变量
    cmd = conf.get(task+"Env","cmdPath")
    return cmd    

###############################################################
#    通过sqoop任务名称获取某个sqoop的所有参数组成的一条命令字符串
#    @param jobName:sqoop任务名称 ，所有任务名称 命名规则：[sqoop_] + [import/export_] +[关系数据库表名称_] + [hive表名称]
###############################################################
def getSqoopJobListByName(jobName):
    conf = ConfigParser.RawConfigParser() 
    sqoopConfFileName = getConfigFileName("sqoop")
    sqoopCmd = getEnv("sqoop") 
    try:
        conf.read(sqoopConfFileName)
    except:
        #email()
        Logger.error("failed to read config file "+ sqoopConfFileName)
        return errorCode
    for (key, value) in conf.items(jobName):
        sqoopCmd = sqoopCmd  + " " + key + " " + value
    return sqoopCmd
###############################################################
#    获取线程池数量
###############################################################
def getSqoopThreadNumber():
    conf = ConfigParser.RawConfigParser() 
    sqoopConfFileName = getConfigFileName("sqoop")
    conf.read(sqoopConfFileName)
    threadPoolNumber = conf.get("sqoopTask","threadPoolNumber")
#     print(type(threadPoolNumber))
    return threadPoolNumber

###############################################################
#    通过section获取某个section下的所有kv对组成的字典
#    @param taskname:任务名称，如：mapreduce,hive,sqoop,spark
#    @param section: 配置文件里的section名称
###############################################################
def getKeyValueBySection(taskName,section):
    conf = ConfigParser.RawConfigParser() 
    configFile = getConfigFileName(taskName)
    if(configFile == errorCode):
        Logger.error("Failed to get configFile from function getConfigFileName("+taskName+")")
        return errorCode
    try:
        conf.read(configFile)
    except:
        Logger.error("read config file failed ! logfile dir is "+ configFile)
        return errorCode
    dict_kv = conf.items(section)
    return dict_kv
#############################################################
#    获取sqoop线程池中线程个数
#############################################################
def getSqoopPoolNumber():
    poolNumber = getValueByKey("sqoop","sqoopTask","threadPoolNumber")
    if(poolNumber != errorCode):
        return poolNumber
    else:
        Logger.error("failed to get sqoop pool number!")
        return errorCode

#############################################################
#    根据配置文件名称，配置文件内部的section，key获取对应的值
#    @param taskname:任务名称，如：mapreduce,hive,sqoop,spark
#    @param section: 配置文件里的section名称
#    @param key:配置文件中的key 
#############################################################
def getValueByKey(taskName,section,key):
    conf = ConfigParser.RawConfigParser() 
    configFile = getConfigFileName(taskName)
    if(configFile == errorCode):
        Logger.error("Failed to get configFile from function getConfigFileName("+taskName+")")
        return errorCode
    try:
        conf.read(configFile)
    except:
        Logger.error("read config file failed ! logfile dir is "+ configFile)
        return errorCode
    value = conf.get(section,key)
    return value

#############################################################
#    根据任务类型获取对应的配置文件
#    @param taskname:任务名称，如：mapreduce,hive,sqoop,spark
#############################################################
def getConfigFileName(taskName):
    configRoot = getConfigRoot()
    if(configRoot == errorCode):
        Logger.error("failed to get the root dir of config file " + taskName)
        return errorCode
    configFileName = configRoot + str(taskName) + ".cfg"
    return configFileName
#############################################################
#    获取配置文件根目录
#############################################################
def getConfigRoot():
    conf = ConfigParser.RawConfigParser()
    try: 
        conf.read(configFile)
    except:
        Logger.err("failed to read config file :" + configFile)
        return errorCode
    return conf.get("configInfo", "configRoot")
#############################################################
#    获取工作流
#############################################################
def getWorkflow():
    conf = ConfigParser.RawConfigParser()
    workflowConfigFile = getConfigFileName("workflow")
    if(workflowConfigFile == errorCode):
        Logger.error("failed to get workflow config file!")
        return errorCode
    try:
        conf.read(workflowConfigFile)
    except:
        send_mail("failed to read workflow config file!","time: "+str(time.time()))
        Logger.error("failed to read workflow config file!")
        return errorCode
    workDict = conf.items("workflow")
    if(workDict is not None):
        return workDict
    else:
        return errorCode
    
#############################################################
#    main函数
#############################################################
if __name__ == '__main__':
    print(getSqoopPoolNumber())
#     getWorkflow()
#     print(getValueByKey("sqoop","sqoopEnv","whichSqoop"))
#     dict = getKeyValueBySection("sqoop","importjob1")
#     for (k,v) in dict:
#         print("K:" + k +"  v: "+ v) 
#     print(getConfigFileName("sqoop"))
#     print(getConfigRoot())
    
    