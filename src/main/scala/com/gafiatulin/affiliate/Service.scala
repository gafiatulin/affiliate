package com.gafiatulin.affiliate

import java.util.UUID
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import com.gafiatulin.affiliate.utils.Config 
import com.gafiatulin.affiliate.models.{RefCodeEntity, RefCodeEntityTable}

object Service extends Service

trait Service extends RefCodeEntityTable with Config {

    import driver.api._

    def getCodeById(id: String):Future[Option[RefCodeEntity]] = db.run(codes.filter(_.id === id).result.headOption)
    def update(id: String, f: RefCodeEntity => RefCodeEntity):Future[Option[RefCodeEntity]] = getCodeById(id).flatMap{
        case Some(code) => 
            val updatedCode = f(code)
            db.run(codes.filter(_.id === id).update(updatedCode)).map(_ => Some(updatedCode))
        case None => Future.successful(None)
    }
    def insert(ce: RefCodeEntity): Future[RefCodeEntity] = {
        val action = (codes returning codes) += ce
        db.run(action)
    }

    def signUp(ref: String) = {
        val duid = UUID.nameUUIDFromBytes((databaseUrl + UUID.randomUUID().toString).getBytes()).toString.replaceAll("-", "")
        val id = insert(RefCodeEntity(duid)).map{_.id}
        id.flatMap(x => upPR(ref))
        id
    }

    def upV(id: String) = update(id, {x => x.copy(visits = x.visits + 1)})
    
    def upR(id: String) = update(id, {x => x.copy(regs = x.regs + 1)})

    def upPR(id: String) = update(id, {x => x.copy(partnerRegs = x.partnerRegs + 1)})
    
    def statsFor(id: String) = getCodeById(id).map{
        case Some(ce) => "Visits: " + ce.visits +  " Registrations: " + ce.regs + " Partner Regsistrations: " + ce.partnerRegs
        case None => "No stats found for id: " + id
    }
}