import datetime as datetime
from django.db import models

# Create your models here.
class Crawling(models.Model):
    PGname = models.CharField(max_length=30)            #PG사
    datetime = models.DateTimeField(default=datetime.datetime.now, blank=True) #날짜시간
    purchasing_office = models.CharField(max_length=30,blank=True)  #실거래처
    purchasing_item = models.CharField(max_length=30,blank=True)    #거래품목
    price = models.IntegerField(default=0, blank=True)              #가격

    # t = forms.DateTimeField(input_formats=['%Y/%m/%d %H:%M:%S'])