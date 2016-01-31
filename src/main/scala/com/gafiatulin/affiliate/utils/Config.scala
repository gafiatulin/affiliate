package com.gafiatulin.affiliate.utils

import com.typesafe.config.ConfigFactory

trait Config {
    private val config = ConfigFactory.load()
    private val httpConfig = config.getConfig("http")
    private val databaseConfig = config.getConfig("database")
    private val serviceConfig = config.getConfig("service")

    val httpInterface = httpConfig.getString("interface")
    val httpPort = httpConfig.getInt("port")

    val refLength = serviceConfig.getInt("refLength")

    val databaseUrl = databaseConfig.getString("url")
    val databaseUser = databaseConfig.getString("user")
    val databasePassword = databaseConfig.getString("password")
}

case object Config extends Config{
    val databaseHash = scala.util.hashing.MurmurHash3.stringHash(databaseUrl+databaseUser+databasePassword).toHexString
}
