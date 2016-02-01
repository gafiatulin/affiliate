name := "affiliate"

version := "1.0"

scalaVersion := "2.11.7"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature")

libraryDependencies ++= {
    val sprayV = "1.3.3"
    val slickV = "3.1.1"
    val scalaTestV = "3.0.0-M15"
    Seq(
        "com.typesafe.akka"   %% "akka-actor"       % "2.4.1",
        "io.spray"            %% "spray-can"        % sprayV,
        "io.spray"            %% "spray-routing"    % sprayV,
        "com.typesafe.slick"  %% "slick"            % slickV,
        "com.typesafe.slick"  %% "slick-hikaricp"   % slickV,
        "org.slf4j"           %  "slf4j-nop"        % "1.7.14",
        "org.flywaydb"        %  "flyway-core"      % "3.2.1",
        "org.postgresql"      %  "postgresql"       % "9.4.1207.jre7",
        "org.scalatest"       %% "scalatest"        % scalaTestV       % "test",
        "org.scalacheck"      %% "scalacheck"       % "1.12.5"         % "test",
        "io.spray"            %% "spray-testkit"    % sprayV           % "test"
    )
}
