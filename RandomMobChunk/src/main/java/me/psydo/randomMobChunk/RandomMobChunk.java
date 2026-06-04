package me.psydo.randomMobChunk;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public final class RandomMobChunk extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("RandomMobChunk has been enabled!");
        Objects.requireNonNull(getCommand("challenge")).setExecutor(this);
        Objects.requireNonNull(getCommand("challenge")).setTabCompleter(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender sender, Command command, @NonNull String label, String @NonNull [] args) {
        if (command.getName().equalsIgnoreCase("challenge")){
            if (args.length == 1){
                List<String> subCommands = new ArrayList<>();
                subCommands.add("randommobchunk");
                return subCommands;
            }

            if (args.length == 2 && args[0].equalsIgnoreCase("randommobchunk")) {
                return Arrays.asList("start", "stop");
            }
        }

        return new ArrayList<>();
    }
}
