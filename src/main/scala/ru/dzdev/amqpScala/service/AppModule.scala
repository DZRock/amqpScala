package ru.dzdev.amqpScala.service

import scaldi.Module

/**
  * Created by dz on 23.03.17.
  */
class AppModule extends Module{

  bind [IUserService] to new UserService

}
