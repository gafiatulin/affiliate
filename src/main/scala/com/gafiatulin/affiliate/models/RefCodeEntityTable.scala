package com.gafiatulin.affiliate.models

import java.util.UUID
import com.gafiatulin.affiliate.utils.DatabaseConfig

trait RefCodeEntityTable extends DatabaseConfig {
  import driver.api._

  class RefCode(tag: Tag) extends Table[RefCodeEntity](tag, "ref_codes") {
    def id = column[String]("id", O.PrimaryKey)
    def visits = column[Long]("visits")
    def regs = column[Long]("regs")
    def partnerRegs = column[Long]("partner_regs")
    def * = (id, visits, regs, partnerRegs) <> ((RefCodeEntity.apply _).tupled, RefCodeEntity.unapply)
  }
  
  protected val codes = TableQuery[RefCode]

}
