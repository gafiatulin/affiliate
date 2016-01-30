package com.gafiatulin.affiliate.utils

import com.github.tminglei.slickpg._

trait CustomizedPostgresDriver extends ExPostgresDriver with PgDate2Support {
    override val api = new API with DateTimeImplicits
}

object CustomizedPostgresDriver extends CustomizedPostgresDriver
