# marcandregoyette.com

The source code of my personal web site. It's a static site written in the Clojure programming language and generated with the help of Stasis.

## Compatibility

marcandregoyette.com currently cannot be built on Windows and on some Linux distros (CentOS, RHEL), because the Optimus library depends on clj-v8. Optimus will probably switch to Nashorn, which will allow compatibility with Windows and all Linux distros.

This site has never been built with Mac OS X, so compatibility is unknown.

Semantic-UI is likely to not work properly with a version of Node.js inferior to 0.12 (there's some workarounds to make it work with 0.10).

### Clojure environment

- Install a Java 8 JVM.

- Install Leiningen.

### Node environment

- Install [Node](https://nodejs.org) and [npm](https://www.npmjs.com/).

- Install [Gulp](http://gulpjs.com/) globally.
``` shell
$ sudo npm install -g gulp
```

### Installing the dependencies

Execute lein deps to download the Clojure dependencies with Leiningen and Semantic-UI with NPM.

``` shell
lein deps
```

### Building Semantic-UI

Build Semantic UI.

``` shell
lein build-semantic
```

After the build, the Semantic-UI files required by marcandregoyette.com will be exported in /resources/public/styles/ and /resources/public/themes/.

## Deploying the site locally

To deploy the site on a local server, run the following command in the root folder of the prohject :
```sh
lein ring server
```

The Marginalia documentation won't be available, since it is only builded when the site is exported.

## Exporting the site

lein export allows to generate the static site and the Marginalia documentation for its source code.
```sh
lein export
```

## License

Copyright © 2014-2018 Marc-André Goyette

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
