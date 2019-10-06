# from django.shortcuts import render
# from rest_server.crawling.serializers import CrawlingDataSerializer
# from rest_framework.response import Response
# from rest_framework.permissions import IsAuthenticated
# from .models import Crawling
# from rest_framework import status, generics
#
# class CrawlingData(generics.GenericAPIView):
#     serializer_class = CrawlingDataSerializer
#     permission_classes = [IsAuthenticated,]
#
#     def get(self,request, format = None):
#         crawling_data = Crawling.objects.all()
#         serializer = CrawlingDataSerializer(crawling_data,many=True)
#         return Response(serializer.data)
#
#     def post(self,request, format = None):
#         serializer = CrawlingDataSerializer(data = request.data)
#         if serializer.is_valid():
#             serializer.save()
#             return Response(serializer.data,status = status.HTTP_201_CREATED)
#         return Response(serializer.errors, status = status.HTTP_400_BAD_REQUEST)
from rest_framework import viewsets
from .serializers import CrawlingDataSerializer
from .models import Crawling

class CrawlingViewSet(viewsets.ModelViewSet):
    queryset = Crawling.objects.all()
    serializer_class = CrawlingDataSerializer