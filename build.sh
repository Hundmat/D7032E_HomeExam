#!/bin/bash

# Determine path separator based on OS
if [ "$(uname)" = "Linux" ] || [ "$(uname)" = "Darwin" ]; then
    SEP=":"
else
    SEP=";"
fi

# Variables for directories and files
SRC_DIR="src"
BUILD_DIR="build"
LIB_DIR="lib"
MAIN_CLASS="PointSalad"

# Create the build directory if it doesn't exist
mkdir -p "$BUILD_DIR"

# Create a classpath string that includes all .jar files in the lib directory
LIB_CP=$(find "$LIB_DIR" -name "*.jar" | tr '\n' "$SEP")

# Compile the Java source files
echo "Compiling Java source files..."
javac -cp "$LIB_CP" -d "$BUILD_DIR" "$SRC_DIR"/*.java

# Check if the compilation was successful
if [ $? -eq 0 ]; then
    echo "Compilation successful."

    # Run the main class with the correct classpath
    echo "Running the $MAIN_CLASS..."
    java -cp "$BUILD_DIR$SEP$LIB_CP" "$MAIN_CLASS"
else
    echo "Compilation failed."
fi
