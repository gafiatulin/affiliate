package com.gafiatulin.affiliate

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http

import com.gafiatulin.affiliate.utils.{Migration, Config}

object Main extends App with Migration with Config {
    implicit val system = ActorSystem()
    val handler = system.actorOf(Props[AffiliateServiceActor], name = "affiliate-service")

    reloadSchema()

    IO(Http) ! Http.Bind(handler, Config.httpInterface, Config.httpPort)
}
