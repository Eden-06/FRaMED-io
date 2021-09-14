[![Build Status](https://app.travis-ci.com/Eden-06/FRaMED-io.svg?branch=master)](https://app.travis-ci.com/Eden-06/FRaMED-io)

# FRaMED-io
This is the repository for the web-based reimplimentation of the FRaMED 2.0 editor for the family of role-based modeling languages.

## Run Demo Only

Simply open <https://Eden-06.github.io/FRaMED-io>.

## Run Demo Locally

To build this project and start a dev serv run:
```bash
./gradlew run
```
This will serve FRaMED-io at <http://localhost:8080>.

## Create production build

```bash
./gradlew dist

# Optionally run production build
# npm install -g serve
serve dist
```
