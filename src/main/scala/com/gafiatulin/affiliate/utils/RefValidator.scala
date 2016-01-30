package com.gafiatulin.affiliate.utils

trait RefValidator {
    def valid: (Option[String], Int) => Boolean = {
        case (Some(x: String), l) => (x.length == l) && x.forall(c => ('0' <= c && c <= '9') || ('a' <= c && c <= 'f'))
        case _ => false
    }
}
