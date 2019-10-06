from rest_framework import serializers
from .models import Crawling

class CrawlingDataSerializer(serializers.ModelSerializer):
    class Meta:
        model = Crawling
        fields = '__all__'