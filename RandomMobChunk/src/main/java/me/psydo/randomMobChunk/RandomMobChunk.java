package me.psydo.randomMobChunk;

import me.psydo.challengesCore.ChallengesCore;
import me.psydo.challengesCore.IChallenge;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class RandomMobChunk extends JavaPlugin implements Listener, IChallenge {

    private boolean isRunning = false;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("RandomMobChunk has been enabled!");

        if (!(Bukkit.getPluginManager().getPlugin("ChallengesCore") instanceof ChallengesCore core)) {
            getLogger().severe("ChallengesCore nicht gefunden! Plugin wird deaktiviert.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        core.getChallengeRegistry().registerChallenge(this);
    }

    @Override
    public void onDisable() {
        getLogger().info("RandomMobChunk has been disabled!");
    }

    @Override
    public String getChallengeId() {
        return "randommobchunk";
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(Component.text("Benutzung: /challenge randommobchunk <start|stop>", NamedTextColor.RED));
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "start" -> {
                if (isRunning) {
                    sender.sendMessage(Component.text("Die Challenge läuft bereits!", NamedTextColor.GOLD));
                } else {
                    isRunning = true;
                    sender.sendMessage(Component.text("RandomMobChunk gestartet!", NamedTextColor.GREEN));
                    // TODO: Challenge-Logik hier implementieren
                }
            }
            case "stop" -> {
                if (!isRunning) {
                    sender.sendMessage(Component.text("Die Challenge läuft nicht!", NamedTextColor.GOLD));
                } else {
                    isRunning = false;
                    sender.sendMessage(Component.text("RandomMobChunk gestoppt!", NamedTextColor.RED));
                    // TODO: Laufende Logik hier stoppen
                }
            }
            default -> sender.sendMessage(Component.text("Unbekannte Aktion: " + args[0] + " | Erlaubt: start, stop", NamedTextColor.RED));
        }

        return true;
    }

    @Override
    public @NonNull List<String> onTabComplete(String[] args) {
        if (args.length == 1) {
            return Arrays.asList("start", "stop");
        }
        return new ArrayList<>();
    }
}
