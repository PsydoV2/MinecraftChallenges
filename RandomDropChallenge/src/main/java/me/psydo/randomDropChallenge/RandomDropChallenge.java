package me.psydo.randomDropChallenge;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;

import java.util.*;

public final class RandomDropChallenge extends JavaPlugin implements Listener {
    private HashMap<Material, Material> drops = new HashMap<>();
    private boolean isRunning = false;
    public enum ChallengeAction {
        START,
        STOP,
        SHUFFLE
    }
    private final Random random = new Random();
    private int secondsPassed = 0;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("RandomDropChallenge has been enabled!");
        Objects.requireNonNull(getCommand("challenge")).setExecutor(this);
        Objects.requireNonNull(getCommand("challenge")).setTabCompleter(this);

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            if (!isRunning) {
                secondsPassed = 0;
                return;
            }

            secondsPassed++;

            int minutes = secondsPassed / 60;
            int seconds = secondsPassed % 60;
            String timeString = String.format("%02d:%02d", minutes, seconds);

            Component actionBarText = Component.text("Zeit: ", NamedTextColor.GRAY)
                    .append(Component.text(timeString, NamedTextColor.GOLD, TextDecoration.BOLD))
                    .append(Component.text(" | Challenge aktiv", NamedTextColor.GREEN));

            for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
                player.sendActionBar(actionBarText);
            }
        }, 0L, 20L); // 20 Ticks = 1 Sekunde
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        if (drops != null){
            drops.clear();
        }
        getLogger().info("RandomDropChallenge has been disabled!");
        secondsPassed = 0;
    }

    @Override
    public boolean onCommand(@NonNull CommandSender sender, @NonNull Command command, @NonNull String label, String @NonNull [] args) {
        if (args.length < 2) {
            sender.sendMessage(Component.text("Benutzung: /challenge <name> <start|stop|shuffle>", NamedTextColor.RED));
            return true;
        }

        String challengeNameInput = args[0];
        ChallengeAction action = mapChallengeAction(args[1]);

        if (command.getName().equalsIgnoreCase("challenge") && Objects.equals(challengeNameInput.toLowerCase(), "randomdropchallenge")){
            if (action == ChallengeAction.SHUFFLE){
                drops.clear();
                drops = generateDrops();
                sender.sendMessage(Component.text("Die Drops wurden neu gemischt!", NamedTextColor.GREEN, TextDecoration.BOLD));
            }

            if (action == ChallengeAction.START) {
                if (isRunning) {
                    sender.sendMessage(Component.text("Die Challenge läuft bereits!", NamedTextColor.GOLD));
                } else {
                    isRunning = true;
                    drops = generateDrops();

                    for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
                        player.showTitle(net.kyori.adventure.title.Title.title(
                                Component.text("CHALLENGE START", NamedTextColor.GREEN, TextDecoration.BOLD),
                                Component.text("Viel Erfolg!", NamedTextColor.GRAY)
                        ));

                        player.playSound(player.getLocation(), org.bukkit.Sound.UI_TOAST_CHALLENGE_COMPLETE, 1f, 1f);
                    }
                }
            }

            if (action == ChallengeAction.STOP){
                if (isRunning){
                    isRunning = false;
                    drops.clear();
                    Bukkit.broadcast(Component.text("Die Random Drop Challenge wurde GESTOPPT!", NamedTextColor.RED, TextDecoration.BOLD));
                }
            }

            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(@NonNull CommandSender sender, Command command, @NonNull String label, String @NonNull [] args) {
        if (command.getName().equalsIgnoreCase("challenge")){
            if (args.length == 1){
                List<String> subCommands = new ArrayList<>();
                subCommands.add("randomdropchallenge");
                return subCommands;
            }

            if (args.length == 2 && args[0].equalsIgnoreCase("randomdropchallenge")) {
                return Arrays.asList("start", "stop", "shuffle");
            }
        }

        return new ArrayList<>();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!isRunning) return;

        event.setDropItems(false);

        Material itemToDrop = drops.get(event.getBlock().getType());

        int maxStack = itemToDrop.getMaxStackSize();

        double r = random.nextDouble(); // Zahl zwischen 0.0 und 1.0
        // Durch r * r (Quadrieren) wandern die Werte stark Richtung 0
        int amount = (int) (Math.pow(r, 64) * maxStack) + 1;

        if (amount <= 0){
            amount = 1;
        }

        ItemStack item = ItemStack.of(itemToDrop);
        item.setAmount(amount);

        event.getBlock().getWorld().dropItemNaturally(
                event.getBlock().getLocation(),
                item
        );
    }

    public List<Material> getDroppableMaterials() {
        List<Material> materials = new ArrayList<>(Arrays.asList(Material.values()));
        materials.removeIf(mat -> mat.isAir() || !mat.isItem());
        return materials;
    }

    public HashMap<Material, Material> generateDrops() {
        List<Material> pool = getDroppableMaterials();
        Collections.shuffle(pool);

        HashMap<Material, Material> drops = new HashMap<>();
        Material[] allPossibleBlocks = Material.values();

        int itemIndex = 0;
        for (Material mat : allPossibleBlocks) {
            if (mat.isBlock() && !mat.isAir()) {
                if (itemIndex >= pool.size()) itemIndex = 0;

                drops.put(mat, pool.get(itemIndex));
                itemIndex++;
            }
        }
        return drops;
    }

    public ChallengeAction mapChallengeAction(String action){
        return switch (action.toUpperCase()) {
            case "STOP" -> ChallengeAction.STOP;
            case "SHUFFLE" -> ChallengeAction.SHUFFLE;
            default -> ChallengeAction.START;
        };
    }
}