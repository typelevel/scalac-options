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
import org.scalacheck.Shrink

package object testkit {

  /** Emits one of the known Scala versions.
    */
  lazy val knownScalaVersionGen: Gen[ScalaVersion] = Gen.oneOf(ScalaVersion.knownVersions)

  implicit lazy val scalaVersionArbitrary: Arbitrary[ScalaVersion] =
    Arbitrary(knownScalaVersionGen)

  // TODO: Shrinking is currently suppressed.
  //       Consider implementing by shrinking the `patch` version down to 0.
  implicit lazy val scalaVersionShrink: Shrink[ScalaVersion] = Shrink.shrinkAny
}
