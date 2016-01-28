package com.gafiatulin.affiliate

import akka.actor.ActorSystem
import akka.stream.Materializer
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.marshalling.ContentTypeOverrider
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import scala.concurrent.ExecutionContext

trait Routes extends Service {
    implicit def system: ActorSystem
    implicit def ec: ExecutionContext
    implicit def materializer: Materializer

    def routes: Route = {
        def htmlContentType(str: String) = HttpEntity(ContentTypes.`text/html(UTF-8)`, str)
        def formFor(path: String, ref: Option[String]) = """<!doctype html><html><head><meta charset="utf-8"></head><body><form action="""" + path + """" method="post"><input type="hidden" name="ref" value="""" + ref.getOrElse("") + """"><input type="submit" value="Register"></form></body></html>"""
        
        pathPrefix("affiliate"){
            path(RestPath){ id =>
                complete(statsFor(id.toString))
            }
        } ~
        get{
            parameter("ref".?){ ref => 
                ref.foreach(upV(_))
                pathPrefix("signup"){
                    pathPrefix("affiliate"){
                        complete(htmlContentType(formFor("/signup/affiliate", ref)))
                    } ~
                    complete(htmlContentType(formFor("/signup", ref)))
                }
            }     
        } ~
        post{
            formFields("ref"){ ref =>
                pathPrefix("signup"){
                    pathPrefix("affiliate"){  
                        complete(signUp(ref).map{"OK: " + _})
                    } ~ 
                    {
                        complete(upR(ref).map{x => "Registred"})
                    }
                }
            }
        }
    }
}