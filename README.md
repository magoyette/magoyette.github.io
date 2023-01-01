# Marc-André Goyette

## Requirements

This projet requires Node v14 or greater with NPM 6 or greater.

On Windows, NPM must be configured to run Bash scripts. An alternative is to run the project in WSL.

```shell
# Configure NPM to run Bash scripts with Git Bash
npm config set script-shell "C:\\Program Files\\git\\bin\\bash.exe"
```

## Installation

Run `npm install` to download the Bulma CSS framework and the Hugo static site generator.

```shell
# Install Bulma and Hugo
npm install
```

A `postinstall` script download Hugo with the version specified in the `config.hugoVersion` of the `package.json` file.

### Start

The project can be started with the `start` script. Hugo will watch the files for changes and rebuild the project when necessary.

```shell
npm run start
```

## Build

The project can be built once with the `build` script.

```shell
npm run build
```

## Export

The `export` script copy the generated files to prepare a deployment on GitHub pages.

Another copy of this repository set on the `main` branch must be cloned at the path `../magoyette.github.io`. The files will be copied to that directory. A Git commit and a Git push can then be done to deploy the changes.

```sh
npm run deploy
```

## License

Copyright © 2020-2023 Marc-André Goyette

The source code of the web site is distributed under the MIT License.

The content (the writings) is licensed under a
[Creative Commons License (CC BY-ND 4.0)](https://creativecommons.org/licenses/by-nd/4.0/).
