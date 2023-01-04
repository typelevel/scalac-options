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

import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalacheck.Prop

class ParserSuite extends munit.ScalaCheckSuite {
  property("Parser.parseGenericVersion should succeed for valid version strings") {
    val ulongGen: Gen[Long] = Gen.chooseNum[Long](0L, Long.MaxValue)
    Prop.forAll(ulongGen, ulongGen, ulongGen) { (major, minor, patch) =>
      assertEquals(
        Parser.parseGenericVersion(s"$major.$minor.$patch"),
        Some((major, minor, patch))
      )
    }
  }
  property("Parser.parseGenericVersion should None for invalid version strings") {
    val gen =
      Arbitrary
        .arbitrary[String]
        .filterNot(Parser.genericVersionRe.pattern.matcher(_).matches())

    Prop.forAll(gen) { s =>
      assert(Parser.parseGenericVersion(s).isEmpty)
    }
  }
}
