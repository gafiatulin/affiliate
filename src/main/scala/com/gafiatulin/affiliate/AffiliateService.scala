package com.gafiatulin.affiliate

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import java.util.concurrent.ThreadLocalRandom

import akka.actor.Actor
import spray.routing.HttpService
import spray.routing.Directive.pimpApply
import spray.http.MediaTypes.{ `text/html` }

import com.gafiatulin.affiliate.models._
import com.gafiatulin.affiliate.utils.{RefValidator, Config}

class AffiliateServiceActor extends Actor with AffiliateService {
    def actorRefFactory = context
    def receive = runRoute(affiliateRoute)
}

trait AffiliateService extends HttpService with ActionTable with PartnerTable {

    import driver.api._
    def validateAbdRun(partner: Option[String], actionType: Byte) = {
        if (RefValidator.valid(partner, Config.refLength))
            db.run(append(Action(partner.get, actionType)))
        else Future.successful(None)
    }

    def append(x: Action) = {
        actions += x
    }

    def signUp(ref: Option[String]):Future[String] = {
        val duid = Config.databaseHash + (ThreadLocalRandom.current.nextLong | 0x1000000000000000L ).toHexString
        db.run((partners returning partners.map(_.id)) += Partner(duid)).map{
            id => upPR(ref)
            id}
    }

    def upV(partner: Option[String])  = validateAbdRun(partner, 0)
    def upR(partner: Option[String])  = validateAbdRun(partner, 1)
    def upPR(partner: Option[String]) = validateAbdRun(partner, 2)

    def statsFor(partner: String):Future[String] = {
        val a = actions.filter(_.partner === partner).groupBy(_.actionType).map{case (x, l) => (x, l.length)}.result
        db.run(a).map(_.map{
            case (0, l) => s"Visits: $l"
            case (1, l) => s"Registrations: $l"
            case (2, l) => s"Partner Registrations: $l"
        }.mkString(" "))
    }

    val affiliateRoute = {
        def formFor(path: String, ref: Option[String]) = """<!doctype html><html><head><meta charset="utf-8"></head><body><form action="""" + path + ref.map("?ref=" + _).getOrElse("") +  """" method="post"><input type="submit" value="Register"></form></body></html>"""
        pathPrefix("affiliate"){
            path(RestPath){ id =>
                complete(statsFor(id.toString))
            }
        } ~
        get{
            parameter("ref".?){ ref =>
                upV(ref)
                respondWithMediaType(`text/html`){
                    pathPrefix("signup"){
                        pathPrefix("affiliate"){
                            complete(formFor("/signup/affiliate", ref))
                        } ~
                        complete(formFor("/signup", ref))
                    }
                }
            }
        } ~
        post{
            parameter("ref".?){ ref =>
                pathPrefix("signup"){
                    pathPrefix("affiliate"){
                        complete(signUp(ref))
                    } ~
                    {
                        complete(upR(ref).map{x => "Registred"})
                    }
                }
            }
        }
    }
}
