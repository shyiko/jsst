#!/bin/bash
GPG_PASSPHRASE=
TARGET_DIRECTORY=build/oss-release
VERSION=1.0.4

function copyToOssRelease {
    for f in $1/*.*; do
        echo "Copying $f to $TARGET_DIRECTORY";
        cp $f $TARGET_DIRECTORY
    done
}

echo "Preparing files..."
rm $TARGET_DIRECTORY/*.jar 
rm $TARGET_DIRECTORY/*.asc
copyToOssRelease build/dist/lib
copyToOssRelease build/dist/src
copyToOssRelease build/dist/docs/javadocs

echo "Running PGP..."
for f in $TARGET_DIRECTORY/*.*; do
    echo "Processing $f";
    echo $GPG_PASSPHRASE|gpg -ab --passphrase-fd 0 --yes --no-tty $f
done

echo "Making bundle..."
cd $TARGET_DIRECTORY;
for artifact in "jsst" "jsst-antlib" "jsst-core" "jsst-junit" "jsst-testng" "jsst-war"
do
    jar -cvf ../$artifact-$VERSION-oss-bundle.jar $artifact-$VERSION*    
done
