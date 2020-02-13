import sbt.Keys.{credentials, publishTo}

lazy val scala212 = "2.12.10"
lazy val scala213 = "2.13.1"
lazy val supportedScalaVersions = List(scala212, scala213)

scalaVersion in ThisBuild := scala212
crossScalaVersions in ThisBuild := supportedScalaVersions

lazy val commonSettings = Seq(
  organization := "io.laserdisc"
)

def commonOptions(scalaVersion: String) =
  CrossVersion.partialVersion(scalaVersion) match {
    case Some((2, 12)) =>
      Seq("-Ypartial-unification")
    case _ => Seq.empty
  }

lazy val root = project
  .in(file("."))
  .settings(commonSettings)
  .settings(crossScalaVersions := Nil)
  .settings(noPublishSettings)
  .aggregate(circe, play, tests)

lazy val CirceVersion = "0.12.3"
lazy val PlayVersion = "2.8.0-M7"
lazy val ScanamoVersion = "1.0.0-M12"
lazy val ScalaTestVersion = "3.1.0-RC2"

lazy val circe = project
  .in(file("scanamo-circe"))
  .settings(commonSettings)
  .settings(scalacOptions := commonOptions(scalaVersion.value))
  .settings(publishSettings)
  .settings(
    moduleName := "scanamo-circe",
    libraryDependencies ++=
      Seq(
        "io.circe" %% "circe-parser" % CirceVersion,
        "org.scanamo" %% "scanamo" % ScanamoVersion,
        "org.scalatest" %% "scalatest" % ScalaTestVersion % "test"
      )
  )
  .dependsOn(tests % "test")

lazy val play = project
  .in(file("scanamo-play-json"))
  .settings(commonSettings)
  .settings(scalacOptions := commonOptions(scalaVersion.value))
  .settings(publishSettings)
  .settings(
    moduleName := "scanamo-play-json",
    libraryDependencies ++=
      Seq(
        "com.typesafe.play" %% "play-json" % PlayVersion,
        "org.scanamo" %% "scanamo" % ScanamoVersion,
        "org.scalatest" %% "scalatest" % ScalaTestVersion % "test"
      )
  )
  .dependsOn(tests % "test")

lazy val tests = project
  .in(file("tests"))
  .settings(commonSettings)
  .settings(scalacOptions := commonOptions(scalaVersion.value))
  .settings(publishSettings)
  .settings(
    moduleName := "scanamo-json-tests",
    libraryDependencies ++=
      Seq(
        "org.scanamo" %% "scanamo" % ScanamoVersion,
        "org.scalatest" %% "scalatest" % ScalaTestVersion
      )
  )

lazy val noPublishSettings = Seq(
  publish := {},
  publishLocal := {},
  publishArtifact := false
)


lazy val publishSettings = Seq(
  publishMavenStyle      := true,
  Test / publishArtifact := true,
  pomIncludeRepository   := (_ => false),
  developers := List(
    Developer(
      id = "howardjohn",
      name = "John Howard",
      email = "johnbhoward96@gmail.com",
      url = url("https://github.com/howardjohn/")
    ),
    Developer("semenodm", "Dmytro Semenov", "", url("https://github.com/semenodm"))
  ),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/laserdisc-io/fs2-aws/tree/master"),
      "scm:git:git@github.com:laserdisc-io/fs2-aws.git",
      "scm:git:git@github.com:laserdisc-io/fs2-aws.git"
    )
  ),
  homepage := Some(url("https://github.com/laserdisc-io/fs2-aws/")),
  licenses := Seq(
    "MIT" -> url("https://raw.githubusercontent.com/laserdisc-io/fs2-aws/master/LICENSE")
  ),
  pgpPublicRing    := file(".travis/local.pubring.asc"),
  pgpSecretRing    := file(".travis/local.secring.asc"),
  releaseEarlyWith := SonatypePublisher
)

lazy val publishSettingsOld = Seq(
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
  publishTo in ThisBuild := Some("Artifactory" at "https://moda.jfrog.io/moda/sbt"),
  credentials += {
    (sys.env.get("JFROG_USER"), sys.env.get("JFROG_PASS")) match {
      case (Some(user), Some(pass)) => Credentials("Artifactory Realm", "moda.jfrog.io", user, pass)
      case _ => Credentials(Path.userHome / "credentials.sbt")
    }
  }
//  publishTo := {
//    val nexus = "https://oss.sonatype.org/"
//    if (isSnapshot.value)
//      Some("snapshots" at nexus + "content/repositories/snapshots")
//    else
//      Some("releases" at nexus + "service/local/staging/deploy/maven2")
//  }
)
