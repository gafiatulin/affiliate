package com.gafiatulin.affiliate

import java.util.concurrent.ThreadLocalRandom

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import com.gafiatulin.affiliate.utils.{Config, RefValidator}
import com.gafiatulin.affiliate.models._

object Service extends Service

trait Service extends Config with ActionTable with PartnerTable with RefValidator {
    import driver.api._

    def validateAbdRun(partner: Option[String], actionType: Byte) = {    //Check if `partner` could be a value in the `partners` Table.
        if (valid(partner, refLength))                                   //Relying on the DB to enforce foreign key constraint
            db.run(append(Action(partner.get, actionType)))              //Proper way to do this is to ask DB for row with this id:
        else Future.successful(None)                                     //db.run(partners.filter(_.id === returned).result.headOption).flatMap(_ => ...)
    }

    def append(x: Action) = {
        actions += x
    }

    def signUp(ref: Option[String]):Future[String] = {
        val duid = databaseHash + (ThreadLocalRandom.current.nextLong | 0x1000000000000000L ).toHexString
        db.run((partners returning partners.map(_.id)) += Partner(duid)).map{
            id => upPR(ref)
            id}
    }

    def upV(partner: Option[String]) =  validateAbdRun(partner, 0)
    def upR(partner: Option[String]) =  validateAbdRun(partner, 1)
    def upPR(partner: Option[String]) =  validateAbdRun(partner, 2)

    def statsFor(partner: String):Future[Option[Tuple3[Int, Int, Int]]] = {
        db.run(partners.filter(_.id === partner).result.headOption).flatMap{
            case Some(_) => db.run(actions.filter(_.partner === partner).groupBy(_.actionType).map{case (x, l) => (x, l.length)}.result).map{x =>
                    val m = x.toMap
                    Some(m.get(0) getOrElse 0, m.get(1) getOrElse 0, m.get(2) getOrElse 0)
                }
            case None => Future.successful(None)
        }
    }
}
