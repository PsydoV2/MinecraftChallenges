package me.psydo.randomMobChunk;

import me.psydo.challengesCore.ChallengesCore;
import me.psydo.challengesCore.IChallenge;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
    public enum ChallengeAction {
        START, STOP
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("RandomMobChunk has been enabled!");

        if (!(Bukkit.getPluginManager().getPlugin("ChallengesCore") instanceof ChallengesCore core)) {
            getLogger().severe("ChallengesCore not found! Plugin gets deactivated!.");
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
            sender.sendMessage(Component.text("Usage: /challenge randommobchunk <start|stop>", NamedTextColor.RED));
            return true;
        }

//        ChallengeAction action = ChallengeAction.valueOf(args[0].toUpperCase());
        ChallengeAction action = mapChallengeAction(args[0]);

        if (action == null) {
            sender.sendMessage(Component.text("Unknow Action: " + args[0] + " | Allowed: start, stop, shuffle", NamedTextColor.RED));
            return true;
        }

        if (action == ChallengeAction.START) {
            if (isRunning) {
                sender.sendMessage(Component.text("The challenge is already underway!", NamedTextColor.GOLD));
            } else {
                isRunning = true;

                for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
                    player.showTitle(net.kyori.adventure.title.Title.title(
                            Component.text("CHALLENGE START", NamedTextColor.GREEN, TextDecoration.BOLD),
                            Component.text("Good luck!", NamedTextColor.GRAY)
                    ));
                    player.playSound(player.getLocation(), org.bukkit.Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
                }
            }
        }

        if (action == ChallengeAction.STOP) {
            if (isRunning) {
                isRunning = false;
                Bukkit.broadcast(Component.text("The Random Mob Chunk Challenge has been STOPPED!", NamedTextColor.RED, TextDecoration.BOLD));
            }
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

    public ChallengeAction mapChallengeAction(String action) {
        return switch (action.toLowerCase()) {
            case "start" -> ChallengeAction.START;
            case "stop" -> ChallengeAction.STOP;
            default -> null;
        };
    }
}
