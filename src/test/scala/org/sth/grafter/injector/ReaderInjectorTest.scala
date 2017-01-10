package org.sth.grafter.injector

import org.zalando.grafter.macros.{reader, readers}
import org.zalando.grafter.GenericReader._

@readers
case class AppConfig()

@reader[AppConfig]
case class Tester1()

@reader[AppConfig]
case class Tester2()
object Tester2 {
  def foo = "foo"
}

object ReaderInjectorTest {
  // these lines will not compile without the plugin
  val r1 = Tester1.reader
  val r2 = Tester2.reader
}
