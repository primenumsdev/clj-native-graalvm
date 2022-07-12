# clj-native-graalvm

Building native images with Clojure and GraalVM.

[github.com/clj-easy/graalvm-clojure](https://github.com/clj-easy/graalvm-clojure/blob/master/doc/clojure-graalvm-native-binary.md)


## Prerequisites

### Step 1 - Installation GraalVM on Ubuntu

[Tutorial](https://dev.to/fahadisrar/guide-to-install-graalvm-community-edition-on-ubuntu-38h8)

1. Download GraalVM binaries - https://github.com/graalvm/graalvm-ce-builds/releases
2. Extract it: `tar -xvzf graalvm-ce-java11-linux-amd64-21.3.0.tar.gz`
3. Move it: `mv graalvm-ce-java11-21.3.0/ /usr/lib/jvm/ && cd /usr/lib/jvm`
4. Check current java alternatives: `update-alternatives --config java`
5. Add new java alternative with 100 priority: 
`sudo update-alternatives --install /usr/bin/java java /usr/lib/jvm/graalvm-ce-java11-21.3.0/bin/java 100`
6. Check java version: `java --version`
7. Create symlink: `ln -s graalvm-ce-java11-21.3.0 graalvm`
8. Add env variables to `~/.bashrc` file:
```
export GRAALVM_HOME=/usr/lib/jvm/graalvm
```
9. Reload `source ~/.bashrc`
10. Update PATH for the current session: `export PATH=$GRAALVM_HOME/bin:$PATH`, didn't save it to .bashrc
since it is breaking update-alternatives switching between java versions.
11. Test that `gu` command is available

### Step 2 - Installation of GraalVM native-image plugin

Run: `gu install native-image`

Check it's available: `native-image`

### Step 3 - Create a new Clojure project

Assuming you have Leiningen installed: `sudo apt install leiningen`

Run: `lein new app hello-world`

### Step 4 - Build and run uberjar

Build: `cd hello-world && lein uberjar`

Run and measure time: `time java -jar target/uberjar/hello-world-0.1.0-SNAPSHOT-standalone.jar`

### Step 5 - Build native image

Build:
```
native-image --report-unsupported-elements-at-runtime \
             --initialize-at-build-time \
             --no-server \
             -jar target/uberjar/hello-world-0.1.0-SNAPSHOT-standalone.jar \
             -H:Name=./target/hello-world
```

Run and measure time: `time target/hello-world`

GraalVM options - https://www.graalvm.org/reference-manual/native-image/Options/


## Adding Java 17

1. Download and install Java 17.
2. Add new java alternative with priority 200:
`sudo update-alternatives --install /usr/bin/java java /usr/lib/jvm/graalvm-ce-java17-21.3.0/bin/java 200`
3. Select new java: `sudo update-alternatives --config java`
4. Check current version: `java --version`
5. Update symlink: `sudo ln -s graalvm-ce-java17-21.3.0 graalvm`
6. Check that native-image is not installed for this java version

With Java 17 GraalVM I had to switch to root user in order to install native-image:
7. Switch to root: `sudo su`
8. Set env var for current session: 
```
    export GRAALVM_HOME=/usr/lib/jvm/graalvm-ce-java17-21.3.0
    export PATH=$GRAALVM_HOME/bin:$PATH
```
9. Install native-image: `gu install native-image`
10. Exit from root: `exit`
11. Check that native-image available

Tip: to remove java alternative run: `sudo update-alternatives --remove java /usr/lib/jvm/graalvm-ce-java11-21.3.0/bin/java`

## Using Docker

### Building JAR docker image

[Tutorial](https://dev.to/diogok/efficient-clojure-multistage-docker-images-with-java-and-native-image-1e9i)

[Tutorial 2](https://blog.appsignal.com/2021/10/19/how-to-dockerize-an-existing-nodejs-application.html)

Docker commands:

1. List all docker images: `docker images`
2. Build a new docker image: `docker build . -t image-name`
3. Delete docker image by ID: `docker rmi image-id`
4. Dangling docker images none:none - https://projectatomic.io/blog/2015/07/what-are-docker-none-none-images/
5. Cleanup dangling docker images: `docker rmi $(docker images -f "dangling=true" -q) --force`, but that drops stage cache.
6. Images none:none remain until old containers that were created from them still exists, even if stopped.[Source](https://stackoverflow.com/questions/51612208/how-to-delete-cached-intermediate-docker-images-after-the-cache-gets-invalidated)
7. To clear all dangling images without associated containers: `docker image prune`

### Optimizing Docker build time

Tip: to speed up docker build, use .dockerignore - https://docs.docker.com/engine/reference/builder/#dockerignore-file

Tip: Multistage builds, and run to specific stage - https://docs.docker.com/develop/develop-images/multistage-build/#stop-at-a-specific-build-stage

TODO: Try buildkit - https://docs.docker.com/engine/reference/builder/#buildkit

### Optimizing Docker image size

1. Use JRE instead of JDK for the final stage - https://adoptium.net/releases.html?variant=openjdk17&jvmVariant=hotspot
2. Use Adoptium Docker Hub - https://hub.docker.com/_/eclipse-temurin
3. Use alpine linux images - https://hub.docker.com/layers/eclipse-temurin/library/eclipse-temurin/17-jre-alpine/images/sha256-839f3208bfc22f17bf57391d5c91d51c627d032d6900a0475228b94e48a8f9b3?context=explore
4. Use scratch image for native apps - https://hub.docker.com/_/scratch


## Installing GraalVM on MacOs with jenv

1. Check installed jenv versions: `jenv versions`
2. Download GraalVM binaries - https://github.com/graalvm/graalvm-ce-builds/releases
3. Extract it: `tar -xvzf graalvm-ce-java11-22.1.0.tar.gz`
4. Remove quarantine attribute on bits: `sudo xattr -r -d com.apple.quarantine path/to/graalvm/folder/`
5. Check where jenv stores java versions: `jenv which java`
6. Move new GraalVM JDK to the jenv versions folder: ` mv ~/graalvm-ce-java11-22.1.0 ~/.jenv/versions/graalvm-ce-java11-22.1.0`
7. Add new JDK to jenv: `jenv add ~/.jenv/versions/graalvm-ce-java11-22.1.0`
8. Check that it was added: `jenv versions`
9. Use the new GraalVM JDK globally: `jenv global graalvm-ce-java11-22.1.0`
10. Check current java version: `java -version`
11. Add env variables to `~/.bashrc` file:
```
export GRAALVM_HOME=~/.jenv/versions/graalvm-ce-java11-22.1.0
```
12. Reload `source ~/.bashrc`
13. Update PATH for the current session: `export PATH=$GRAALVM_HOME/bin:$PATH`, didn't save it to .bashrc
since it is breaking jenv versions.
14. Test that `gu` command is available

### Step 2 - Installation of GraalVM native-image plugin

Run: `gu install native-image`

Check it's available: `native-image`
