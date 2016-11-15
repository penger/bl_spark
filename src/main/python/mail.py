# -*-coding:UTF-8-*-
'''
Created on 2015-08-05

@author: shichao.bigdata
'''

from email.mime.text import MIMEText
import smtplib

exception_list = ["shichao@bl.com"]
info_list = ["shichao@bl.com"]
mail_host = "smtp.163.com"
mail_user = "yeahmobizealot"
mail_pass = "yeah123"
mail_postfix = "163.com"

def send_mail(sub, context, mailtype):
    '''
    to_list: 发送给谁
    sub: 主题
    context: 内容
    send_mail("xxx@126.com","sub","context")
    '''
    me = mail_user + "<" + mail_user + "@" + mail_postfix + ">"
    msg = MIMEText(context, 'plain', 'utf-8')
    msg['Subject'] = sub
    msg['From'] = "bailian-sqoop-info"
    if mailtype == "INFO":
        mailto_list = info_list
    else:
        mailto_list = exception_list
    msg['To'] = ";".join(mailto_list)
    try:
        send_smtp = smtplib.SMTP()
        send_smtp.connect(mail_host)
        send_smtp.login(mail_user, mail_pass)
        send_smtp.sendmail(me, mailto_list, msg.as_string())
    except Exception as e:
        print(str(e))
        return False
    finally:
        send_smtp.close()
    return True

if __name__ == '__main__':
    if (True == send_mail("subject", "context", "EXCEPTION")):
        print ("测试成功")
    else:
        print ("测试失败")
