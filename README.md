# marcandregoyette.com

The source code of my personal web site. It's a static site written in the Clojure programming language and generated with the help of Stasis.

## Compatibility

marcandregoyette.com currently cannot be built on Windows and on some Linux distros (CentOS, RHEL), because the Optimus library depends on clj-v8. Optimus will probably switch to Nashorn, which will allow compatibility with Windows and all Linux distros.

This site has never been built with Mac OS X, so compatibility is unknown.

Semantic-UI is likely to not work properly with a version of Node.js inferior to 0.12 (there's some workarounds to make it work with 0.10).

## Building Semantic-UI

[Semantic-UI](http://semantic-ui.com/) needs to be installed to build [marcandregoyette.com](http://marcandregoyette.com). This is necessary because the site uses a lightly customized version of the default theme of Semantic-UI.

### Semantic-UI dependencies

Install [Node](https://nodejs.org) and [npm](https://www.npmjs.com/).

Install [Gulp](http://gulpjs.com/) globally.
``` shell
$ sudo npm install -g gulp
```

### Install and build Semantic-UI

Run the script install-semantic-ui.sh to install Semantic-UI with the settings from the /semantic-ui-site folder. /semantic-ui-folder is used to override some of the default settings of Semantic-UI without having to maintain a Semantic-UI theme or a site.

``` shell
chmod a+x ./install-semantic-ui.sh
./install-semantic-ui.sh
```

During the Semantic-UI installation, answer the following questions.
```
Set-up Semantic UI:
  Automatic (Use defaults locations and all components)

Is this your project folder?
  <path>/marcandregoyette.com
  Yes

Where should we put Semantic UI inside your project?
  semantic/
```

The settings from /semantic-ui-site/semantic.json will be applied, even if the installation is done in Automatic mode.

install-semantic-ui.sh will also do a build with Gulp, so there will be nothing left to do before deploying the web site.

After the build, the Semantic-UI files required by marcandregoyette.com will be exported in /resources/public/styles/ and /resources/public/themes/.

### Rebuilding Semantic-UI

After a change to the files in /semantic-ui-site, Semantic-UI can be rebuilt with Gulp by running rebuild-semantic-ui.sh.
``` shell
chmod a+x ./rebuild-semantic-ui.sh
./rebuild-semantic-ui.sh
```

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
