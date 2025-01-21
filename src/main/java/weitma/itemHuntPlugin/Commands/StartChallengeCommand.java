package weitma.itemHuntPlugin.Commands;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Boss;
import org.bukkit.inventory.ItemStack;
import weitma.itemHuntPlugin.ItemHuntPlugin;

public class StartChallengeCommand implements CommandExecutor {

    private ItemHuntPlugin plugin;

    public StartChallengeCommand(ItemHuntPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        plugin.setChallengeStarted(true);

        startCountdown(10);

        showItemsToCollect();

        return true;
    }

    public void startCountdown(int seconds) {
        // Broadcast the time left to all players
        int taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            int timeLeft = seconds;

            boolean playedSound = false;

            @Override
            public void run() {
                if (timeLeft > 0) {
                    StringBuilder message = new StringBuilder();
                    message.append(ChatColor.GREEN).append(ChatColor.BOLD);

                    int hoursRemaining = timeLeft / 3600;
                    int minutesRemaining = (timeLeft % 3600) / 60;
                    int secondsRemaining = timeLeft % 60;

                    // Broadcast the time left to all players
                    if (hoursRemaining > 0) {
                        if (hoursRemaining < 10) {
                            message.append("0");
                        }
                        message.append(hoursRemaining);
                    } else {
                        message.append("00");
                    }
                    message.append(":");

                    if (minutesRemaining > 0) {
                        if (minutesRemaining < 10) {
                            message.append("0");
                        }
                        message.append(minutesRemaining);
                    } else {
                        message.append("00");
                    }
                    message.append(":");

                    if (secondsRemaining > 0) {
                        if (secondsRemaining < 10) {
                            message.append("0");
                        }
                        message.append(secondsRemaining);
                    } else {
                        message.append("00");
                    }

                    Bukkit.getWorlds().forEach(world -> world.getPlayers().forEach(player ->
                            player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, new TextComponent(message.toString()))
                    ));

                    timeLeft--;

                } else {
                    Bukkit.getWorlds().forEach(world -> world.getPlayers().forEach(player -> {
                        player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED + "" + ChatColor.BOLD + "00:00" +
                                ":00"));
                        if(!playedSound){
                            player.playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1, 1);
                        }
                    }));
                    playedSound = true;

                }
            }
        }, 0L, 20L); // 20 ticks = 1 second
    }

    private void showItemsToCollect() {
        Bukkit.getBossBars().forEachRemaining(bossBar -> bossBar.removeAll());

        BossBar bossbar = Bukkit.createBossBar(Material.ACACIA_BOAT.toString(), BarColor.WHITE, BarStyle.SOLID);
        bossbar.setProgress(0.0);

        bossbar.addPlayer(Bukkit.getPlayer("weitii"));

    }
}
