#!/usr/bin/env bash

set -e

printf "Release version? "
read release_version

mvn -q versions:set -DnewVersion=$release_version
git add pom.xml
git commit -q -m "Prepare release $release_version"
git tag -a featured-shared-$release_version -m "Release $release_version"

printf "New development version? "
read dev_version

mvn -q versions:set -DnewVersion=$dev_version
git add pom.xml
git commit -q -m "New release cycle $dev_version [ci skip]"

mvn -q versions:commit
git push --follow-tags