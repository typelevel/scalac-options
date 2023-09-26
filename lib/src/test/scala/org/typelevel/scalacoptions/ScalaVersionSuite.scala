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

import org.scalacheck.Gen
import org.scalacheck.Prop.forAll

class ScalaVersionSuite extends munit.ScalaCheckSuite {
  val versionGen = Gen.chooseNum(0L, 20L)

  property("fromString parse a version") {
    forAll(versionGen, versionGen, versionGen) {
      (currentMaj: Long, currentMin: Long, currentPatch: Long) =>
        val version = ScalaVersion.fromString(s"$currentMaj.$currentMin.$currentPatch")
        assertEquals(version, Right(ScalaVersion(currentMaj, currentMin, currentPatch)))
    }
  }

  property("fromString parse an RC version") {
    forAll(versionGen, versionGen, versionGen) {
      (currentMaj: Long, currentMin: Long, currentPatch: Long) =>
        val version = ScalaVersion.fromString(s"$currentMaj.$currentMin.$currentPatch-RC3")
        assertEquals(version, Right(ScalaVersion(currentMaj, currentMin, currentPatch)))
    }
  }

  property("fromString parse a NIGHTLY version") {
    forAll(versionGen, versionGen, versionGen) {
      (currentMaj: Long, currentMin: Long, currentPatch: Long) =>
        val version = ScalaVersion.fromString(
          s"$currentMaj.$currentMin.$currentPatch-RC1-bin-20231106-f61026d-NIGHTLY"
        )
        assertEquals(version, Right(ScalaVersion(currentMaj, currentMin, currentPatch)))
    }
  }

  property("fromString parse a commit version") {
    forAll(versionGen, versionGen, versionGen) {
      (currentMaj: Long, currentMin: Long, currentPatch: Long) =>
        val version = ScalaVersion.fromString(s"$currentMaj.$currentMin.$currentPatch-bin-80514f7")
        assertEquals(version, Right(ScalaVersion(currentMaj, currentMin, currentPatch)))
    }
  }
}
