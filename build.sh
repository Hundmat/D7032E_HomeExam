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
JSON_JAR="json.jar"
SERVER_IP="127.0.0.1"  # Hardcoded IP address for the client to connect to
MAIN_SERVER="__init__"

# Create the build directory if it doesn't exist
mkdir -p "$BUILD_DIR"

# Create a classpath string that includes all .jar files in the lib directory, including json.jar
LIB_CP=$(find "$LIB_DIR" -name "*.jar" | tr '\n' "$SEP")
if [ -z "$LIB_CP" ]; then
    LIB_CP="$JSON_JAR"
else
    LIB_CP="$JSON_JAR$SEP$LIB_CP"
fi

# Add build directory to classpath
FULL_CP="$BUILD_DIR$SEP$LIB_CP"

# Compile the Java source files
echo "Compiling Java source files..."
javac -cp "$LIB_CP" -d "$BUILD_DIR" $(find "$SRC_DIR" -name "*.java")

# Check if the compilation was successful
if [ $? -eq 0 ]; then
    echo "Compilation successful."

    # Ask the user if they want to start as a server or a client
    echo "Do you want to run as a server or a client? (server/client)"
    read ROLE

    if [ "$ROLE" = "server" ]; then
        # Run the server

       
        echo "Running the server interactively..."
        java -cp "$FULL_CP" "$MAIN_SERVER"
    elif [ "$ROLE" = "client" ]; then
        # Run the client and connect to the hardcoded IP address
        echo "Connecting to server at $SERVER_IP..."
        java -cp "$FULL_CP" "$MAIN_CLASS" "$SERVER_IP"
    else
        echo "Invalid option. Please run the script again and choose 'server' or 'client'."
    fi
    
else
    echo "Compilation failed."
fi
