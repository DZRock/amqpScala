package ru.dzdev.amqpScala.service

import ru.dzdev.amqpScala.model.User

/**
  * Created by dz on 23.03.17.
  */
trait IUserService {

  def saveUser(user: User): Unit
  def getUser(id: Int): User
  def getUsers: List[User]
  def updateUser(id: Int, user: User): Unit
}
