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

package org.typelevel.scalacoptions.internal

private[scalacoptions] object Parser {
  private[internal] lazy val genericVersionRe = """^(0|[1-9]\d*)\.(0|[1-9]\d*)\.(0|[1-9]\d*)$""".r

  private object asLong {
    def unapply(s: String): Option[Long] = {
      try {
        Some(java.lang.Long.parseUnsignedLong(s))
      } catch {
        case _: NumberFormatException => None
      }
    }
  }

  def parseGenericVersion(s: String): Option[(Long, Long, Long)] = s match {
    case genericVersionRe(asLong(major), asLong(minor), asLong(patch)) =>
      Some((major, minor, patch))
    case _ =>
      None
  }
}
