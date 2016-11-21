# -*-coding:UTF-8-*-
old_file = open("m_uec_jr_all_jr.sh", 'r')
new_file = open("new_file.sh", 'w')
for i in old_file.readlines():
    new_file.write(i)
    if not i.startswith("#") and i.count("48.18"):
        line = i.replace("10.201.48.18", "{NEW_DB_IP}")
        line = '${PREFIX}' + line
        new_file.write(line)
old_file.close()
new_file.close()