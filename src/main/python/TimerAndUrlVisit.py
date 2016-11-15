# -*-coding:UTF-8-*-
import time
import datetime
import urllib2
import re
import os

# http://www.kuaidaili.com/getproxy/?orderid=957886395561573&num=100&area=%E4%B8%8A%E6%B5%B7&area_ex=&port=&port_ex=&ipstart=&ipstart_ex=&carrier=0&an_ha=1&an_an=1&protocol=1&method=2&quality=0&sort=0&b_pcchrome=1&b_pcie=1&b_pcff=1&showtype=1

orderid = "957886395561573"
area = "上海"
temp_path = "d:\\abc.txt"

# target_url = "http://www.kuaidaili.com/getproxy/?orderid=957886395561573&num=100&area=%E4%B8%8A%E6%B5%B7&area_ex=&port=&port_ex=&ipstart=&ipstart_ex=&carrier=0&an_ha=1&an_an=1&protocol=1&method=2&quality=0&sort=0&b_pcchrome=1&b_pcie=1&b_pcff=1&showtype=1&format=text"
target_url = "http://www.kuaidaili.com/getproxy/?orderid=" + orderid + "&num=100&area=" + area + "&area_ex=&port=&port_ex=&ipstart=&ipstart_ex=&carrier=0&an_ha=1&an_an=1&protocol=1&method=2&quality=0&sort=0&b_pcchrome=1&b_pcie=1&b_pcff=1&showtype=1&format=text"


def task():
    start = time.clock()
    # 如果文件存在那么删除
    if os.path.exists(temp_path):
        os.remove(temp_path)
    temp_file = open(temp_path, "w")
    resp = urllib2.urlopen(target_url)
    html = resp.read()
    # print html
    count = 0
    for i in html.split("\n"):
        # 满足表达式以多个空格开头,满足ip表达式,以换行结束
        m = re.match('^ +((\d{1,3}\.){3}\d{1,3}:\d{1,4})<br/>', i)
        if m:
            if count != 0:
                line = "\n" + m.group(1)
            else:
                line = m.group(1)
            temp_file.write(line)
            count += 1

    temp_file.close()
    print "抓取了 %d 个IP, 用时 %.2f 秒" % (count, time.clock() - start)


def timer(n):
    while True:
        print time.strftime('%Y-%m-%d %X', time.localtime())
        task()
        time.sleep(n)


if __name__ == "__main__":
    timer(12)
