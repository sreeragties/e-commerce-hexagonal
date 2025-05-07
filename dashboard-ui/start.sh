#!/bin/sh

# Check if the directory is empty
if [ -z "$(ls -A .)" ]; then
  # Create a new Angular project if the directory is empty
  ng new dashboard-fronted --directory . --routing --style scss --strict --standalone --view-encapsulation ShadowDom --ssr false --interactive false
fi

npm install
ng serve --host 0.0.0.0 --port 4200 --disable-host-check