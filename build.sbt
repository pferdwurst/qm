name := "qm"

version := "1.0-SNAPSHOT"

lazy val root = (project in file("."))
    .enablePlugins(PlayScala)
    .settings(commonPlaySettings: _*)
    .settings(qmLibs: _*)



scalaVersion := "2.11.0"



lazy val commonPlaySettings: Seq[Setting[_]] = Seq(
    libraryDependencies ++= Seq(
      jdbc,
      anorm,
      cache,
      ws
    )
)

  lazy val qmLibs: Seq[Setting[_]] = Seq(
    libraryDependencies ++= Seq(
    "org.igniterealtime.smack" % "smack" % "3.3.1" from "http://repository.opencastproject.org/nexus/content/repositories/public/org/igniterealtime/smack/smack/3.3.1/smack-3.3.1.jar",
    "org.igniterealtime.smackx" % "smackx" % "3.3.1" from "http://repository.opencastproject.org/nexus/content/repositories/public/org/igniterealtime/smack/smackx/3.3.1/smackx-3.3.1.jar",
    "com.github.sstone" % "amqp-client_2.11" % "1.4"
    )
)