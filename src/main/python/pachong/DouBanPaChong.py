# -*-coding:UTF-8-*-
import sys, time, os, re
import urllib, urllib2, cookielib
from bs4 import BeautifulSoup
import sys
reload(sys)
sys.setdefaultencoding('utf-8')
temp_path = "d:\\info.txt"
if os.path.exists(temp_path):
    os.remove(temp_path)
info_file = open(temp_path, 'a')

loginurl = 'https://www.douban.com/accounts/login'
cookie = cookielib.CookieJar()
opener = urllib2.build_opener(urllib2.HTTPCookieProcessor(cookie))
params = {
    "form_email":"",
    "form_password":"",
    "source":"index_nav" # 没有的话登录不成功
}
# 从首页提交登录
response=opener.open(loginurl, urllib.urlencode(params))

print response.geturl()

# 验证成功跳转至登录页
if response.geturl() == "https://www.douban.com/accounts/login":
    html=response.read()
    # 验证码图片地址
    imgurl=re.search('<img id="captcha_image" src="(.+?)" alt="captcha" class="captcha_image"/>', html)
    if imgurl:
        url=imgurl.group(1)
        # 将图片保存至同目录下
        if os.path.exists('v.jpg'):
            os.remove('v.jpg')
        res=urllib.urlretrieve(url, 'v.jpg')
        # 获取captcha-id参数
        captcha=re.search('<input type="hidden" name="captcha-id" value="(.+?)"/>' ,html)
        if captcha:
            print "图片地址为: ",captcha
            vcode=raw_input('请输入图片上的验证码：')
            params["captcha-solution"] = vcode
            params["captcha-id"] = captcha.group(1)
            params["user_login"] = "登录"
            # 提交验证码验证
            response=opener.open(loginurl, urllib.urlencode(params))
            ''' 登录成功跳转至首页 '''
            print response.geturl() , "--------------------------"
            if response.geturl() == "https://www.douban.com/":
                print response.read()
                print 'login success ! '
                print '准备进行爬虫'
                htmlfile = open("d://s.html", 'w')
                for i in range(1, 3):
                    print "爬出第%d 篇广播" % i
                    new_url = 'https://www.douban.com/people/chunsue/statuses?p='+bytes(i)
                    # new_url = 'https://www.douban.com/'
                    # content = urllib2.urlopen(new_url).read().decode("utf-8")
                    content=opener.open(new_url).read()
                    htmlfile.write(content)
                    soup = BeautifulSoup(content, "html.parser")
                    pcontent = soup.find('div', id='content').find_all("p")
                    for item in pcontent:
                        print str(item.string)
                        info_file.write(str(item.string).replace('\n', '').replace(' ', '').strip()+'\n')
            info_file.close()
            htmlfile.close()
            print pcontent