name := "scalac-options-root"

ThisBuild / tlBaseVersion := "0.1"

ThisBuild / organization     := "org.typelevel"
ThisBuild / organizationName := "Typelevel"
ThisBuild / startYear        := Some(2022)
ThisBuild / licenses         := Seq(License.Apache2)
ThisBuild / developers := List(
  tlGitHubDev("DavidGregory084", "David Gregory")
)

ThisBuild / tlSonatypeUseLegacyHost := false

val Scala212 = "2.12.20"
val Scala213 = "2.13.14"
val Scala3   = "3.3.3"

ThisBuild / scalaVersion := Scala212 // The default while sbt is still based on Scala 2.12.x

ThisBuild / crossScalaVersions := Seq(
  Scala212,
  Scala213,
  Scala3
) // There's no reason not to cross-publish

lazy val root = tlCrossRootProject.aggregate(lib)

lazy val munitVersion           = "1.0.2"
lazy val scalacheckVersion      = "1.18.1"
lazy val munitScalacheckVersion = "1.0.0"

lazy val lib = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .in(file("lib"))
  .settings(
    name := "scalac-options",
    libraryDependencies ++= Seq(
      "org.scalameta"  %%% "munit"            % munitVersion           % Test,
      "org.scalacheck" %%% "scalacheck"       % scalacheckVersion      % Test,
      "org.scalameta"  %%% "munit-scalacheck" % munitScalacheckVersion % Test
    )
  )
