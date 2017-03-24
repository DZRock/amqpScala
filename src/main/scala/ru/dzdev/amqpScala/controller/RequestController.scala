package ru.dzdev.amqpScala.controller

import com.github.sstone.amqp.Amqp.Delivery
import com.github.sstone.amqp.RpcServer.ProcessResult
import org.json4s.NoTypeHints
import org.json4s.native.Serialization
import org.json4s.native.Serialization.{read, write}
import ru.dzdev.amqpScala.dto.{UserDTO, Wrapper}
import ru.dzdev.amqpScala.model.User
import ru.dzdev.amqpScala.service.{AppModule, IUserService}
import ru.dzdev.amqpScala.util.QueueListener
import scaldi.{Injectable, Injector}

import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

/**
  * Created by dz on 23.03.17.
  */
class RequestController extends Injectable{

  implicit val formats = Serialization.formats(NoTypeHints)
  implicit val injector: Injector = new AppModule
  val userService:IUserService = inject[IUserService]

  @QueueListener(queueName = "queue_micro")
  def newUserFunc(delivery: Delivery) = {
    val user = read[UserDTO](new String(delivery.body.map(_.toChar)))
    userService.saveUser(User(user.login,user.password,user.fullName))
    val response: Wrapper[UserDTO] = Wrapper("100",null,"success")
    Future(ProcessResult(Some(write(response).getBytes)))
  }

  @QueueListener(queueName = "getUsers")
  def getUsers(delivery: Delivery) = {
    val users = ListBuffer[UserDTO]()
    userService.getUsers.foreach(user =>{
      users += UserDTO(user.login,user.password,user.fullName)
    })
    val response: Wrapper[ListBuffer[UserDTO]] = Wrapper("100", users, "success")
    Future(ProcessResult(Some(write(response).getBytes)))
  }

}
