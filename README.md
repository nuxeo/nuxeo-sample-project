# Nuxeo Sample Project

# Introduction

Sample Maven project that includes a core module and multiple modules for building different types of Nuxeo Packages for the [Nuxeo Marketplace](http://marketplace.nuxeo.com/).

Those sample MP ("Nuxeo Packages", formerly called "Marketplace Packages") will deploy this project into a Nuxeo Server.

The Nuxeo Package is a powerful packaging system when extending Nuxeo.

It improves your development process (for instance by allowing you to better automate the integration tests) but it also greatly improves the deployment process. For instance, with a Nuxeo Package you can easily send upgrades to your production team either as a ZIP, either directly within the server Administration page (where the administrator will see if a new package is available).

Moreover, the Nuxeo packaging system automatically maintains a bundles registry: you don't have anymore to track which files (especially the third-party libraries) must be added (or removed) to (or from) your server.

The assemblies are using [org.nuxeo.build:ant-assembly-maven-plugin](https://github.com/nuxeo/ant-assembly-maven-plugin).

The functional tests are using [org.nuxeo:nuxeo-ftest](https://github.com/nuxeo/tools-nuxeo-ftest).

## Content listing

    .
    |-- README.md
    |-- nuxeo-sample-project-core              * Nuxeo Sample Project Core Functionality
    |-- nuxeo-sample-project-package-explicit  * Nuxeo Package - explicit solution
    |-- nuxeo-sample-project-package-nonxr     * Nuxeo Package - no NXR solution
    |-- nuxeo-sample-project-package           * Nuxeo Package - recommended solution
    |   |-- pom.xml
    |   |-- src
    |   |   `-- main
    |   |       |-- assemble
    |   |       |   `-- assembly.xml             ** MP assembly
    |   |       `-- resources
    |   |           |-- install
    |   |           |   `-- templates
    |   |           |       `-- nuxeo-sample     ** Sample configuration template
    |   |           |           `-- nuxeo.defaults
    |   |           |-- install.xml              ** MP install instructions
    |   |           `-- package.xml              ** MP description
    |   `-- target                               ** Output directory with MP ZIP
    |
    |-- ftest
    |   |-- funkload                           * Functional or performance tests with Funkload
    |   |   |-- itests.xml
    |   |   |-- pom.xml
    |   |   `-- README.txt
    |   |
    |   |-- gatling                            * Performance tests with Gatling
    |   |   |-- itests.xml
    |   |   |-- pom.xml
    |   |   |-- README.md
    |   |   `-- src
    |   |       `-- test                         ** Scala and resources test files
    |   |           |-- resources
    |   |           |   |-- data
    |   |           |   |   |-- admins.csv
    |   |           |   |   `-- gatling-users.csv
    |   |           |   |-- gatling.conf
    |   |           |   `-- logback.xml
    |   |           `-- scala
    |   |               `-- org
    |   |                   `-- nuxeo
    |   |                       `-- sample
    |   |                           `-- gatling
    |   |                                `-- Feeders.scala
    |   |                                `-- RestClient.scala
    |   |                                `-- Sim00Setup.scala
    |   |                                `-- Sim10CreateDocuments.scala
    |   |                                `-- Sim20Cleanup.scala
    |   |
    |   |-- nightwatchjs                   * Functional tests with Nightwatchjs
    |   |   |-- itests.xml
    |   |   |-- nightwatch.json
    |   |   |-- package.json
    |   |   `-- pages
    |   |       `-- nuxeo.js
    |   |   |-- pom.xml
    |   |   |-- README.md
    |   |   `-- tests
    |   |       `-- smoke_test.js
    |   |-- startup                         * Startup Functional tests
    |   |   |-- itests.xml
    |   |   |-- pom.xml
    |   |
    |   `-- webdriver                       * Functional tests with WebDriver
    |       |-- itests.xml
    |       |-- pom.xml
    |       `-- src
    |           `-- test                      ** Java and resources test files
    |               |-- java
    |               |   `-- com
    |               |       `-- nuxeo
    |               |           `-- functionaltests
    |               |               `-- ITLoginLogoutTest.java
    |               `-- resources
    |                   `-- log4j.xml
    `-- pom.xml

## About the three sample Nuxeo Package solutions

There are multiple ways to build a Nuxeo Package. We only look here at those using Maven and
[org.nuxeo.build:ant-assembly-maven-plugin](https://github.com/nuxeo/ant-assembly-maven-plugin).

Here are three different solutions, from the recommended one to the simplest one.

### NXR method (recommended)

The recommended method is to build an NXR corresponding to the wanted result after the package install. Then we operate a comparison between that product and a reference one (usually the Nuxeo CAP), and we finally generate the Nuxeo Package which will perform the wanted install.

* PROS
  * Always up-to-date in regards to the dependencies and requirements (other bundles and third-party libraries).
  * Maven is aware of the project dependencies and can properly order the build in a multi-module project.
  * No maintenance: changes on transitive dependencies won't break the assembly, code is generic.
  * Perfect size: the package contains the exact necessary and sufficient content.
  * Factorization: the NXR can be attached to the reactor for being reused in another assembly.
* CONS
  * The drawback is it takes some build time and has a dependency on a whole Nuxeo distribution.
  * Maximum build time: the build performs more actions than the other methods and will consume more bandwidth.
  * Writing requires Maven knowledge on concepts such as the GAV, the dependency graph, the dependency management, the artifact resolution.

### No-NXR method

That method is using the same principle for building the Nuxeo Package as for building an NXR, with no final optimization. It is an acceptable compromise between speed and quality.

* PROS
  * It is as much reliable regarding at the dependencies as the above recommended method.
  * Maven is aware of the project dependencies and can properly order the build in a multi-module project.
  * No maintenance: changes on transitive dependencies won't break the assembly, code is generic.
  * Moderate build time: faster than the recommended method.
* CONS
  * The drawback is since the solution is empiric, it will likely embed useless files and generate a bigger archive.
  * Biggest size: the package contains the necessary but also useless content.
  * Writing requires Maven knowledge on concepts such as the GAV, the dependency graph, the dependency management, the artifact resolution.

### Explicit method

That latest method is explicitly listing everything that must be packaged. It is not recommended except for specific cases, quick solution or proof of concept.

* PROS
  * Easy method: very few code is required, you simply list what you want. It requires absolutely no Maven concept knowledge.
  * Minimum build time: the build is really fast.
* CONS
  * Maintenance required: you have to manually update the package assembly every time the dependencies change.
  * You also risk not to see that an indirect dependency has changed and requires some changes on the third-party libraries.
  * Managed size: the package contains exactly what you expect, with no guarantee of results.

## Applied to this sample project, here are the results from those three methods.

### NXR method (recommended)

4 directories, 6 files, 128KB. Build time: 11s.

    recommended/
    |-- install
    |   |-- artifacts-nuxeo-sample.properties
    |   |-- bundles
    |   |   `-- The sample project bundle.
    |   |-- templates
    |   |   `-- nuxeo-sample
    |   `-- test-artifacts-nuxeo-sample.properties
    |-- install.xml
    `-- package.xml

The `lib` directory is empty because all required third-parties are already included in the basic Nuxeo distribution.
The `bundles` directory only contains the sample project bundle because all its dependencies are also already included
in the basic distribution.

### No-NXR method.

5 directories, 150 files, 33MB. Build time: 6s.

    nonxr/
    |-- install
    |   |-- artifacts-nuxeo-sample.properties
    |   |-- bundles
    |   |   `-- 46 bundles.
    |   |-- lib
    |   |   `-- 99 third-party libraries.
    |   |-- templates
    |   |   `-- sample
    |   `-- test-artifacts-nuxeo-sample.properties
    |-- install.xml
    `-- package.xml

Here, we are embedding a lot of bundles and libraries which are useless because already included in the basic Nuxeo
distribution but that cannot be detected by the build process.

### Explicit method.

5 directories, 8 files, 1.6MB. Build time: 3s.

    explicit/
    |-- install
    |   |-- bundles
    |   |   `-- The sample project bundle, explicitly listed.
    |   |-- lib
    |   |   `-- 4 third-party libraries, explicitly listed.
    |   `-- templates
    |       `-- nuxeo-sample
    |-- install.xml
    `-- package.xml

That solution builds a lighter package than the no-NXR method but we don't know if it will be missing some dependencies
or not. The embedded bundles and libraries list must be manually maintained.

# Building
 
    mvn clean install

## Requirements
 
See [CORG/Compiling Nuxeo from sources](http://doc.nuxeo.com/x/xION)
 
## QA 
 
[![Build Status](https://qa.nuxeo.org/jenkins/buildStatus/icon?job=addons_nuxeo-sample-project-master)](https://qa.nuxeo.org/jenkins/job/addons_nuxeo-sample-project-master/)

## Deploying
 
Install [the Sample Nuxeo Package](https://connect.nuxeo.com/nuxeo/site/marketplace/package/nuxeo-sample).  
Or manually copy the built artifacts into `$NUXEO_HOME/templates/custom/bundles/` and activate the "custom" template.

# Resources
 
## Reporting issues

https://jira.nuxeo.com/browse/NXP/component/14503/
 
# Licensing
 
[GNU Lesser General Public License (LGPL) v2.1](http://www.gnu.org/licenses/lgpl-2.1.html)
 
# About Nuxeo
 
Nuxeo dramatically improves how content-based applications are built, managed and deployed, making customers more agile, innovative and successful. Nuxeo provides a next generation, enterprise ready platform for building traditional and cutting-edge content oriented applications. Combining a powerful application development environment with SaaS-based tools and a modular architecture, the Nuxeo Platform and Products provide clear business value to some of the most recognizable brands including Verizon, Electronic Arts, Sharp, FICO, the U.S. Navy, and Boeing. Nuxeo is headquartered in New York and Paris. More information is available at www.nuxeo.com.
