package ru.dzdev.amqpScala.model

import com.github.aselab.activerecord._
import com.github.aselab.activerecord.dsl._
/**
  * Created by dz on 23.03.17.
  */
object Tables extends ActiveRecordTables{

  val users = table[User]

}
