# marcandregoyette.com

The source code of my personal web site. It's a static site written in the Clojure programming language and generated with the help of Stasis.

## Building Semantic-UI

[Semantic-UI](http://semantic-ui.com/) needs to be installed to build [marcandregoyette.com](http://marcandregoyette.com). This is necessary because the site uses a lightly customized version of the default theme of Semantic-UI. The [Getting Started](http://semantic-ui.com/introduction/getting-started.html) page explains how to install Semantic-UI.

Currently, it requires to:
- install [Node](https://nodejs.org) and [npm](https://www.npmjs.com/)
- install [Gulp](http://gulpjs.com/) globally
```sh
$ npm install -g gulp
```
- execute npm install semantic-ui --save in the root folder of this repository
```sh
npm install semantic-ui --save
```
- build Semantic-UI with Gulp
```sh
cd semantic/
gulp build
```

The Semantic-UI files required by marcandregoyette.com will be exported in /resources/public/styles/ and /resources/public/themes/.

## License

Copyright © 2014 Marc-André Goyette

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
