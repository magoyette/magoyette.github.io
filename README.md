# marcandregoyette.com

The source code of my personal web site. It's a static site written in the Clojure programming language and generated with the help of Stasis.

## Compatibility

marcandregoyette.com currently cannot be built on Windows and on some Linux distros (CentOS, RHEL), because the Optimus library depends on clj-v8. Optimus will probably switch to Nashorn, which will allow compatibility with Windows and all Linux distros.

This site has never been built with Mac OS X, so compatibility is unknown.

Semantic-UI is likely to not work properly with a version of Node.js inferior to 0.12 (there's some workarounds to make it work with 0.10).

## Building Semantic-UI

[Semantic-UI](http://semantic-ui.com/) needs to be installed to build [marcandregoyette.com](http://marcandregoyette.com). This is necessary because the site uses a lightly customized version of the default theme of Semantic-UI. The [Getting Started](http://semantic-ui.com/introduction/getting-started.html) page explains how to install Semantic-UI.

Currently, it requires to:
- install [Node](https://nodejs.org) and [npm](https://www.npmjs.com/)
- install [Gulp](http://gulpjs.com/) globally
```sh
$ sudo npm install -g gulp
```
- install Semantic-UI in the root folder of this repository
```sh
npm install semantic-ui --save
```
```
Set-up Semantic UI:
  Custom (Customize all src/dist values)

Is this your project folder?
  <path>/marcandregoyette.com
  Yes

Where should we put Semantic UI inside your project?
  semantic/
```
- run customize-semantic-ui.sh
```sh
chmod a+x ./customize-semantic-ui.sh
./customize-semantic-ui.sh
```
- build Semantic-UI with Gulp
```sh
cd semantic/
gulp build
```

The Semantic-UI files required by marcandregoyette.com will be exported in /resources/public/styles/ and /resources/public/themes/.

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

Copyright © 2014-2016 Marc-André Goyette

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
