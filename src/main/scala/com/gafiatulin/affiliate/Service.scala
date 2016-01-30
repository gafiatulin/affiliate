package com.gafiatulin.affiliate

import java.util.UUID
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import com.gafiatulin.affiliate.utils.{Config, RefValidator}
import com.gafiatulin.affiliate.models._

object Service extends Service

trait Service extends Config with ActionTable with PartnerTable with RefValidator {
    import driver.api._


    def validateAbdRun(partner: Option[String], actionType: Byte) = {
        if (valid(partner, refLength))
            db.run(append(Action(partner.get, actionType)))
        else Future.successful(None)
    }

    def append(x: Action) = {
        actions += x
    }

    def signUp(ref: Option[String]):Future[String] = {
        val duid = databaseHash + UUID.randomUUID().toString.replaceAll("-", "")
        db.run((partners returning partners.map(_.id)) += Partner(duid)).map{
            id => upPR(ref)
            id}
    }

    def upV(partner: Option[String]) =  validateAbdRun(partner, 0)
    def upR(partner: Option[String]) =  validateAbdRun(partner, 1)
    def upPR(partner: Option[String]) =  validateAbdRun(partner, 2)

    def statsFor(partner: String):Future[String] = {
        val a = actions.filter(_.partner === partner).groupBy(_.actionType).map{case (x, l) => (x, l.length)}.result
        db.run(a).map(_.map{
            case (0, l) => s"Visits: $l"
            case (1, l) => s"Registrations: $l"
            case (2, l) => s"Partner Registrations: $l"
        }.mkString(" "))
    }
}
