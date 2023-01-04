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

import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll
import org.typelevel.scalacoptions.testkit._

import scala.Ordering.Implicits._

class ScalacOptionSuite extends munit.ScalaCheckSuite with internal.ScalaCollectionCompat {

  private val optionNameGen = Arbitrary.arbitrary[String]
  private val versionsPairGen = {
    // Make sure there will be at least one older version available.
    val secondVersion = ScalaVersion.knownVersions.tail.head // we know for sure it is there
    for {
      newerVersion <- Gen.oneOf(ScalaVersion.knownVersions.rangeFrom(secondVersion))
      olderVersion <- Gen.oneOf(ScalaVersion.knownVersions.rangeUntil(newerVersion))
    } yield (olderVersion, newerVersion)
  }

  property("valid when no version predicate") {
    forAll { (optionName: String, currentVersion: ScalaVersion) =>
      val scalacOption = ScalacOption(optionName, _ => true)
      assert(
        scalacOption.isSupported(currentVersion),
        "Should be valid when neither addedIn nor removedIn"
      )
    }
  }

  property("valid when added in a past release") {
    forAll(optionNameGen, versionsPairGen) { case (optionName, (addedVersion, currentVersion)) =>
      val scalacOption = ScalacOption(optionName, version => version >= addedVersion)
      assert(
        scalacOption.isSupported(currentVersion),
        "Should be valid when addedIn matches past major release"
      )
    }
  }

  property("valid when added in this minor/patch release") {
    forAll { (optionName: String, currentVersion: ScalaVersion) =>
      val scalacOption = ScalacOption(optionName, version => version >= currentVersion)
      assert(
        scalacOption.isSupported(currentVersion),
        "Should be valid when addedIn matches this minor/patch release"
      )
    }
  }

  property("not valid when added in a future release") {
    forAll(optionNameGen, versionsPairGen) { case (optionName, (currentVersion, addedVersion)) =>
      val scalacOption = ScalacOption(optionName, version => version >= addedVersion)
      assert(
        !scalacOption.isSupported(currentVersion),
        "Should not be valid when addedIn matches a future major release"
      )
    }
  }

  property("valid when removed in a future release") {
    forAll(optionNameGen, versionsPairGen) { case (optionName, (currentVersion, removedVersion)) =>
      val scalacOption = ScalacOption(optionName, version => version < removedVersion)
      assert(
        scalacOption.isSupported(currentVersion),
        "Should be valid when removedIn matches next major release"
      )
    }
  }

  property("not valid when removed in this release") {
    forAll { (optionName: String, currentVersion: ScalaVersion) =>
      val scalacOption = ScalacOption(optionName, version => version < currentVersion)
      assert(
        !scalacOption.isSupported(currentVersion),
        "Should not be valid when removedIn matches this minor/patch release"
      )
    }
  }

  property("not valid when removed in a past release") {
    forAll(optionNameGen, versionsPairGen) { case (optionName, (removedVersion, currentVersion)) =>
      val scalacOption = ScalacOption(optionName, version => version < removedVersion)
      assert(
        !scalacOption.isSupported(currentVersion),
        "Should not be valid when removedIn matches an old major release"
      )
    }
  }
}
