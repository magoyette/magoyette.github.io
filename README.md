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

Execute the `deps` goal to download the Clojure dependencies with Leiningen, download Semantic-UI with NPM and build a customized version of Semantic UI.

``` shell
lein deps
```

### Rebuilding Semantic-UI

Semantic UI can be rebuilt with the `build-semantic` goal.
This is necessary when the `semantic` folder is modified.
Unlike `deps`, Semantic UI isn't reinstalled and Clojure dependencies aren't updated.

``` shell
lein build-semantic
```

## Deploying the site locally

To deploy the site on a local server, run the `start` goal.

```sh
lein start
```

The `test` goal is executed before the server is started. Failing tests will prevent the server from starting. `lein ring server` can be used to start the local server even if some tests fails.

The Marginalia documentation won't be available, since it is only built when the `export` or the `deploy` goal is executed.

## Exporting the site

The `export` goal allows to generate the static site and the Marginalia documentation.

```sh
lein export
```

## Deploying the site

The `deploy` goal includes `export`, but also copies the generated static site to the path `../magoyette.github.io`. The `magoyette.github.io` repository should be set on the `master` branch and is used to commit and push the changes to GitHub pages.

``` sh
lein deploy
```

The `test` goal is executed before the `export` goal is executed. Failing tests will prevent the deployment from completing.

## License

Copyright © 2014-2019 Marc-André Goyette

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
