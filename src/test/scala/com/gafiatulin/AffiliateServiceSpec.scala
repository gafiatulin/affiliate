package com.gafiatulin

import org.scalatest._
import org.scalatest.concurrent.ScalaFutures
import akka.event.{ NoLogging, LoggingAdapter }
import spray.testkit.ScalatestRouteTest
import spray.http.StatusCodes

import scala.concurrent.Await
import scala.concurrent.duration._

import com.gafiatulin.affiliate.AffiliateService
import com.gafiatulin.affiliate.models._
import com.gafiatulin.affiliate.utils.{Migration, RefValidator}

class AffiliateSpec extends WordSpec with Matchers with ScalatestRouteTest with ScalaFutures with AffiliateService with Migration {
    override def actorRefFactory = system
    protected val log: LoggingAdapter = NoLogging
    import driver.api._

    reloadSchema()
    Await.result(db.run(partners += Partner("000000000000000000000000")), 5.seconds)

    "Affiliate" should {
        "expose some paths" in {
            Get("/signup") ~> affiliateRoute ~> check {
                 status === StatusCodes.OK
            }

            Get("/signup/affiliate") ~> affiliateRoute ~> check {
                 status === StatusCodes.OK
            }

            Get("/affiliate/") ~> affiliateRoute ~> check {
                 status === StatusCodes.OK
            }

            Post("/signup") ~> affiliateRoute ~> check {
                 status === StatusCodes.OK
            }

            Post("/signup/affiliate") ~> affiliateRoute ~> check {
                 status === StatusCodes.OK
            }
        }

        "reject anything else" in {
            Get("/") ~> sealRoute(affiliateRoute) ~> check {
                 status === StatusCodes.NotFound
            }

            Post("/affiliate/") ~> sealRoute(affiliateRoute) ~> check {
                 status === StatusCodes.NotFound
            }
        }

        "register clients" in {
            Post("/signup") ~> affiliateRoute ~> check {
                 responseAs[String] === "Registred"
            }
        }

        "register partners and return valid ref" in {
            Post("/signup/affiliate") ~> affiliateRoute ~> check {
                val x = responseAs[String]
                RefValidator.valid(Some(x), x.length) === true
            }
        }

        "persist new partners in the DB" in {
            Post("/signup/affiliate")~> affiliateRoute ~> check {
                val returned = responseAs[String]
                val fut = db.run(partners.filter(_.id === returned).result.headOption)
                whenReady(fut){ x => x === Some(returned)}
            }
        }

        "persist views with ref" in {
            Get("/signup?ref=000000000000000000000000") ~> affiliateRoute ~> check {
                 val fut = statsFor("000000000000000000000000").map{_.map{case (v, _, _) => v} getOrElse 0 }
                 whenReady(fut){_ === 1}
            }
        }

        "persist registrations with ref" in {
            Post("/signup?ref=000000000000000000000000") ~> affiliateRoute ~> check {
                 val fut = statsFor("000000000000000000000000").map{_.map{case (_, r, _) => r} getOrElse 0 }
                 whenReady(fut){_ === 1}
            }
        }

        "persist partner registrations with ref" in {
            Post("/signup/affiliate?ref=000000000000000000000000") ~> affiliateRoute ~> check {
                 val fut = statsFor("000000000000000000000000").map{_.map{case (_, _, pr) => pr} getOrElse 0 }
                 whenReady(fut){_ === 1}
            }
        }

        "show information conserning user" in {
            Get("/affiliate/000000000000000000000000") ~> affiliateRoute ~> check {
                responseAs[String] === "Visits: 1, Registrations: 1, Partner Registrations: 1"
            }
        }

    }
}
