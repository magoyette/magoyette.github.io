#!/bin/bash
set -e

if [ -z "$1" ]; then
  scriptName=$(basename "$0")
  echo "Usage: $scriptName hugoVersion"
  exit 1
fi

scriptDir=$(dirname "$0")

if "$scriptDir"/../hugo/hugo version | grep -q "v$1"; then
  echo "Hugo v$1 is already installed: $("$scriptDir"/../hugo/hugo version)"
  exit 0
fi

hugoFolder="$scriptDir/../hugo"

mkdir -p "$hugoFolder"
tempFile="$hugoFolder/hugoTemp"

echo "Downloading Hugo v$1"
curl -L -o "$tempFile" "https://github.com/gohugoio/hugo/releases/download/v$1/hugo_extended_$1_Windows-64bit.zip"
unzip -o -j "$tempFile" "hugo.exe" -d "$hugoFolder"

rm "$tempFile"

echo "Hugo version v$1 has been installed: $("$scriptDir"/../hugo/hugo version)"
