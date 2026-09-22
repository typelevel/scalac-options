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

/** A collection of scalac options.
  *
  * @param rawOptions
  *   raw untyped scalac options groupped by their base name.
  *
  * @example
  *   The following list of options:
  *   {{{
  *   Seq(
  *     "-deprecation",
  *     "-feature:false",
  *     "-Xlint:deprecation,unused",
  *     "-Wconf:cat=lint&msg=hello:e,any:i",
  *     "-Xlint:_,-constant",
  *     "-Wunused"
  *   )
  *   }}}
  *   will be represented as:
  *   {{{
  *     Map(
  *       "deprecation" -> List(""),
  *       "feature" -> List("false"),
  *       "Xlint" -> List("deprecation", "unused", "_", "-constant"),
  *       "Wconf" -> List("msg=cat=lint&msg=hello", "any:i"),
  *       "Wunused" -> List("")
  *     )
  *   }}}
  *
  * @note
  *   Even a single option like `-Xlint` should be kept as a pair of `"Xlint" -> List("")`, because
  *   it can be important to parse groupped options correctly. It means that the underlying list
  *   cannot be empty. Thus, the question: can be consider `NonEmptyList` from cats for that?
  */
final class ScalacOptions(rawOptions: Map[String, List[String]]) {

  def get[A](implicit opt: ScalacOption[A], sel: ScalacOption.Select[A]): Option[sel.Out] = {
    rawOptions
      .get(opt.baseName)
      .flatMap { sel.parse(_) }
  }
}
