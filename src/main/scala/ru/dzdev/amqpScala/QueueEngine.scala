package ru.dzdev.amqpScala

import java.util.UUID

import akka.actor.{ActorRef, ActorSystem}
import com.github.sstone.amqp.Amqp.{ChannelParameters, Delivery, QueueParameters, StandardExchanges}
import com.github.sstone.amqp.RpcServer.{IProcessor, ProcessResult}
import com.github.sstone.amqp.{ConnectionOwner, RpcServer}
import com.rabbitmq.client.ConnectionFactory
import org.slf4j.Logger
import ru.dzdev.amqpScala.controller.RequestController
import ru.dzdev.amqpScala.model.Tables
import ru.dzdev.amqpScala.util.QueueListener

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._
/**
  * Created by dz on 23.03.17.
  */
object QueueEngine {

  implicit val system = ActorSystem("mySystem")

  def init : Unit = {
    Tables.initialize

    val connFactory = new ConnectionFactory()
    connFactory.setUri("amqp://guest:guest@localhost")
    val conn = system.actorOf(ConnectionOwner.props(connFactory, 1 second))

    val clazz = classOf[RequestController]
    clazz
      .getDeclaredMethods
      .filter(method => {
        method.getAnnotation(classOf[QueueListener]) != null
      })
      .foreach(method => {
        val queueName = method.getAnnotation(classOf[QueueListener]).queueName()
        println(s"Binding '${method.getName}' to queue '${queueName}'")

        def callbackWrapper(delivery: Delivery): Future[ProcessResult] ={
          method.invoke(clazz.newInstance(),delivery).asInstanceOf[Future[ProcessResult]]
        }
        createQueueListener(conn, callbackWrapper, system, queueName)
      })
  }

  def createQueueListener(actor: ActorRef,
                          callback: (Delivery) => Future[ProcessResult],
                          system: ActorSystem,
                          queueName: String
                         ): Unit ={
    val queueParams = QueueParameters(queueName, passive = false, durable = true, exclusive = false, autodelete = false)

    val rpcServers = for (i <- 1 to 5) yield {
      val processor = new IProcessor {
        def process(delivery: Delivery) = callback(delivery)
        def onFailure(delivery: Delivery, e: Throwable) = ProcessResult(None)
      }
      ConnectionOwner.createChildActor(actor,
        RpcServer.props(queueParams,
          StandardExchanges.amqDirect,
          UUID.randomUUID().toString,
          processor,
          ChannelParameters(qos = 1)
        )
      )
    }
  }
}
