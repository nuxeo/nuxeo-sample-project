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

import org.nuxeo.cap.bench.Constants
import org.nuxeo.cap.bench.Headers

object Feeders {

  val usersQueue = csv("gatling-users.csv").queue
  val users = csv("gatling-users.csv").circular
  val admins = csv("admins.csv").circular

}
