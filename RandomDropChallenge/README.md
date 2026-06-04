# RandomDropChallenge

A Minecraft challenge plugin where every block you break drops a completely random item in a random quantity. Can you survive and beat the game when dirt drops diamonds and obsidian drops rotten flesh?

## Features

- **Total randomization** — every block type is mapped to a unique random item at challenge start.
- **Weighted drop amounts** — uses a power-law curve to make large stacks rare and exciting.
- **Live timer** — elapsed time displayed in the action bar above the hotbar.
- **Epic start effects** — titles and sounds for all online players when the challenge begins.
- **Shuffle** — re-roll all drop mappings mid-game without stopping the timer.
- **Smart filtering** — air and non-obtainable items are excluded automatically.

## Requirements

- Paper `26.1.2` or newer
- Java 25+
- **[ChallengesCore](../ChallengesCore/README.md)** installed on the server

## Installation

1. Install `ChallengesCore.jar` first.
2. Place `RandomDropChallenge.jar` into your server's `plugins/` folder.
3. Restart the server.

## Commands

All commands require operator permissions.

| Command                                  | Description                                      |
|------------------------------------------|--------------------------------------------------|
| `/challenge randomdropchallenge start`   | Starts the challenge and generates drop mappings |
| `/challenge randomdropchallenge stop`    | Stops the challenge and clears all mappings      |
| `/challenge randomdropchallenge shuffle` | Re-rolls all block-to-item assignments           |

## Building from Source

ChallengesCore must be installed to the local Maven repository first:

```bash
cd ../ChallengesCore
mvn install

cd ../RandomDropChallenge
mvn clean package
```

The compiled JAR is in `target/`.
