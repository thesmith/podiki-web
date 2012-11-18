import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "podiki-web"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "com.top10" %% "scala-redis-client" % "1.7.0-SNAPSHOT"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      resolvers += "Sonatype OSS Snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
      resolvers += "Scala-Tools Maven2 Releases Repository" at "http://scala-tools.org/repo-releases"     
    )

}
