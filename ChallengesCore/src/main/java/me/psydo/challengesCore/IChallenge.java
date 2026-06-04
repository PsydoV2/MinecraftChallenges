package me.psydo.challengesCore;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public interface IChallenge {
    String getChallengeId();
    boolean onCommand(CommandSender sender, String[] args);
    @NotNull List<String> onTabComplete(String[] args);
}
