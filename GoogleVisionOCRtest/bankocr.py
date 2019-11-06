import io   # 파일을 읽고 쓰기위한 모듈
import re

# Imports the Google Cloud client library
from google.cloud import vision

client = vision.ImageAnnotatorClient()

with io.open('file path', 'rb') as image_file:
    content = image_file.read()

image = vision.types.Image(content=content)

response = client.text_detection(image=image)
texts = response.text_annotations

wholeText = texts[0].description
print("< ocr 결과 >")
print(wholeText)
print()
tokens = wholeText.split('\n')

date_regular = re.compile('\d+[/]\d+[/]*\d*|\d+[.]\d+[.]*\d*|\d+월\s*\d+일')
price_regular = re.compile('-\d*[,]*\d+\s*원|출금\s*\d*[,]*\d+\s*원')
price_regular2 = re.compile('(\d*[,]*)*\d+\s*원|(\d*[,]*)*\d+|[\\\](\d*[,]*)*\d+|W(\d*[,]*)*\d+')
price_regular3 = re.compile('(\d*[,]*)*\d+\s*원')
numbers = []
valid = False            # 간편결제 or PG사 지출이 맞는지
changePrice = False     # for 똑똑가계부 \처리


for token in tokens:
    if "이니시스" in token:
        company = "이니시스"
        valid = True
        break
    elif "네이버페이" in token:
        company = "네이버페이"
        valid = True
        break
    elif "NHN" in token:
        company = "NHN"
        valid = True
        break

# 날짜 먼저 찾고 입금이라는 단어가 있을경우 지출내역 아니므로 지출금액 찾지 않음
if valid:
    # print("<날짜>")
    for token in tokens:
        m = date_regular.search(token)
        if m is not None:
            date = m.group()
            print(date)
        if "입금" in token:
            valid = False

# 지출금액 찾기
if valid:
    # print("<금액>")
    # - ~ 원 혹은 출금 ~ 원 인 경우 우선 추출 (한개만)
    for token in tokens:
        m = price_regular.search(token)
        if m is not None:
            numbers.append(m.group())
            break
    # 한개 있는 경우
    if len(numbers) == 1:
        price = numbers[0]
        # print(price)
    # - ~ 원 혹은 출금 ~ 원 문자열이 없는 경우
    else:
        # ~원, W~ , \~ , 숫자 로 시작하는 경우를 다 찾음
        for token in tokens:
            m = price_regular2.match(token)
            if m is not None:
                numbers.append(token)
        for i in numbers:
            if ':' in i:
                index = numbers.index(i)
                del numbers[index]
        for i in numbers:
            if '월' in i or '화' in i or '수' in i or '목' in i or '금' in i or '토' in i or '일' in i:
                index = numbers.index(i)
                del numbers[index]

        # 가장 마지막 바운드 값을 price로 가정
        price = numbers[len(numbers) - 1]
        # 전체 문자열 중에 W가 있는 경우 똑똑가계부
        for i in numbers:
            if 'W' in i and 'W' not in price:
                changePrice = True
        # 똑똑가계부 예외처리
        if changePrice:
            for j in reversed(numbers):
                if 'W' in j:
                    price = j

    findPrice = re.compile('[0-9]')
    price = "".join(findPrice.findall(price))
    print(price)