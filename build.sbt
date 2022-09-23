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

val Scala212 = "2.12.17"
val Scala213 = "2.13.10"
val Scala3   = "3.2.0"

ThisBuild / scalaVersion := Scala212 // The default while sbt is still based on Scala 2.12.x

ThisBuild / crossScalaVersions := Seq(
  Scala212,
  Scala213,
  Scala3
) // There's no reason not to cross-publish

lazy val root = tlCrossRootProject.aggregate(lib)

lazy val munitVersion      = "0.7.29"
lazy val scalacheckVersion = "1.17.0"

lazy val lib = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .in(file("lib"))
  .settings(
    name := "scalac-options",
    scalacOptions := {
      if (tlIsScala3.value)
        scalacOptions.value.filterNot(_ == "-source:3.0-migration") :+ "-source:3.1"
      else
        scalacOptions.value
    },
    libraryDependencies ++= Seq(
      "org.scalameta"  %%% "munit"            % munitVersion      % Test,
      "org.scalacheck" %%% "scalacheck"       % scalacheckVersion % Test,
      "org.scalameta"  %%% "munit-scalacheck" % munitVersion      % Test
    )
  )
