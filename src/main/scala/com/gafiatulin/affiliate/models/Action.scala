package com.gafiatulin.affiliate.models

import com.gafiatulin.affiliate.utils.DatabaseConfig

case class Action (partner: String, actionType: Byte, id: Option[Long] = None)

trait ActionTable extends DatabaseConfig {
    import driver.api._
    class Actions(tag: Tag) extends Table[Action](tag, "actions") {
        def partner = column[String]("partner")
        def actionType = column[Byte]("actionType")
        def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
        def * = (partner, actionType, id.?) <> (Action.tupled, Action.unapply)
    }
    protected val actions = TableQuery[Actions]
}
