# web-app

Example of native web app with Jetty web server and Clojure.

## Prerequisites

1. GraalVM JDK 11 or 17 and native-image plugin
2. Leiningen

## Build

Create uberjar:

    $ lein uberjar

Create native image via lein-shell plugin:

    $ lein native

## Usage

Run jar:

    $ java -jar target/uberjar/web-app-0.1.0-SNAPSHOT-standalone.jar

Run native-image:

    $ target/web-app


## License

Copyright © 2022 primenumsdev

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
