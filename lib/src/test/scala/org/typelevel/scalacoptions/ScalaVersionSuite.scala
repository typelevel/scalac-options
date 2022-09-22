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

import org.typelevel.scalacoptions.testkit._
import org.scalacheck.Prop

class ScalaVersionSuite extends munit.ScalaCheckSuite {
  property("ScalaVersion#from/unsafeFrom can create a known version") {
    Prop.forAll(knownScalaVersionGen) { case expected @ ScalaVersion(major, minor, patch) =>
      assertEquals(ScalaVersion.from(major, minor, patch), Some(expected))
      assertEquals(ScalaVersion.unsafeFrom(major, minor, patch), expected)
    }
  }
  property("ScalaVersion#from/unsafeFrom are consistent with each other") {
    Prop.forAll { (major: Long, minor: Long, patch: Long) =>
      ScalaVersion.from(major, minor, patch) match {
        case Some(expected) =>
          assertEquals(ScalaVersion.unsafeFrom(major, minor, patch), expected)
        case None =>
          val expectedMessage = s"invalid Scala version: $major.$minor.$patch"
          interceptMessage[IllegalArgumentException](expectedMessage) {
            ScalaVersion.unsafeFrom(major, minor, patch)
          }
          ()
      }
    }
  }
  property("ScalaVersion.fromString.apply/unsafe can create a known version") {
    Prop.forAll(knownScalaVersionGen) { case expected @ ScalaVersion(major, minor, patch) =>
      val s = s"$major.$minor.$patch"
      assertEquals(ScalaVersion.fromString(s), Some(expected))
      assertEquals(ScalaVersion.fromString.unsafe(s), expected)
      s match {
        case ScalaVersion.fromString(obtained) => assertEquals(obtained, expected)
        case _                                 => fail("must not happen")
      }
    }
  }
}
