


# PointSalad - Project Setup and Testing Guide

This guide will help you set up, run, and test the **PointSalad** project using the provided Bash scripts.

## Requirements

- **Java Development Kit (JDK) 11 or higher**
- **JUnit 5** for running the unit tests
- **JSON library** (already included in the `lib` directory)
- **Unix-like shell** (Linux, macOS, or compatible shell environment on Windows)

## Running the Game

To start the game, use the `build.sh` script. This script compiles the Java files and allows you to run the application either as a server or a client, with multiple gameplay modes available.

### Gameplay Modes

In **PointSalad**, you can choose from the following gameplay modes:
- **Player vs Player (PvP)**: Two players compete against each other.
- **Player vs Bot (PvBot)**: A single player competes against a bot using the game's bot logic.
- **Bot vs Bot (BvB)**: Two bots compete against each other using the bot logic while you observe.

### Steps to Run the Game:

1. Open your terminal and navigate to the project root directory.
2. Run the following command to start the game:
   ```bash
   ./build.sh
   ```
3. You will be prompted to choose whether to run the game as a server or client:
   - Type `server` to host the game.
   - Type `client` to connect to a server. You will be asked to provide the server's IP address (default is `127.0.0.1`).

4. After connecting, you will be prompted to select a gameplay mode:
   - For **Player vs Player**, both players can connect as clients.
   - For **Player vs Bot**, one player will connect as a client, while the server runs the bot.
   - For **Bot vs Bot**, the server will control both bots using bot logic, and you can observe the game.

**Note:** Ensure that the necessary JAR files (e.g., `json.jar`) are available in the `lib` directory before running the script.

## Running Unit Tests

Three separate unit test scripts are provided to test different aspects of the PointSalad application. Each script compiles the code, runs the tests using JUnit, and outputs the results.

### Available Test Scripts:

1. **`unitTestInitial.sh`** - Tests the initialization functionality of the game.
   ```bash
   ./unitTestInitial.sh
   ```

2. **`unitTestGame.sh`** - Tests the game loop and related functionality.
   ```bash
   ./unitTestGame.sh
   ```

3. **`unitTestScore.sh`** - Tests the scoring system of the game.
   ```bash
   ./unitTestScore.sh
   ```

### Steps to Run a Test:

1. Open your terminal and navigate to the project root directory.
2. Run one of the test scripts depending on the functionality you want to test:
   ```bash
   ./<test_script_name>.sh
   ```
   For example, to run the game loop tests:
   ```bash
   ./unitTestGame.sh
   ```

### Notes:

- Ensure that the `junit-platform-console-standalone-1.11.2.jar` file is available in the `lib` directory, as this is used to run the tests.
- After compilation, the JUnit tests will automatically run, and the results will be displayed in the terminal.

---

