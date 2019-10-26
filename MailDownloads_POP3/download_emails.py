import poplib
from email.parser import Parser
from email.header import decode_header
from email.utils import parseaddr
import base64
from bs4 import BeautifulSoup
import quopri

poplib._MAXLINE=204800

def UTFdecoding(msg):
    msg = msg.replace("=?UTF-8?B?","")
    msg = msg.replace("=?utf-8?B?", "")
    msg = msg.replace("?=", "")
    msg = msg.replace("\"","")
    msg = base64.b64decode(msg)
    msg = msg.decode('utf-8')
    return msg

def EUC_KRdecoding(msg):
    msg = msg.replace("=?euc-kr?B?", "")
    msg = msg.replace("=?EUC-KR?B?", "")
    msg = msg.replace("?=", "")
    msg = msg.replace("\"", "")
    msg = base64.b64decode(msg)
    msg = msg.decode('euc-kr')
    return msg

def price_from_html_for_naverpay(msg_content):
    html_doc = msg_content
    start_index = html_doc.find("<html")
    end_index = html_doc.rfind('/html>') + 5
    real_content = html_doc[start_index:end_index + 1]
    soup = BeautifulSoup(real_content,'html.parser')
    soup.prettify()
    print(real_content)
    print()
    for tag in soup.find_all('span'):
        print(tag.text)

def info_from_html_for_KG(msg_content):
    global price, purchasing_item, purchasing_office
    html_doc = msg_content
    start_index = html_doc.find("<html")
    end_index = html_doc.rfind('/html>') + 5
    real_content = html_doc[start_index:end_index + 1]
    soup = BeautifulSoup(real_content, 'html.parser')
    soup.prettify()

    price = soup.select('td[colspan="2"]')[1].text.strip()
    purchasing_item = soup.select('td[width="242"]')[1].text
    purchasing_office = soup.select('td[width="242"]')[2].text

def info_from_html_for_NHN(msg_content):
    global price, purchasing_item, purchasing_office

    html_doc = msg_content

    price_index = html_doc.find("결제금액")
    price_content = html_doc[price_index + 9:price_index + 400]
    soup = BeautifulSoup(price_content, 'html.parser')
    soup.prettify()
    price = soup.find('td').text

    office_index = html_doc.find("구매상점명")
    office_content = html_doc[office_index + 10:office_index + 300]
    soup = BeautifulSoup(office_content, 'html.parser')
    soup.prettify()
    purchasing_office = soup.find('td').text

    item_index = html_doc.find("주문상품명")
    item_content = html_doc[item_index + 10:item_index + 300]
    soup = BeautifulSoup(item_content, 'html.parser')
    soup.prettify()
    purchasing_item = soup.find('td').text



# input email address, password and pop3 server domain or ip address

email ="ID"
password = "PW"

pop3_server = "pop.naver.com"

# pop3서버 접근, pop3포트 번호 995:
server = poplib.POP3_SSL('pop.naver.com',995)

# open debug switch to print debug information between client and pop3 server.
#server.set_debuglevel(1)

'''
# get pop3 server welcome message.
pop3_server_welcome_msg = server.getwelcome().decode('utf-8')
print(pop3_server_welcome_msg)
'''
# user account authentication (로그인)
server.user(email)
server.pass_(password)

# stat() function return email count and occupied disk size (메시지개수, 디스크사이즈)
print('Messages: %s. Size: %s' % server.stat())
print()
# list() function return all email list(email 전체 받음)
resp, mails, octets = server.list()

# retrieve the newest email index number
index = len(mails)
print("length of index : ",index)
print()

#declare variables for db
company = "PG"
date = "yyyy.MM.DD"
price = "0"
purchasing_office = "somewhere"
purchasing_item = "something"


for i in range(index):
    resp, lines, octets = server.retr(index-i)
    try:
        msg_content = b'\r\n'.join(lines).decode('utf-8')
    except UnicodeDecodeError :
        try:
            msg_content = b'\r\n'.join(lines).decode('euc-kr')
        except:
            pass

    msg = Parser().parsestr(msg_content)
    email_from = msg.get('From')

    #KG이니시스
    if "S0fAzLTPvcO9ug" in email_from:
        email_title = msg.get('Subject')
        email_title = EUC_KRdecoding(email_title)
        if "결제" in email_title:
            print("<", i, ">")
            email_date = msg.get('Date')
            info_from_html_for_KG(msg_content)
            try:
                print('Date ' + email_date)
            except TypeError:
                print("type error")
            print('From KG이니시스')
            print('Subject ' + email_title)
            info_from_html_for_KG(msg_content)
            print('구매상점명 '+purchasing_office)
            print('주문상품명'+purchasing_item)
            print('Price ' + price)
            print()
        else:
            pass

    #NHN KCP
    elif "NHN KCP" in email_from:
        email_title = msg.get('Subject')
        email_title = EUC_KRdecoding(email_title)
        if "결제" in email_title:
            print("<", i, ">")
            email_date = msg.get('Date')
            try:
                print('Date ' + email_date)
            except TypeError:
                print("type error")
            print('From NHN')
            print('Subject ' + email_title)
            info_from_html_for_NHN(msg_content)
            print('구매상점명 ' + purchasing_office)
            print('주문상품명' + purchasing_item)
            print('Price ' + price)
            #print(msg_content)
            print()
        else:
            pass

    else:
        pass



# delete the email from pop3 server directly by email index.
# server.dele(index)
# close pop3 server connection.
server.quit()


