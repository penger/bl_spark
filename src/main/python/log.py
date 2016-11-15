# -*- coding: utf-8 -*- 
'''
Created on 2015-08-09

@author: shichao.bigdata
'''

import datetime
import logging.handlers

def initlog():
    logger = logging.getLogger()
    today=str(datetime.date.today())
    LOG_FILE="../log/"+today+".log"
    hdlr = logging.handlers.TimedRotatingFileHandler(LOG_FILE,when='D',interval=1,backupCount=40)
    formatter = logging.Formatter('%(asctime)19s %(levelname)-8s %(name)s %(threadName)s %(message)s')
    hdlr.setFormatter(formatter)
    logger.addHandler(hdlr)
    logger.setLevel(logging.INFO)
    return logger
Logger = initlog()

if __name__ == '__main__':
    pass