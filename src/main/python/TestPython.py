# -*-coding:UTF-8-*-
import sys, time, os, re
import urllib, urllib2, cookielib
loginurl = 'https://www.douban.com/accounts/login'
cookie = cookielib.CookieJar()
opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(cookie))
params = {
    "form_email":"gongpengllpp@sina.com",
    "form_password":"lipp125?",
    "source":"index_nav" #没有的话登录不成功
}
#从首页提交登录
response=opener.open(loginurl, urllib.urlencode(params))


vvcode=raw_input('请输入图片上的验证码：')
print vvcode
exit()
