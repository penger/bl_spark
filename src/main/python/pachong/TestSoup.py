# -*-coding:UTF-8-*-
from bs4 import BeautifulSoup
import os
import sys
reload(sys)
sys.setdefaultencoding('utf-8')
temp_path = "d:\\info.txt"
if os.path.exists(temp_path):
    os.remove(temp_path)
info_file = open(temp_path, 'w')
soup = BeautifulSoup(open('a.html'), "html.parser")
pcontent = soup.find('div', id='content').find_all("p")
for item in pcontent:
    print str(item.string)
    print type(str(item.string))
    info_file.write(str(item.string).replace('\n', '').replace(' ', '').strip()+'\n')
info_file.close()
print pcontent

