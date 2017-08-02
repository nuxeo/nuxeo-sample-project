package org.nuxeo.sample.gatling

/*
 * (C) Copyright 2016 Nuxeo SA (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Benoit Delbosc
 *     Antoine Taillefer
 */

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.Duration
import scala.util.Random

import org.nuxeo.cap.bench.Constants
import org.nuxeo.cap.bench.Parameters

object CreateDocuments {

  def get = (duration: Duration, pause: Duration) => {
    scenario("Create Documents")
      .during(duration) {  
        feed(Feeders.users)
        .exec(session => session.set("randomInt", Math.abs(Random.nextInt)))
        .exec(RestClient.createDocument(Constants.GAT_WS_PATH, "file_${user}_${randomInt}", "File"))
        .pause(pause)
      }
  }

}

class Sim10CreateDocuments extends Simulation {

  val httpProtocol = http
    .baseURL(Parameters.getBaseUrl())
    .disableWarmUp
    .acceptEncodingHeader("gzip, deflate")
    .acceptEncodingHeader("identity")
    .connection("keep-alive")

  val scn = CreateDocuments.get(Parameters.getSimulationDuration(60), Parameters.getPause(1000))

  setUp(scn.inject(rampUsers(Parameters.getConcurrentUsers(10)).over(Parameters.getRampDuration(5))).exponentialPauses)
    .protocols(httpProtocol)
    .assertions(global.successfulRequests.percent.is(100))
}
