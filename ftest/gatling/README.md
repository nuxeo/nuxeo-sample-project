# Nuxeo Gatling Sample

## Requirements

- A running Nuxeo server instance
- Setup an existing administrator account in the file: `src/test/resources/data/admins.csv`
- Setup a list of users that will be created as members and used in the bench: `src/test/resources/data/gatling-users.csv`.
  Default file contains 100 users.
- Increase vcs and db pool size to 60 in `nuxeo.conf`: `nuxeo.*.max-pool-size`

## Simulations

### Setup Simulation

This simulation initializes the environment and needs to be run first, it is idempotent.

- Create a Gatling user group
- Create a bench workspace
- Grant ReadWrite permission to the Gatling group on the bench workspace
- Create all users in this group

### Create Documents Simulation

Simulates document creation in the bench workspace.

- Peek a random Nuxeo user
- Create a File document in the bench workspace

### Cleanup Simulation

This simulation removes all documents, users and group from the Nuxeo instance.

## Launching Simulations

### All in One

Sets up a Nuxeo instance with the required packages and configuration, runs all the simulations and stops the Nuxeo instance.

    mvn -nsu verify

You can add the following profiles:

- `pgsql`: use a PostgreSQL database as a backend for Nuxeo
- `monitor`: record metrics to Graphite

Default options: see below.

### Running a Single Simulation on an Running Nuxeo Instance

    mvn -nsu gatling:execute -Dgatling.simulationClass
    ...
    Choose a simulation number:
         [0] org.nuxeo.sample.gatling.Sim00Setup
         [1] org.nuxeo.sample.gatling.Sim10CreateDocuments
         [2] org.nuxeo.sample.gatling.Sim20Cleanup

Common options with default values:

    # Nuxeo target URL
    -Durl=http://localhost:8080/nuxeo
    # Duration in seconds of the simulation
    -Dduration=60
    # Sleep time in seconds between document creations
    -Dpause=1
    # Number of concurrent users
    -Dusers=10
    # Time in seconds to reach the target number of concurrent users
    -Dramp=5

Note that you may need to edit the administrator account if it is not the default one:

    src/test/resources/data/admins.csv

You can also bypass the interactive mode and execute a given simulation:

    mvn -nsu gatling:execute -Dgatling.simulationClass=org.nuxeo.sample.gatling.Sim00Setup

## Licensing

[Apache License, Version 2.0 (the "License")](http://www.apache.org/licenses/LICENSE-2.0)
