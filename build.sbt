onLoad in Global := ((s: State) => { "updateIdea" :: s}) compose (onLoad in Global).value

libraryDependencies += "org.zalando" %% "grafter" % "1.3.0"

lazy val grafterIntellijPlugin: Project =
  Project("grafter-intellij-plugin", file("."))
    .enablePlugins(SbtIdeaPlugin)
    .settings(
      name := "grafter-intellij-plugin",
      version := "1.0.0",
      scalaVersion := "2.11.8",
      assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false),
      ideaInternalPlugins := Seq(),
      ideaExternalPlugins := Seq(IdeaPlugin.Zip("scala-plugin", url("https://plugins.jetbrains.com/files/1347/21610/scala-intellij-bin-1.9.2.zip"))),
      aggregate in updateIdea := false,
      assemblyExcludedJars in assembly <<= ideaFullJars,
      ideaBuild := "163.9166.29"
    )

lazy val ideaRunner: Project = project.in(file("ideaRunner"))
  .dependsOn(grafterIntellijPlugin % Provided)
  .settings(
    name := "ideaRunner",
    version := "1.0",
    scalaVersion := "2.11.8",
    autoScalaLibrary := false,
    unmanagedJars in Compile <<= ideaMainJars.in(grafterIntellijPlugin),
    unmanagedJars in Compile += file(System.getProperty("java.home")).getParentFile / "lib" / "tools.jar"
  )

lazy val packagePlugin = TaskKey[File]("package-plugin", "Create plugin's zip file ready to load into IDEA")

packagePlugin in grafterIntellijPlugin <<= (assembly in grafterIntellijPlugin,
  target in grafterIntellijPlugin,
  ivyPaths) map { (ideaJar, target, paths) =>
  val pluginName = "grafter-intellij-plugin"
  val ivyLocal = paths.ivyHome.getOrElse(file(System.getProperty("user.home")) / ".ivy2") / "local"
  val sources = Seq(
    ideaJar -> s"$pluginName/lib/${ideaJar.getName}"
  )
  val out = target / s"$pluginName.zip"
  IO.zip(sources, out)
  out
}
    