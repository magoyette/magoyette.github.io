const path = require("path");
const fs = require("fs-extra");
const glob = require("glob");
const { NamedModulesPlugin } = require("webpack");

function deleteFile(file) {
  try {
    fs.removeSync(file);
  } catch (err) {
    console.error(err);
  }
}

function deleteFilesInDirectoryRecursively(directoryPath) {
  // dotfiles aren't globbed, so .git isn't deleted
  glob(directoryPath + "*", { absolute: true }, (er, files) => {
    if (er) {
      console.error(er);
    }
    files.forEach((file) => {
      deleteFile(file);
    });
  });
}

function copyFileToDirectory(filePath, destinationDirPath) {
  const filename = path.basename(filePath);
  try {
    return fs.copySync(filePath, path.join(destinationDirPath, filename));
  } catch (err) {
    console.error(err);
  }
}

function copyFilesInDirectoryToAnotherDirectory(
  sourceDirPath,
  destinationDirPath
) {
  glob(sourceDirPath, { absolute: true }, (err, files) => {
    if (err) {
      console.error(err);
    } else {
      files.forEach((file) => copyFileToDirectory(file, destinationDirPath));
    }
  });
}

function replaceAllFilesInDirectory(sourceDirPath, destinationDirPath) {
  deleteFilesInDirectoryRecursively(destinationDirPath);
  copyFilesInDirectoryToAnotherDirectory(sourceDirPath, destinationDirPath);
}

module.exports.deleteFile = deleteFile;
module.exports.copyFileToDirectory = copyFileToDirectory;
module.exports.copyFilesInDirectoryToAnotherDirectory = copyFilesInDirectoryToAnotherDirectory;
module.exports.replaceAllFilesInDirectory = replaceAllFilesInDirectory;
module.exports.deleteFilesInDirectoryRecursively = deleteFilesInDirectoryRecursively;
