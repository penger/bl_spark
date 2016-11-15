from bs4 import BeautifulSoup

def insertion_insert(lst):
    for index in range(1, len(lst)):
        value = lst[index]
        i = index - 1
        print i
        while i >= 0 and value < lst[i]:
            print i
            lst[i+1] = lst[i]
            lst[i] = value
            i -= 1


a = [1, 3, 4, 5, 2, 2]
insertion_insert(a)
print a
