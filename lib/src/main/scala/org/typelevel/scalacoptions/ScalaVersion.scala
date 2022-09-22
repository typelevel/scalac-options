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

import org.typelevel.scalacoptions.internal.Parser

import scala.Ordering.Implicits._
import scala.collection.immutable

abstract sealed case class ScalaVersion private (major: Long, minor: Long, patch: Long) {

  def isBetween(addedVersion: ScalaVersion, removedVersion: ScalaVersion): Boolean =
    this >= addedVersion && this < removedVersion
}

object ScalaVersion {
  private def apply(major: Long, minor: Long, patch: Long): ScalaVersion =
    new ScalaVersion(major, minor, patch) {}

  lazy val knownVersions: immutable.SortedSet[ScalaVersion] = {
    val bld = immutable.SortedSet.newBuilder[ScalaVersion]

    def bldPatches(major: Long, minor: Long, lastPatch: Long): Unit =
      for (patch <- 0L to lastPatch)
        bld += ScalaVersion(major, minor, patch)

    bldPatches(2, 11, 12)
    bldPatches(2, 12, 17)
    bldPatches(2, 13, 9)
    bldPatches(3, 0, 2)
    bldPatches(3, 1, 3)
    bldPatches(3, 2, 0)

    bld.result()
  }

  def from(major: Long, minor: Long, patch: Long): Option[ScalaVersion] = {
    val candidate = ScalaVersion(major, minor, patch)
    if (knownVersions.contains(candidate)) Some(candidate) else None
  }

  def unsafeFrom(major: Long, minor: Long, patch: Long): ScalaVersion =
    from(major, minor, patch).getOrElse(throw invalidScalaVersionError(s"$major.$minor.$patch"))

  object fromString {
    def apply(s: String): Option[ScalaVersion] =
      Parser.parseGenericVersion(s).flatMap(Function.tupled(from _))
    def unapply(s: String): Option[ScalaVersion] = apply(s)

    def unsafe(s: String): ScalaVersion = apply(s).getOrElse(throw invalidScalaVersionError(s))
  }

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
  val V3_0_0   = ScalaVersion(3, 0, 0)
  val V3_1_0   = ScalaVersion(3, 1, 0)

  implicit lazy val scalaVersionOrdering: Ordering[ScalaVersion] =
    Ordering.by(version => (version.major, version.minor, version.patch))

  private def invalidScalaVersionError(s: String) =
    new IllegalArgumentException(s"invalid Scala version: $s")
}
