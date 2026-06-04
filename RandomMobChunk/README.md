# RandomMobChunk

A Minecraft challenge plugin — currently in development.

## Requirements

- Paper `26.1.2` or newer
- Java 25+
- **[ChallengesCore](../ChallengesCore/README.md)** installed on the server

## Installation

1. Install `ChallengesCore.jar` first.
2. Place `RandomMobChunk.jar` into your server's `plugins/` folder.
3. Restart the server.

## Commands

| Command                           | Description          |
|-----------------------------------|----------------------|
| `/challenge randommobchunk start` | Starts the challenge |
| `/challenge randommobchunk stop`  | Stops the challenge  |

## Building from Source

ChallengesCore must be installed to the local Maven repository first:

```bash
cd ../ChallengesCore
mvn install

cd ../RandomMobChunk
mvn clean package
```

The compiled JAR is in `target/`.
