package me.psydo.randomDropChallenge;

import me.psydo.challengesCore.ChallengesCore;
import me.psydo.challengesCore.IChallenge;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NonNull;

import java.util.*;

public final class RandomDropChallenge extends JavaPlugin implements Listener, IChallenge {
    private HashMap<Material, Material> drops = new HashMap<>();
    private boolean isRunning = false;

    public enum ChallengeAction {
        START, STOP, SHUFFLE
    }

    private final Random random = new Random();
    private int secondsPassed = 0;

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("RandomDropChallenge has been enabled!");

        if (!(Bukkit.getPluginManager().getPlugin("ChallengesCore") instanceof ChallengesCore core)) {
            getLogger().severe("ChallengesCore nicht gefunden! Plugin wird deaktiviert.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        core.getChallengeRegistry().registerChallenge(this);

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
        }, 0L, 20L);
    }

    @Override
    public void onDisable() {
        if (drops != null) {
            drops.clear();
        }
        getLogger().info("RandomDropChallenge has been disabled!");
        secondsPassed = 0;
    }

    @Override
    public String getChallengeId() {
        return "randomdropchallenge";
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(Component.text("Benutzung: /challenge randomdropchallenge <start|stop|shuffle>", NamedTextColor.RED));
            return true;
        }

        ChallengeAction action = mapChallengeAction(args[0]);

        if (action == null) {
            sender.sendMessage(Component.text("Unbekannte Aktion: " + args[0] + " | Erlaubt: start, stop, shuffle", NamedTextColor.RED));
            return true;
        }

        if (action == ChallengeAction.SHUFFLE) {
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

        if (action == ChallengeAction.STOP) {
            if (isRunning) {
                isRunning = false;
                drops.clear();
                Bukkit.broadcast(Component.text("Die Random Drop Challenge wurde GESTOPPT!", NamedTextColor.RED, TextDecoration.BOLD));
            }
        }

        return true;
    }

    @Override
    public @NonNull List<String> onTabComplete(String[] args) {
        if (args.length == 1) {
            return Arrays.asList("start", "stop", "shuffle");
        }
        return new ArrayList<>();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!isRunning) return;

        event.setDropItems(false);

        Material itemToDrop = drops.get(event.getBlock().getType());
        if (itemToDrop == null) return;

        int maxStack = itemToDrop.getMaxStackSize();

        double r = random.nextDouble();
        int amount = (int) (Math.pow(r, 64) * maxStack) + 1;

        if (amount <= 0) {
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

    public ChallengeAction mapChallengeAction(String action) {
        return switch (action.toUpperCase()) {
            case "START" -> ChallengeAction.START;
            case "STOP" -> ChallengeAction.STOP;
            case "SHUFFLE" -> ChallengeAction.SHUFFLE;
            default -> null;
        };
    }
}
