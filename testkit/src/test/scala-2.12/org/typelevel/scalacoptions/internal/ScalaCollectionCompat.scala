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

import scala.collection.SortedSet
import scala.collection.SortedSetLike
import scala.language.implicitConversions

private[scalacoptions] trait ScalaCollectionCompat {
  import ScalaCollectionCompat._

  implicit def sortedSetCompatOps[A, C[a] <: SortedSet[a] with SortedSetLike[a, C[a]]](
    ca: C[A]
  ): SortedSetCompatOps[A, C[A]] =
    new SortedSetCompatOps(ca)
}

private[internal] object ScalaCollectionCompat {
  final class SortedSetCompatOps[A, C <: SortedSet[A] with SortedSetLike[A, C]](val ca: C)
    extends AnyVal {

    /** `SortedSet#from` is deprecated since Scala 2.13 and `rangeFrom` is recommended instead.
      */
    def rangeFrom(a: A): C = ca.from(a)

    /** `SortedSet#until` is deprecated since Scala 2.13 and `rangeUntil` is recommended instead.
      */
    def rangeUntil(a: A): C = ca.until(a)
  }
}
