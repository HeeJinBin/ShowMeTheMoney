"""rest_server URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/2.2/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path, include

from rest_framework import routers
from rest_framework_swagger.views import get_swagger_view

import crawling.api
#import crawling.views
from django.conf.urls import url

from crawling.views import CrawlingViewSet
app_name = 'crawling'
app_name_API = 'api'
router = routers.DefaultRouter()
router.register('crawling',CrawlingViewSet)

urlpatterns = [
    path('admin/', admin.site.urls),
    path('api/doc',get_swagger_view(title='REST API DOCS')),
    path('api/v1/',include((router.urls,'crawling'),app_name)),
    #path(r'^api/v1',include('router.urls')),
    #
    # url(r'^api/doc',get_swagger_view(title='REST API DOCS')),
    # url(r'^api/v1/',include((router.urls,'crawling'),app_name_API)),
    #
    # path('main/',include('main.urls',namespace='main'))
    #
    # app_name = 'main'
    # include('main.urls',app)
]
