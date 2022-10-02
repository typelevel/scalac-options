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

lazy val root = tlCrossRootProject.aggregate(core, macros, lib, testkit)

lazy val literallyVersion  = "1.1.0"
lazy val munitVersion      = "0.7.29"
lazy val scalacheckVersion = "1.17.0"

lazy val core = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .in(file("core"))
  .enablePlugins(NoPublishPlugin)
  .settings(
    name := "scalac-options-core"
  )

lazy val macros = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .in(file("macros"))
  .dependsOn(core)
  .enablePlugins(NoPublishPlugin)
  .settings(
    name := "scalac-options-macros",
    scalacOptions := {
      if (tlIsScala3.value)
        scalacOptions.value.filterNot(_ == "-source:3.0-migration") :+ "-source:3.1"
      else
        scalacOptions.value
    },
    libraryDependencies ++= Seq(
      "org.typelevel" %%% "literally" % literallyVersion
    ) ++ {
      if (tlIsScala3.value) Nil
      else
        List("org.scala-lang" % "scala-reflect" % scalaVersion.value % Provided)
    }
  )

lazy val lib = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .in(file("lib"))
  .dependsOn(core, macros)
  .settings(
    name := "scalac-options"
  )
  .settings {
    def projectMappings(cproj: sbtcrossproject.CrossProject) = Def.taskDyn {
      cproj.projects(crossProjectPlatform.value) / Compile / packageBin / mappings
    }

    Compile / packageBin / mappings ++=
      projectMappings(core).value ++ projectMappings(macros).value
  }
  .settings {
    def projectMappings(cproj: sbtcrossproject.CrossProject) = Def.taskDyn {
      cproj.projects(crossProjectPlatform.value) / Compile / packageSrc / mappings
    }

    Compile / packageSrc / mappings ++=
      projectMappings(core).value ++ projectMappings(macros).value
  }

lazy val testkit = crossProject(JVMPlatform, JSPlatform)
  .crossType(CrossType.Pure)
  .in(file("testkit"))
  .dependsOn(lib)
  .settings(
    name := "scalac-options-testkit",
    libraryDependencies ++= Seq(
      "org.scalacheck" %%% "scalacheck"       % scalacheckVersion,
      "org.scalameta"  %%% "munit"            % munitVersion % Test,
      "org.scalameta"  %%% "munit-scalacheck" % munitVersion % Test
    )
  )
