#!/bin/bash
set -e

if ./hugo/hugo version | grep -q "v$1"; then
  echo "Hugo v$1 is already installed"
  exit 0
fi

hugoFolder="./hugo"

mkdir -p "$hugoFolder"
tempFile="$hugoFolder/hugoTemp"

echo "Downloading Hugo v$1"
curl -L -o "$tempFile" "https://github.com/gohugoio/hugo/releases/download/v$1/hugo_extended_$1_Windows-64bit.zip"
unzip -o -j "$tempFile" "hugo.exe" -d "$hugoFolder"

rm $tempFile

echo "Hugo version v$1 has been installed"
