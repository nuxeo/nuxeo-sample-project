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
import io.gatling.core.config.GatlingFiles
import io.gatling.http.Predef._

import scala.io.Source

import org.nuxeo.cap.bench.Constants
import org.nuxeo.cap.bench.Parameters
import org.nuxeo.cap.bench.NuxeoRest

object Setup {

  def get = (userCount: Integer) => {
    scenario("Setup")
      .feed(Feeders.admins)
        .exec(NuxeoRest.createGroupIfNotExists(Constants.GAT_GROUP_NAME)).exitHereIfFailed
        .exec(NuxeoRest.createDocumentIfNotExistsAsAdmin(Constants.ROOT_WORKSPACE_PATH, Constants.GAT_WS_NAME, "Workspace")).exitHereIfFailed
        .exec(NuxeoRest.grantReadWritePermission(Constants.GAT_WS_PATH, Constants.GAT_GROUP_NAME)).exitHereIfFailed
        .repeat(userCount.intValue(), "userCount") {
          feed(Feeders.usersQueue)
            .exec(NuxeoRest.createUserIfNotExists(Constants.GAT_GROUP_NAME))
        }
  }
}

class Sim00Setup extends Simulation {

  val httpProtocol = http
    .baseURL(Parameters.getBaseUrl())
    .disableWarmUp
    .acceptEncodingHeader("gzip, deflate")
    .connection("keep-alive")

  val userCount = Source.fromFile(GatlingFiles.dataDirectory + "/gatling-users.csv").getLines.size - 1
  val scn = Setup.get(userCount)

  setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
    .assertions(global.successfulRequests.percent.is(100))
}
