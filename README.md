[![Build Status](https://travis-ci.org/Eden-06/FRaMED-io.svg?branch=master)](https://travis-ci.org/Eden-06/FRaMED-io)

# FRaMED-io
This is the repository for the web-based reimplimentation of the FRaMED 2.0 editor for the family of role-based modeling languages.

## Run Demo Only

Simply open <https://Eden-06.github.io/FRaMED-io>.

## Run Demo Locally
The easiest way to start FRaMED-io is to run from your command line:
```bash
./gradlew run
```
and open <http://localhost:3000> afterwards.

This command will download all needed dependencies and starts an node.js server on port `3000`.
For this you need `node` and `npm` in the path variable.

## Setup development environment
For development start the node.js server manually.
```bash
cd web
npm install #Only before first execution
node index.js
```
The js file can be compiled with
```bash
./gradlew build
```
