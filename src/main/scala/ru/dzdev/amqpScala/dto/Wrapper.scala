package ru.dzdev.amqpScala.dto

/**
  * Created by dz on 22.03.17.
  */
case class Wrapper[T](code:String, content: T, message: String)
