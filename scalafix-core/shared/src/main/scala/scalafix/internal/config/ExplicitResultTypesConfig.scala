package scalafix.internal.config

import metaconfig._
import MetaconfigPendingUpstream.XtensionConfScalafix

case class ExplicitResultTypesConfig(
    memberKind: List[MemberKind] = Nil,
    memberVisibility: List[MemberVisibility] = Nil,
    skipSimpleDefinitions: Boolean = true,
    skipLocalImplicits: Boolean = true,
    // Experimental, still blocked by https://github.com/scalameta/scalameta/issues/1099
    // to work for defs. May insert names that conflicts with existing names in scope.
    // Use at your own risk.
    unsafeShortenNames: Boolean = false
) {
  implicit val reader: ConfDecoder[ExplicitResultTypesConfig] =
    ConfDecoder.instanceF[ExplicitResultTypesConfig] { c =>
      (
        c.getField(memberKind) |@|
          c.getField(memberVisibility) |@|
          c.getField(skipSimpleDefinitions) |@|
          c.getField(skipLocalImplicits) |@|
          c.getField(unsafeShortenNames)
      ).map {
        case ((((a, b), c), d), e) =>
          ExplicitResultTypesConfig(a, b, c, d, e)
      }
    }
}

object ExplicitResultTypesConfig {
  val default: ExplicitResultTypesConfig = ExplicitResultTypesConfig()
  implicit val reader: ConfDecoder[ExplicitResultTypesConfig] = default.reader
}

sealed trait MemberVisibility
object MemberVisibility {
  case object Public extends MemberVisibility
  case object Protected extends MemberVisibility
  case object Private extends MemberVisibility
  def all: List[MemberVisibility] =
    List(Public, Protected, Private)
  implicit val readerMemberVisibility: ConfDecoder[MemberVisibility] =
    ReaderUtil.fromMap(all.map(x => x.toString -> x).toMap)
}

sealed trait MemberKind
object MemberKind {
  case object Def extends MemberKind
  case object Val extends MemberKind
  case object Var extends MemberKind
  def all: List[MemberKind] =
    List(Def, Val, Var)
  implicit val readerMemberKind: ConfDecoder[MemberKind] =
    ReaderUtil.fromMap(all.map(x => x.toString -> x).toMap)
}
