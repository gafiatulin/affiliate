package com.gafiatulin.affiliate.utils

trait DatabaseConfig {
    val driver = slick.driver.PostgresDriver
    import driver.api._
    val db = Database.forConfig("database")
}
