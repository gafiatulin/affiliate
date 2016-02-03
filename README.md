Bare-bones prototype implementation of the affiliate program backend system, written is Scala with [akka-http](http://doc.akka.io/docs/akka-stream-and-http-experimental/2.0.3/scala.html) ([Spray](http://spray.io)), [Slick](http://slick.typesafe.com) and [PostgreSQL](http://www.postgresql.org).

1. Generates unique referral codes.
2. Tracks actions with the code.
3. Provides statistics for the code.

Full description and load-testing results available in wiki (in russian): [Description](https://github.com/gafiatulin/affiliate/wiki), [Load Testing](https://github.com/gafiatulin/affiliate/wiki/Load-Testing)
