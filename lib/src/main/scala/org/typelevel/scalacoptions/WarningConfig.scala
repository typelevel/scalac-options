/*
 * Copyright 2022 Typelevel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.typelevel.scalacoptions

sealed trait WarningAction

case object silent extends WarningAction
case object error  extends WarningAction

sealed trait WarningActionWithVerbosity extends WarningAction { action =>

  def summary: WarningAction = new WarningAction {
    override def toString = s"$action-summary"
  }

  def verbose: WarningAction = new WarningAction {
    override def toString = s"$action-verbose"
  }
}

case object warning extends WarningActionWithVerbosity
case object info    extends WarningActionWithVerbosity

sealed class WarningFilter(val asString: String) {
  override def toString: String = asString

  def &(that: WarningFilter): WarningFilter = new WarningFilter(
    Seq(this.asString, that.asString).mkString("&")
  )
}

case object any                     extends WarningFilter("any")
final case class cat(cat: String)   extends WarningFilter(s"cat=$cat")
final case class msg(regex: String) extends WarningFilter(s"msg=$regex")
final case class src(regex: String) extends WarningFilter(s"src=$regex")

final case class origin(segments: String*)
  extends WarningFilter(s"origin=${segments.mkString(raw"\.")}")

final case class WarningConfig(rules: (WarningFilter, WarningAction)*) {

  override def toString: String = {
    val conf = rules.toSeq
      .map { case (filter, action) => Seq(filter, action).mkString(":") }
      .mkString(",")
    s"-Wconf:$conf"
  }

  def asScalacOption: ScalacOption =
    ScalacOption(
      option = this.toString,
      isSupported = {
        case ScalaVersion(2, 12, x) => x >= 13
        case ScalaVersion(2, 13, x) => x >= 2
        case _                      => false
      }
    )
}
