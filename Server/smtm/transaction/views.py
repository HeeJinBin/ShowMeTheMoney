from django.shortcuts import render
from rest_framework import viewsets
from .models import Transaction
from .serializers import TransactionSerializer

# Create your views here.
class TransactionViewSet(viewsets.ModelViewSet):
    queryset = Transaction.objects.all()
    serializer_class = TransactionSerializer