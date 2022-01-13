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
5. Installed new java with 100 priority: 
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
2. Add java alternative with priority 200:
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

