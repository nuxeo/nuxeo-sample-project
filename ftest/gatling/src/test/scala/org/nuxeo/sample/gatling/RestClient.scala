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

object RestClient {

  def createDocument = (parent: String, name: String, docType: String) => {
    http("Create " + docType)
      .post(Constants.API_PATH + parent)
      .headers(Headers.base)
      .header("Content-Type", "application/json")
      .basicAuth("${user}", "${password}")
      .body(StringBody(
      """{ "entity-type": "document", "name":"""" + name + """", "type": """" + docType +
        """","properties": {"dc:title":"""" + name + """", "dc:description": "Gatling test document"}}""".stripMargin))
      .check(status.in(201))
  }

}
