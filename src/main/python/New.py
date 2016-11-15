# -*-coding:UTF-8-*-
from bs4 import BeautifulSoup
import re
import urllib2
import urllib

#抓取豆瓣排行榜电影
# class Douban:
#     def __init__(self, page):
#         self.page = page
#
#     def getMovies(self, pattern):
#         mList = re.findall(pattern,self.page)
#         # print mList
#         return mList
#
#
Url = "http://movie.douban.com/chart"
page = urllib2.urlopen(Url).read().decode("utf-8")
# douban = Douban(page)
# # film name
# pname = r'<img src=".*?" alt="(.*?)" class=""/>'
# names = douban.getMovies(pname)
#
#
# # 新片榜
# print r'豆瓣新片榜 · · · · · ·'
# i = 0
# for items in names:
#     print names[i]
#     i += 1
#
#
# # 下面用beautifulSoup来获取北美票房榜
# print page

soup = BeautifulSoup(page, "html.parser")
NAranking = soup.find('div', id="ranking").find('ul', id="listCont1")
print "\n\n\n北美票房榜---------------------------------------------------------------"
print soup.find('div', id="ranking").find('ul', id="listCont1").find('li').get_text()

i = 1
money = NAranking.find_all('span')
for item in NAranking.find_all('a'):
    print i, item.get_text().strip(), money[i-1].get_text()
    i += 1