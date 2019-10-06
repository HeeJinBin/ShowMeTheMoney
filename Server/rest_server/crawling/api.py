from .models import Crawling
from rest_framework import serializers, viewsets

class CrawlingSerializer(serializers.ModelSerializer):

    class Meta:
        model = Crawling
        fields = '__all__'

class CrawlingViewSet(viewsets.ModelViewSet):
    queryset = Crawling.objects.all()
    serializer_class = CrawlingSerializer