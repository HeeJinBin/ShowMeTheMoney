# Generated by Django 2.2.6 on 2019-10-12 17:48

import datetime
from django.db import migrations, models
from django.utils.timezone import utc


class Migration(migrations.Migration):

    dependencies = [
        ('transaction', '0002_transaction_user_id'),
    ]

    operations = [
        migrations.AlterField(
            model_name='transaction',
            name='date',
            field=models.DateField(blank=True, default=datetime.datetime(2019, 10, 12, 17, 48, 30, 854817, tzinfo=utc)),
        ),
    ]
