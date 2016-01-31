package com.gafiatulin.affiliate

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.http.scaladsl.Http

import com.gafiatulin.affiliate.utils.Migration 

object Main extends App with Routes with Migration {
    implicit val system = ActorSystem()
    implicit val ec = system.dispatcher
    implicit val materializer = ActorMaterializer()

    migrate()

    Http().bindAndHandle(routes, httpInterface, httpPort)
}
