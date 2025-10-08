from django.urls import path, include
from rest_framework.routers import DefaultRouter

# Routers for viewsets will be registered here as we implement APIs
router = DefaultRouter()

urlpatterns = [
    path('', include(router.urls)),
]

