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
    "jivesoftware" % "smack" % "3.1.0",
    "jivesoftware" % "smackx" % "3.1.0",
    "com.github.sstone" % "amqp-client_2.11" % "1.4"
    )
)