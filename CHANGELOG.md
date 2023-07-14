# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Early Semantic Versioning](https://docs.scala-lang.org/overviews/core/binary-compatibility-for-library-authors.html#recommended-versioning-scheme) in addition to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.1.2] - 2023-07-14

### Added

- All relevant changes to `ScalacOptions` in [sbt-tpolecat](https://github.com/typelevel/sbt-tpolecat) since the previous release were added to the library, specifically:
  - [#115](https://github.com/typelevel/sbt-tpolecat/pull/115), which added the `-Wnonunit-statement` option.
  - [#127](https://github.com/typelevel/sbt-tpolecat/pull/127), which added verbose options to the plugin.
  - [#147](https://github.com/typelevel/sbt-tpolecat/pull/147), which added warnings which were reintroduced in Scala 3.3.x.

## [0.1.1] - 2022-08-04

### Fixed

- Added a singleton object `ScalacOptions` to enable access to the scala compiler options DSL. The previous code was imported directly from sbt-tpolecat and the `ScalacOptions` trait was package-private, making the library quite difficult to use!

## [0.1.0] - 2022-08-03

### Added

- Begin keeping this [CHANGELOG](./CHANGELOG.md).
- Initial release of this library using code extracted from [sbt-tpolecat](https://github.com/typelevel/sbt-tpolecat).

[Unreleased]: https://github.com/typelevel/scalac-options/compare/v0.1.2...HEAD
[0.1.2]: https://github.com/typelevel/scalac-options/compare/v0.1.1...v0.1.2
[0.1.1]: https://github.com/typelevel/scalac-options/compare/v0.1.0...v0.1.1
[0.1.0]: https://github.com/typelevel/scalac-options/releases/tag/v0.1.0
