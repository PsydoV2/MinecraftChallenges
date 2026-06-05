package me.psydo.challengesCore;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class ChallengesCore extends JavaPlugin {

    private ChallengeRegistry challengeRegistry;

    @Override
    public void onEnable() {
        challengeRegistry = new ChallengeRegistry();
        Objects.requireNonNull(getCommand("challenge")).setExecutor(this);
        Objects.requireNonNull(getCommand("challenge")).setTabCompleter(this);

        getLogger().info("ChallengesCore has been enabled!");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ChallengeRegistry getChallengeRegistry() {
        return challengeRegistry;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {
        // /challenge <name> <action>  →  args[0]=name, args[1]=action

        if (args.length < 2) {
            sender.sendMessage("§cBenutzung: /challenge <name> <aktion>");
            return true;
        }

        IChallenge challenge = challengeRegistry.getChallenge(args[0]);

        if (challenge == null) {
            sender.sendMessage("§cUnbekannte Challenge: " + args[0]);
            return true;
        }

        // args ohne den Challenge-Namen weitergeben: ["start"] statt ["randommobchunk","start"]
        String[] challengeArgs = Arrays.copyOfRange(args, 1, args.length);
        return challenge.onCommand(sender, challengeArgs);
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, @NonNull String [] args) {
        if (args.length == 1) {
            // Ersten Arg: alle bekannten Challenge-Namen vorschlagen
            return new ArrayList<>(challengeRegistry.getChallengeNames());
        }

        if (args.length >= 2) {
            // Ab dem zweiten Arg: an die Challenge delegieren
            IChallenge challenge = challengeRegistry.getChallenge(args[0]);
            if (challenge != null) {
                String[] challengeArgs = Arrays.copyOfRange(args, 1, args.length);
                return challenge.onTabComplete(challengeArgs);
            }
        }

        return new ArrayList<>();
    }
}
