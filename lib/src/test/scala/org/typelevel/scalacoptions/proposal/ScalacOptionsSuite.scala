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

package org.typelevel.scalacoptions.proposal

import org.scalacheck.Arbitrary
import org.scalacheck.Gen
import org.scalacheck.Prop
import org.typelevel.scalacoptions.ScalaVersion

class ScalacOptionsSuite extends munit.ScalaCheckSuite {
  import ScalacOptionsSuite._

  private val arbStrGen = Arbitrary.arbitrary[String]
  // uncomment to simplify debugging
  // TODO: remove!
  // private val arbStrGen = Gen.asciiPrintableStr

  test("ScalacOptions.get should work for ScalacOption.Single") {
    val gen =
      for {
        targetBaseName    <- arbStrGen
        targetResultValue <- arbStrGen
        targetOtherValues <-
          Gen.listOf(
            Gen.oneOf(
              arbStrGen,
              arbStrGen.map(targetResultValue + _)
            )
          )
        otherOptions <- Gen.mapOf(Gen.zip(arbStrGen, Gen.listOf(arbStrGen)))
      } yield {
        (
          targetBaseName,
          targetResultValue,
          otherOptions + (targetBaseName -> (targetOtherValues :+ targetResultValue))
        )
      }

    Prop.forAll(gen) { case (targetBaseName, targetResultValue, allOptions) =>
      implicit val singleScalacOption: ScalacOption.Aux[TestOption, ScalacOption.Single] =
        new ScalacOption[TestOption] {
          type Type = ScalacOption.Single

          override def isSupported(sv: ScalaVersion): Boolean = ??? // not a subject for testing

          override def baseName: String = targetBaseName

          override def parse(rawValue: String): Option[TestOption] = {
            if (rawValue.startsWith(targetResultValue))
              Some(TestOption(rawValue))
            else
              None
          }
        }

      val obtained = new ScalacOptions(allOptions).get[TestOption].map(_.value)

      assertEquals(obtained, Some(targetResultValue))
    }
  }
}

object ScalacOptionsSuite {
  final case class TestOption(value: String) extends AnyVal
}
