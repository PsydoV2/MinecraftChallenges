# MinecraftChallenges

A collection of Minecraft challenge plugins for Paper servers. All challenges share a single unified command interface provided by the **ChallengesCore** plugin.

## Architecture

```
ChallengesCore          ← owns /challenge, routes to the right plugin
├── RandomDropChallenge ← every block drops a random item
└── RandomMobChunk      ← (in development)
```

Each challenge is a separate plugin JAR. ChallengesCore must be installed for any challenge to work.

## Command Structure

All challenges are controlled through one command:

```
/challenge <challenge-name> <action>
```

Tab-completion is supported at every step.

## Available Challenges

| Plugin              | Command Name          | Actions                    |
|---------------------|-----------------------|----------------------------|
| RandomDropChallenge | `randomdropchallenge` | `start`, `stop`, `shuffle` |
| RandomMobChunk      | `randommobchunk`      | `start`, `stop`            |

## Installation

1. Build or download all plugin JARs.
2. Place **all desired JARs** into your server's `plugins/` folder — ChallengesCore is always required.
3. Restart the server.

## Building from Source

ChallengesCore must be installed to the local Maven repository before the challenge plugins can be compiled.

```bash
cd ChallengesCore
mvn install

cd ../RandomDropChallenge
mvn clean package

cd ../RandomMobChunk
mvn clean package
```

The compiled JARs are located in each project's `target/` folder.

## Requirements

- Paper `26.1.2` or newer
- Java 25+

## Adding a New Challenge

See [ChallengesCore/README.md](ChallengesCore/README.md) for a guide on creating your own challenge plugin.
