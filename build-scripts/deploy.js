const helpers = require("./build-scripts-helpers.js");

console.info("Starting to copy site to ../magoyette.github.io/");
helpers.replaceAllFilesInDirectory("public/*", "../magoyette.github.io/");
console.info("Site has been deployed to ../magoyette.github.io/");
