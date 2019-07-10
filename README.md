# marcandregoyette.com

The source code of my personal web site. It's a static site written in the Clojure programming language.

### Environment setup

- Install Java 8 or Java 11. This site is currently used with OpenJDK 11.

- Install Leiningen (version should be at least 2.9.0).

- Install [Node](https://nodejs.org) and [npm](https://www.npmjs.com/).

### Building the Sass styles

The npm packages must first be installed.

``` shell
npm install
```

The Sass styles can be built with NPM.

``` shell
# Build the Sass files as CSS
npm run css-build

# Build continuously the Sass files as CSS when the Sass files are modified
npm run css-watch
```

## Deploying the site locally

To deploy the site on a local server, run the `start` goal.

```sh
lein start
```

The `test` goal is executed before the server is started. Failing tests will prevent the server from starting.

`lein ring server` can be used to start the local server without running `test`.

## Exporting the site

The `export` script allows to generate the static site for a deployment on GitHub pages.

```sh
./export
```

It includes many tasks:

- the lein goal `test` is executed to run the tests
- the lein goal `export` to generate the static site
- the npm script `format-html` to format the HTML files
- Bash commands to copy the static site to the path `../magoyette.github.io`

The `magoyette.github.io` repository should be set on the `master` branch and is used to commit and push the changes to GitHub pages.

The HTML files are formatted to make their content easier to review in a git diff.

## License

Copyright � 2014-2019 Marc-Andr� Goyette

Distributed under the Eclipse Public License either version 1.0 or (at your option) any later version.
