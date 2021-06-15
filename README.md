# Marc-André Goyette

## Installation

The project can be installed by executing the following commands.

``` shell
# Install the dependencies with NPM
npm install
```

## Build

The project can be built with the `build` script.

``` shell
# Run Webpack, then Hugo
npm run build
```

### Watching for changes from Webpack

Watching changes to SASS files requires starting Webpack in watch mode in a dedicated shell.

``` shell
npm run watch
```

### Watching for changes from Hugo

The following command starts a server that detect changes and reload them automatically.
It doesn't detect changes to SASS files, since it's Webpack that compiles SASS files.

``` shell
npm run start
```

## Deploying the site

The `deploy` script deploys the generated files from the build to GitHub pages.

```sh
npm run deploy
```

It copes the static site from `./public` to the path `../magoyette.github.io`

The `magoyette.github.io` repository should be set on the `master` branch.
The repo is used to commit and push the changes to GitHub pages.

## License

Copyright © 2020 Marc-André Goyette

The source code of the web site is distributed under the MIT License.

The content (the writings) is licensed under a
[Creative Commons License (CC BY-ND 4.0)](https://creativecommons.org/licenses/by-nd/4.0/).
