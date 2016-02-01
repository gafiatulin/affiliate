package com.gafiatulin

import org.scalatest._
import org.scalatest.prop.GeneratorDrivenPropertyChecks
import org.scalacheck.Gen

import com.gafiatulin.affiliate.utils.RefValidator

class UtilsSpec extends WordSpec with Matchers with GeneratorDrivenPropertyChecks {

    "Utils" should {
        "validate ref codes" in {
            val alphabet = "0123456789abcdef"
            val ss = for (n <- Gen.choose(0, 100)) yield (Some(Stream.continually(scala.util.Random.nextInt(alphabet.size)).map(alphabet).take(n).mkString), n)
            forAll (ss) { case (s, l) => RefValidator.valid(s, l) should equal (true)}
        }
    }
}
