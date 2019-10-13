from django.conf import settings
from django.db import models
from django.utils import timezone
# Create your models here.


class Transaction(models.Model):
    user_id = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE)
    PGname = models.CharField(max_length=30)                                    #PG사
    date = models.DateField(default = timezone.now(), blank=True)               #결제일자
    purchasing_office = models.CharField(max_length=40, null=True, blank=True)  #상점명
    purchasing_item = models.CharField(max_length=50,blank=True)                #상품명
    price = models.IntegerField(blank=True)                                     #결제금액

    def __str__(self):
        return "%s %s %s" %(self.PGname, self.date, self.price)
    # t = forms.DateField(input_formats=['%Y/%m/%d'])
