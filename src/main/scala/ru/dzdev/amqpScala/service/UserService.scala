package ru.dzdev.amqpScala.service

import ru.dzdev.amqpScala.model.User
import com.github.aselab.activerecord.dsl._

/**
  * Created by dz on 23.03.17.
  */
class UserService extends IUserService{

  override def saveUser(user: User): Unit = {
    User.inTransaction(
      user.save()
    )
  }

  override def getUser(id: Int): User = {
    User.find(id).get
  }

  override def getUsers: List[User] = {
    User.all.toList
  }

  override def updateUser(id: Int, user: User): Unit = {
    val exUser = User.find(id).get
    exUser.login = user.login
    exUser.password = user.password
    exUser.fullName = user.fullName
    User.inTransaction(
      exUser.save()
    )
  }
}
