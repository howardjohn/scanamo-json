lazy val commonSettings = Seq(
  organization := "io.github.howardjohn",
  scalaVersion := "2.12.6",
  version := "0.2.0",
  scalacOptions += "-Ypartial-unification"
)

lazy val root = project
  .in(file("."))
  .settings(commonSettings)
  .settings(noPublishSettings)
  .aggregate(circe, play, tests)

lazy val CirceVersion = "0.9.0"
lazy val PlayVersion = "2.6.7"
lazy val ScanamoVersion = "1.0.0-M6"
lazy val ScalaTestVersion = "3.0.4"

lazy val circe = project
  .in(file("scanamo-circe"))
  .settings(commonSettings)
  .settings(publishSettings)
  .settings(
    moduleName := "scanamo-circe",
    libraryDependencies ++=
      Seq(
        "io.circe" %% "circe-parser" % CirceVersion,
        "com.gu" %% "scanamo" % ScanamoVersion,
        "org.scalatest" %% "scalatest" % ScalaTestVersion % "test"
      )
  )
  .dependsOn(tests % "test")

lazy val play = project
  .in(file("scanamo-play-json"))
  .settings(commonSettings)
  .settings(publishSettings)
  .settings(
    moduleName := "scanamo-play-json",
    libraryDependencies ++=
      Seq(
        "com.typesafe.play" %% "play-json" % PlayVersion,
        "com.gu" %% "scanamo" % ScanamoVersion,
        "org.scalatest" %% "scalatest" % ScalaTestVersion % "test"
      )
  )
  .dependsOn(tests % "test")

lazy val tests = project
  .in(file("tests"))
  .settings(commonSettings)
  .settings(noPublishSettings)
  .settings(
    moduleName := "tests",
    libraryDependencies ++=
      Seq(
        "com.gu" %% "scanamo" % ScanamoVersion,
        "org.scalatest" %% "scalatest" % ScalaTestVersion
      )
  )
lazy val noPublishSettings = Seq(
  publish := {},
  publishLocal := {},
  publishArtifact := false
)

lazy val publishSettings = Seq(
  homepage := Some(url("https://github.com/howardjohn/scanamo-json")),
  licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT")),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/howardjohn/scanamo-json"),
      "scm:git@github.com:howardjohn/scanamo-json.git"
    )),
  developers := List(
    Developer(
      id = "howardjohn",
      name = "John Howard",
      email = "johnbhoward96@gmail.com",
      url = url("https://github.com/howardjohn/")
    )
  ),
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ =>
    false
  },
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  }
)
