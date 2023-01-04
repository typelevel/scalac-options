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

import literals._

class literalsSuite extends munit.FunSuite {

  // Compile-time checks are only possible.
  test("""literals.sver: correct literals should compile successfully""") {
    assertEquals(sver"2.12.1", ScalaVersion.unsafeFrom(2, 12, 1))
    assertEquals(sver"2.12.17", ScalaVersion.unsafeFrom(2, 12, 17))
    assertEquals(sver"2.13.1", ScalaVersion.unsafeFrom(2, 13, 1))
    assertEquals(sver"2.13.8", ScalaVersion.unsafeFrom(2, 13, 8))
    assertEquals(sver"3.0.0", ScalaVersion.unsafeFrom(3, 0, 0))
    assertEquals(sver"3.1.3", ScalaVersion.unsafeFrom(3, 1, 3))
  }
  test("literals.sver: incorrect literals should fail to compile") {
    def check(error: String) =
      // assertNoDiff does not work for Scala3.
      assert(error.contains("error: incorrect or unsupported Scala version"))

    check(compileErrors("""sver"""""))
    check(compileErrors("""sver"1""""))
    check(compileErrors("""sver"1.2""""))
    check(compileErrors("""sver"1.2.3""""))
    check(compileErrors("""sver"1.2.3.4""""))
  }
}
