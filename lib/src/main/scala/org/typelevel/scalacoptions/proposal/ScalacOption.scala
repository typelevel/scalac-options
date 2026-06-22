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

import org.typelevel.scalacoptions.ScalaVersion

trait ScalacOption[A] {
  type Type <: ScalacOption.Type

  // ..or `isSupportedFor` maybe?
  def isSupported(sv: ScalaVersion): Boolean

  /** Base option name.
    *
    * @return
    *   - for `"-deprecation"` results to `"deprecation"`
    *   - for `"-deprecation:true"` results to `"deprecation"`
    *   - for `"-Xlint"` results to `"Xlint"`
    *   - for `"-Xlint:deprecation"` results to `"Xlint"`
    */
  def baseName: String

  // /** Full option name.
  //   *
  //   * @return
  //   *   - for `"-deprecation"` results to `"deprecation"`
  //   *   - for `"-deprecation:true"` results to `"deprecation"` (same as above!)
  //   *   - for `"-Xlint"` results to `"Xlint"`
  //   *   - for `"-Xlint:deprecation"` results to `"Xlint:deprecation"`
  //   */
  // def fullName: String

  /** Parses the option from a raw value found for the option's base name.
    *
    * @param rawValue
    *   a raw option value, can be empty strings for options like `-deprecation`
    *
    * @return
    *   - `None` if the value does not belong to the option and thus cannot be parsed, i.e. when
    *     `"unused"` is passed where `-Xlint:deprecation` model is expected;
    *   - `Some(A)` when a correct value is passed for the option, e.g. both `"unused"` and
    *     `"-unused"` will be accepted for the an option type class that models the `-Xlint:unused`
    *     option.
    *
    * @note
    *   It deliberately made not returning any failure type like `ParseFailure`, becase
    *   `ScalacOption.Select` is responsible for that.
    */
  def parse(rawValue: String): Option[A]
}

object ScalacOption {
  type Aux[A, T <: Type] = ScalacOption[A] { type Type = T }

  // Note: initially I thought that Singe/Recurring might not be necessary.
  //       But later I realized that `Recurring` can be useful for modeling such options like "-Wconf"
  sealed trait Type
  abstract final class Single private ()    extends Type // never instantiated
  abstract final class Recurring private () extends Type // never instantiated

  sealed trait Select[A] {
    // Not sure why it is a higher kinded type in http4s' Header.
    // A regular plain type seems to be just enough here.
    type Out

    /** Parses all raw option values groupped by their [[ScalacOption.baseName]].
      *
      * @param rawValues
      *   a list of raw option values in the order they occured in a command line.
      */
    def parse(rawValues: List[String]): Option[Out]
  }

  object Select {
    implicit def singleScalacOption[A](implicit
      so: ScalacOption.Aux[A, Single]
    ): Select[A] { type Out = A } =
      new Select[A] {
        type Out = A

        def parse(rawValues: List[String]): Option[A] = {
          // Assume for now that for a single-occuring option the last value occured overrides
          // all previous values. E.g.: for `Seq("-feature", "-feature:false")` the last one should
          // take effect.
          rawValues.iterator.flatMap(so.parse).toList.lastOption
        }
      }

    implicit def recurringScalacOption[A](implicit
      so: ScalacOption.Aux[A, Recurring]
    ): Select[A] { type Out = List[A] } =
      new Select[A] {
        type Out = List[A] // consider NonEmptyList

        def parse(rawValues: List[String]): Option[List[A]] = {
          // As simple as it is (but with NonEmptyList it would be even simpler).
          rawValues.iterator.flatMap(so.parse).toList match {
            case Nil => None
            case nel => Some(nel) // the result cannot be an empty list!
          }
        }
      }
  }
}
