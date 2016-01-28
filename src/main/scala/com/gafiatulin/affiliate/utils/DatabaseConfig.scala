package com.gafiatulin.affiliate.utils

trait DatabaseConfig {
    val driver = slick.driver.PostgresDriver
    import driver.api._
    def db = Database.forConfig("database")
}