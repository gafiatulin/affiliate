package com.gafiatulin.affiliate.models

import java.util.UUID
import com.gafiatulin.affiliate.utils.DatabaseConfig

case class Partner(id: String = UUID.randomUUID().toString)

trait PartnerTable extends DatabaseConfig {
    import driver.api._
    class Partners(tag: Tag) extends Table[Partner](tag, "partners") {
        def id = column[String]("id", O.PrimaryKey)
        def * = id <> (Partner.apply, Partner.unapply)
    }
    protected val partners = TableQuery[Partners]
}
