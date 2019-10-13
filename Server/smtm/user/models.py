from django.contrib.auth.models import AbstractBaseUser, BaseUserManager, PermissionsMixin
from django.db import models

class UserManager(BaseUserManager):

    use_in_migrations = True

    def create_user(self, user_id, password=None):
        if not user_id:
            raise ValueError('User must have user ID')
        user = self.model(
            user_id = user_id
        )
        user.set_password(password)
        user.save(using=self._db)
        return user

    def create_superuser(self, user_id, password):
        user = self.create_user(
            user_id = user_id,
            password=password
        )
        user.is_admin = True
        user.is_superuser = True
        user.is_staff = True
        user.save(using=self._db)
        return user


class User(AbstractBaseUser, PermissionsMixin):

    objects = UserManager()

    user_id = models.CharField(max_length=20,unique=True)
    password = models.CharField(max_length=20,null=False)
    nickname = models.CharField(max_length=20, null=False)
    email_1 = models.EmailField(max_length = 255, null=False)
    pw_1 = models.CharField(max_length = 20,null=False)
    email_2 = models.EmailField(max_length=255)
    pw_2 = models.EmailField(max_length=20)
    email_3 = models.EmailField(max_length=255)
    pw_3  = models.CharField(max_length=20)

    is_active = models.BooleanField(default=True)
    is_admin = models.BooleanField(default=False)
    is_superuser = models.BooleanField(default=False)
    is_staff = models.BooleanField(default=False)
    date_joined = models.DateTimeField(auto_now_add=True)

    USERNAME_FIELD = 'user_id'
    REQUIRED_FIELDS = []




