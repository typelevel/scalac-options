# scalac-options

[![Continuous Integration](https://github.com/typelevel/scalac-options/actions/workflows/ci.yml/badge.svg)](https://github.com/typelevel/scalac-options/actions/workflows/ci.yml)
[![License](https://img.shields.io/github/license/typelevel/scalac-options.svg)](https://opensource.org/licenses/Apache-2.0)
[![Discord](https://img.shields.io/discord/632277896739946517.svg?label=&logo=discord&logoColor=ffffff&color=404244&labelColor=6A7EC2)](https://discord.gg/D7wY3aH7BQ)
[![Maven Central](https://img.shields.io/maven-central/v/org.typelevel/scalac-options_3)](https://search.maven.org/artifact/org.typelevel/scalac-options_3)

*scalac-options* is a library containing logic for configuring Scala compiler options according to the current Scala compiler version.

This logic was originally developed in the [sbt-tpolecat](https://github.com/typelevel/sbt-tpolecat) sbt plugin, and this library is intended to enable the reuse of that logic in other build tool plugins, for example [sbt-typelevel](https://github.com/typelevel/sbt-typelevel) and [mill-tpolecat](https://github.com/DavidGregory084/mill-tpolecat).

## Usage

This library is published for Scala 2.12.x, 2.13.x and 3.1.x:

```scala
// sbt
"org.typelevel" %% "scalac-options" % "0.1.2"

// mill
ivy"org.typelevel::scalac-options:0.1.2"

// Scala CLI
//> using lib "org.typelevel::scalac-options:0.1.2"
```

This library offers functions for filtering proposed Scala compiler options according to Scala version:

```scala
scalacOptions := ScalacOptions.tokensForVersion(
  ScalaVersion.V3_1_0, // the Scala compiler major, minor, patch version
  ScalacOptions.default // a curated default option set
) // returns a Seq[String] of Scala compiler options

ScalacOptions.optionsForVersion(
  ScalaVersion.V3_1_0,
  ScalacOptions.default
) // returns a Set[ScalacOption]
```

A shorthand for using the default option set is also provided:

```scala
scalacOptions := ScalacOptions.defaultTokensForVersion(
  ScalaVersion.V3_1_0
) // returns a Seq[String] of Scala compiler options based on the default option set

ScalacOptions.defaultOptionsForVersion(
  ScalaVersion.V3_1_0
) // returns a Set[ScalacOption] based on the default option set
```

### Conduct

Participants are expected to follow the [Scala Code of Conduct](https://www.scala-lang.org/conduct/) while discussing the project on GitHub and any other venues associated with the project. See the [organizational Code of Conduct](https://github.com/typelevel/.github/blob/main/CODE_OF_CONDUCT.md) for more details.

### License

All code in this repository is licensed under the Apache License, Version 2.0. See [LICENSE](./LICENSE).
