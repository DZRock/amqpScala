package ru.dzdev.amqpScala.model

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._
/**
  * Created by dz on 23.03.17.
  */
case class User(var login: String, var password: String, var fullName:String) extends ActiveRecord

object User extends ActiveRecordCompanion[User]
