package org.sth.grafter.injector

import org.zalando.grafter.macros.reader

object ReaderInjectorTest {

  case class AppConfig()

  @reader[AppConfig]
  case class Tester1()

  @reader[AppConfig]
  case class Tester2()
  object Tester2 {
    def foo = "foo"
  }

  // these lines will not compile without the plugin
  val r1 = Tester1.reader
  val r2 = Tester2.reader

}
