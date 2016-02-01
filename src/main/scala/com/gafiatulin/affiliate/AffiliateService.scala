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
    def validateAbdRun(partner: Option[String], actionType: Byte) = {    //Check if `partner` could be a value in the `partners` Table.
        if (RefValidator.valid(partner, Config.refLength))               //Relying on the DB to enforce foreign key constraint
            db.run(append(Action(partner.get, actionType)))              //Proper way to do this is to ask DB for row with this id:
        else Future.successful(None)                                     //db.run(partners.filter(_.id === returned).result.headOption).flatMap(_ => ...)
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

    def statsFor(partner: String):Future[Option[Tuple3[Int, Int, Int]]] = {
        db.run(partners.filter(_.id === partner).result.headOption).flatMap{
            case Some(_) => db.run(actions.filter(_.partner === partner).groupBy(_.actionType).map{case (x, l) => (x, l.length)}.result).map{x =>
                    val m = x.toMap
                    Some(m.get(0) getOrElse 0, m.get(1) getOrElse 0, m.get(2) getOrElse 0)
                }
            case None => Future.successful(None)
        }
    }

    def formFor(path: String, ref: Option[String]) = """<!doctype html><html><head><meta charset="utf-8"></head><body><form action="""" + path + ref.map("?ref=" + _).getOrElse("") +  """" method="post"><input type="submit" value="Register"></form></body></html>"""

    val affiliateRoute = {
        get{
            pathPrefix("affiliate"){
                path(RestPath){ id =>
                    complete{
                        statsFor(id.toString).map{
                            case Some((v: Int, r: Int, pr: Int)) => s"Visits: $v, Registrations: $r, Partner Registrations: $pr"
                            case None => ""
                        }
                    }
                }
            } ~
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
