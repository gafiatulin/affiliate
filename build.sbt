name := "affiliate"

version := "1.0"

scalaVersion := "2.11.7"

libraryDependencies ++= {
  val akkaStreamV      = "2.0.3"
  val scalaTestV       = "3.0.0-M15"
  val slickV           = "3.1.1"
  Seq(
    "com.typesafe.akka"   %% "akka-stream-experimental"              % akkaStreamV,
    "com.typesafe.akka"   %% "akka-http-core-experimental"           % akkaStreamV,
    "com.typesafe.akka"   %% "akka-http-experimental"                % akkaStreamV,
    "com.typesafe.slick"  %% "slick"                                 % slickV,
    "com.typesafe.slick"  %% "slick-hikaricp"                        % slickV,
    "com.github.tminglei" %  "slick-pg_2.11"                         % "0.11.0",
    "com.github.tminglei" %  "slick-pg_date2_2.11"                   % "0.11.0",
    "org.slf4j"           %  "slf4j-nop"                             % "1.7.14",
    "org.flywaydb"        %  "flyway-core"                           % "3.2.1",
    "org.scalatest"       %% "scalatest"                             % scalaTestV       % "test",
    "com.typesafe.akka"   %% "akka-http-testkit-experimental"        % akkaStreamV      % "test"
  )
}
