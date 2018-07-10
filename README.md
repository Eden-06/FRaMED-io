# FRaMED-io
This is the repository for the web-based reimplimentation of the FRaMED 2.0 editor for the family of role-based modeling languages.

## Execute demo
To simply run the project execute
```bash
./gradlew run
```
This will download all needed packages and starts an node.js server on port `3000`.

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