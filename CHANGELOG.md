# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Early Semantic Versioning](https://docs.scala-lang.org/overviews/core/binary-compatibility-for-library-authors.html#recommended-versioning-scheme) in addition to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.1.1] - 2022-08-03

### Fixed

- Added a singleton object `ScalacOptions` to enable access to the scala compiler options DSL. The previous code was imported directly from sbt-tpolecat and the `ScalacOptions` trait was package-private, making the library quite difficult to use!

## [0.1.0] - 2022-08-03

### Added

- Begin keeping this [CHANGELOG](./CHANGELOG.md).
- Initial release of this library using code extracted from [sbt-tpolecat](https://github.com/typelevel/sbt-tpolecat).

[0.1.0]: https://github.com/typelevel/scalac-options/releases/tag/v0.1.0
