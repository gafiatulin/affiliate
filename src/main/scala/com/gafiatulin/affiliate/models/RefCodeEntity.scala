package com.gafiatulin.affiliate.models

import java.util.UUID

case class RefCodeEntity(id: String = UUID.randomUUID().toString, referer: Option[String] = None, visits: Long = 0, regs: Long = 0, partnerRegs: Long = 0)
