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

import scala.Ordering.Implicits._

case class ScalaVersion(major: Long, minor: Long, patch: Long) {
  def isBetween(addedVersion: ScalaVersion, removedVersion: ScalaVersion): Boolean =
    this >= addedVersion && this < removedVersion

  def isAtLeast(addedVersion: ScalaVersion): Boolean =
    this >= addedVersion

  override def toString: String =
    s"$major.$minor.$patch"
}

object ScalaVersion {
  val V2_11_0  = ScalaVersion(2, 11, 0)
  val V2_11_11 = ScalaVersion(2, 11, 11)
  val V2_12_0  = ScalaVersion(2, 12, 0)
  val V2_12_2  = ScalaVersion(2, 12, 2)
  val V2_12_5  = ScalaVersion(2, 12, 5)
  val V2_12_13 = ScalaVersion(2, 12, 13)
  val V2_13_0  = ScalaVersion(2, 13, 0)
  val V2_13_2  = ScalaVersion(2, 13, 2)
  val V2_13_3  = ScalaVersion(2, 13, 3)
  val V2_13_4  = ScalaVersion(2, 13, 4)
  val V2_13_5  = ScalaVersion(2, 13, 5)
  val V2_13_6  = ScalaVersion(2, 13, 6)
  val V2_13_9  = ScalaVersion(2, 13, 9)
  val V3_0_0   = ScalaVersion(3, 0, 0)
  val V3_1_0   = ScalaVersion(3, 1, 0)
  val V3_1_2   = ScalaVersion(3, 1, 2)
  val V3_3_0   = ScalaVersion(3, 3, 0)
  val V3_3_1   = ScalaVersion(3, 3, 1)
  val V3_3_3   = ScalaVersion(3, 3, 3)
  val V3_3_5   = ScalaVersion(3, 3, 5)
  val V3_4_0   = ScalaVersion(3, 4, 0)
  val V3_5_0   = ScalaVersion(3, 5, 0)
  val V3_5_2   = ScalaVersion(3, 5, 2)

  private val versionRegex = raw"""(\d+)\.(\d+)\.(\d+)(?:-.*)?""".r
  def fromString(version: String): Either[IllegalArgumentException, ScalaVersion] =
    version match {
      case versionRegex(major, minor, patch) =>
        Right(ScalaVersion(major.toLong, minor.toLong, patch.toLong))
      case _ => Left(new IllegalArgumentException(s"Scala version $version not recognized"))
    }

  def unsafeFromString(version: String): ScalaVersion =
    fromString(version).fold(throw _, identity)

  implicit val scalaVersionOrdering: Ordering[ScalaVersion] =
    Ordering.by(version => (version.major, version.minor, version.patch))
}
