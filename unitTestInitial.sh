#!/bin/bash

# Determine path separator based on OS
if [ "$(uname)" = "Linux" ] || [ "$(uname)" = "Darwin" ]; then
    SEP=":"
else
    SEP=";"
fi

# Variables for directories and filesä
Testing_DIR="testing"
SRC_DIR="src"
BUILD_DIR="build"
LIB_DIR="lib"
MAIN_CLASS="MainSalad"
JSON_JAR="lib/json.jar"
SERVER_IP="127.0.0.1"  # Hardcoded IP address for the client to connect to


# Function to clean build directory
clean_build() {
    echo "Cleaning build directory..."
    rm -rf "$BUILD_DIR"/*
}

# Create the build directory if it doesn't exist
mkdir -p "$BUILD_DIR"

# Check if lib directory exists
if [ ! -d "$LIB_DIR" ]; then
    echo "Library directory '$LIB_DIR' does not exist."
    exit 1
fi

# Create a classpath string that includes all .jar files in the lib directory
LIB_CP=$(find "$LIB_DIR" -name "*.jar" | tr '\n' "$SEP")
if [ -z "$LIB_CP" ]; then
    echo "No .jar files found in the library directory. Exiting."
    exit 1
fi

# Add json.jar to the classpath
LIB_CP="$JSON_JAR$SEP$LIB_CP"

# Add build directory to classpath
FULL_CP="$BUILD_DIR$SEP$LIB_CP"

# Compile the Java source files
echo "Compiling Java source files..."
javac -cp "$LIB_CP" -d "$BUILD_DIR" $(find "$SRC_DIR" -name "*.java")


echo "Running InitialisationTesting"
java -cp "$LIB_DIR/junit-platform-console-standalone-1.11.2.jar${SEP}$FULL_CP" org.junit.platform.console.ConsoleLauncher -c testing.InitialisationTesting


    