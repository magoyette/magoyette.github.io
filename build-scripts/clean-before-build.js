const helpers = require("./build-scripts-helpers.js");

helpers.deleteFilesInDirectoryRecursively("public/");
helpers.deleteFile("static/styles.css");
helpers.deleteFile("static/main.js");
