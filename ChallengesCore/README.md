# ChallengesCore

The central plugin that powers all challenges. It owns the `/challenge` command and routes it to the correct challenge plugin based on the first argument.

## How It Works

1. A player types `/challenge randomdropchallenge start`.
2. ChallengesCore receives the command and looks up `randomdropchallenge` in its registry.
3. It strips the challenge name from the arguments and calls `onCommand(sender, ["start"])` on the registered plugin.
4. Tab-completion works the same way — the first argument shows all registered challenge names, subsequent arguments are delegated to each challenge.

## Commands

| Command                      | Description                               |
|------------------------------|-------------------------------------------|
| `/challenge <name> <action>` | Runs an action on the specified challenge |

## Installation

Place `ChallengesCore.jar` in your server's `plugins/` folder. Without it, no challenge plugin will load.

## Building

```bash
mvn install
```

Use `install` (not `package`) so other plugins can reference ChallengesCore as a Maven dependency.

---

## Creating a New Challenge Plugin

### 1. Add ChallengesCore as a dependency in `pom.xml`

```xml
<dependency>
    <groupId>me.psydo</groupId>
    <artifactId>challengescore</artifactId>
    <version>1.0-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
```

The scope is `provided` — ChallengesCore is loaded by the server at runtime, not bundled into your JAR.

### 2. Declare the dependency in `plugin.yml`

```yaml
depend: [ChallengesCore]
```

This ensures ChallengesCore loads before your plugin.

### 3. Implement the `IChallenge` interface

```java
public final class MyChallenge extends JavaPlugin implements IChallenge {

    @Override
    public String getChallengeId() {
        return "mychallenge"; // what players type in /challenge <this>
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        // args[0] is the action (start, stop, etc.)
        // the challenge name has already been removed by ChallengesCore
        return true;
    }

    @Override
    public @NotNull List<String> onTabComplete(String[] args) {
        if (args.length == 1) return Arrays.asList("start", "stop");
        return new ArrayList<>();
    }
}
```

### 4. Register with ChallengesCore in `onEnable`

```java
@Override
public void onEnable() {
    if (!(Bukkit.getPluginManager().getPlugin("ChallengesCore") instanceof ChallengesCore core)) {
        getLogger().severe("ChallengesCore not found! Disabling plugin.");
        getServer().getPluginManager().disablePlugin(this);
        return;
    }
    core.getChallengeRegistry().registerChallenge(this);
}
```

That's all — no command registration needed in your plugin.
