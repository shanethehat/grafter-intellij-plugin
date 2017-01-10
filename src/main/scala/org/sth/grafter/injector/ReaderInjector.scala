package org.sth.grafter.injector

import com.intellij.openapi.diagnostic.Logger
import org.jetbrains.plugins.scala.lang.psi.api.base.types.ScParameterizedTypeElement
import org.jetbrains.plugins.scala.lang.psi.api.expr.ScAnnotation
import org.jetbrains.plugins.scala.lang.psi.api.toplevel.typedef.{ScClass, ScObject, ScTypeDefinition}
import org.jetbrains.plugins.scala.lang.psi.impl.toplevel.typedef.SyntheticMembersInjector


class ReaderInjector extends SyntheticMembersInjector {

  val readerAnnotation = "org.zalando.grafter.macros.reader"

  override def injectFunctions(source: ScTypeDefinition): Seq[String] = {
    source match {
      case obj: ScObject => obj.fakeCompanionClassOrCompanionClass match {
        case clazz: ScClass =>
          Option(clazz.findAnnotation(readerAnnotation)).collect {
            case annotation: ScAnnotation => createReader(clazz, annotation)
          }.getOrElse(Seq.empty)
        case _ => Seq.empty
      }
      case _ => Seq.empty
    }
  }

  private def createReader(c: ScClass, a: ScAnnotation): Seq[String] = {
    a.typeElement match {
      case e: ScParameterizedTypeElement =>
        e.typeArgList.typeArgs.headOption.map { t =>
          val aType = t.getType().get.presentableText
          Seq(s"implicit def reader: cats.data.Reader[$aType, ${c.getQualifiedName}] = genericReader")
        }.getOrElse(Seq.empty)
      case _ => Seq.empty
    }
  }
}