#!/bin/bash
set -e

echo "Removing files from the previous deployment"
# dotfiles aren't globbed, so .git isn't deleted
rm -rf ../magoyette.github.io/*

echo "Copying files from the current deployment"
cp -r ./public/* ../magoyette.github.io
