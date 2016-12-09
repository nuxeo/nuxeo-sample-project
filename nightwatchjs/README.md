# Nuxeo Nightwatchjs Sample

Nightwatch.js is an automated testing framework for web applications and websites, written in Node.js and using the W3C WebDriver API (formerly Selenium WebDriver).

It is a complete browser (End-to-End) testing solution which aims to simplify the process of setting up Continuous Integration and writing automated tests. Nightwatch can also be used for writing Node.js unit tests.

## Getting started

###Browser/IDE Setup

Here is the [nightwathjs wiki](https://github.com/nightwatchjs/nightwatch/wiki) to have information about browser tests setup, running/debugging in your favorite IDE.

###Configuration

The test runner expects a configuration file to be passed, using by default a `nightwatch.json` file from the current directory, if present. A `nightwatch.conf.js` file will also be loaded by default, if found.

[Here's an example](http://nightwatchjs.org/getingstarted#settings-file) (you can have a look in the present repository itself too)

###Page Object API

In order to factorize your tests, the Page Object API has been introduced in Nightwatchjs and allows developers to create different page descriptors with the related selectors for re-using them easily in the IT tests.

[More information here](https://github.com/nightwatchjs/nightwatch/wiki/Page-Object-API)

###The Command Queue

When Nightwatch runs a test, it processes its commands in a list known as the command queue. This list manages the asynchronous execution of the commands defined in that test.

[More information here](https://github.com/nightwatchjs/nightwatch/wiki/Understanding-the-Command-Queue)

###API

Please refer to this [developer guide](http://nightwatchjs.org/guide) to see how to use:

- Default commands
- CSS or xpath Selectors
- Pages
- APIs

## Licensing

Nightwatchjs: https://github.com/nightwatchjs/nightwatch/blob/master/LICENSE.md

Nuxeo: [Apache License, Version 2.0 (the "License")](http://www.apache.org/licenses/LICENSE-2.0)
