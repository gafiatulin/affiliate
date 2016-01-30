package com.gafiatulin.affiliate.models

import java.util.UUID
import java.time.LocalDateTime
import com.gafiatulin.affiliate.utils.DatabaseConfig
import com.gafiatulin.affiliate.utils.CustomizedPostgresDriver

case class Action (partner: String, actionType: Byte, time: LocalDateTime = LocalDateTime.now(), id: UUID = UUID.randomUUID())

trait ActionTable extends DatabaseConfig with CustomizedPostgresDriver {
    import CustomizedPostgresDriver.api._
    class Actions(tag: Tag) extends Table[Action](tag, "actions") {
        def partner = column[String]("partner")
        def actionType = column[Byte]("actionType")
        def time = column[LocalDateTime]("time")
        def id = column[UUID]("id", O.PrimaryKey)
        def * = (partner, actionType, time, id) <> (Action.tupled, Action.unapply)
    }
    protected val actions = TableQuery[Actions]
}
