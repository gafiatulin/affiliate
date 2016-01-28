name := "affiliate"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= {
  val akkaStreamV      = "2.0.3"
  val scalaTestV       = "3.0.0-M15"
  val slickV           = "3.1.1"
  Seq(
    "com.typesafe.akka"  %% "akka-stream-experimental"              % akkaStreamV,
    "com.typesafe.akka"  %% "akka-http-core-experimental"           % akkaStreamV,
    "com.typesafe.akka"  %% "akka-http-experimental"                % akkaStreamV,
    "com.typesafe.slick" %% "slick"                                 % slickV,
    "com.typesafe.slick" %% "slick-hikaricp"                        % slickV,
    "org.slf4j"          %  "slf4j-nop"                             % "1.7.14",
    "org.postgresql"     %  "postgresql"                            % "9.4.1207.jre7",
    "org.flywaydb"       %  "flyway-core"                           % "3.2.1",
    "org.scalatest"      %% "scalatest"                             % scalaTestV       % "test",
    "com.typesafe.akka"  %% "akka-http-testkit-experimental"        % akkaStreamV      % "test"
  )
}
