#!/bin/bash
cp ./semantic-ui-site/semantic.json ./semantic.json
cp -r ./semantic-ui-site/src/ ./semantic/
cd semantic
gulp build
cd ..
