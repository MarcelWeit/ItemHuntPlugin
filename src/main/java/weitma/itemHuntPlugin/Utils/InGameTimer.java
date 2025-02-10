package weitma.itemHuntPlugin.Utils;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import weitma.itemHuntPlugin.ItemHuntPlugin;

public class InGameTimer {

    private String currentTimer;
    private int taskIDStart;
    private int taskIDPause;
    private int secondsLeft;

    private ItemHuntPlugin plugin;

    public InGameTimer(ItemHuntPlugin plugin) {
        this.plugin = plugin;
    }

    public void startTimer(int seconds) {
        taskIDStart = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
            int timeLeft = seconds;

            @Override
            public void run() {
                if (timeLeft > 0) {
                    secondsLeft = timeLeft;
                    StringBuilder message = new StringBuilder();
                    message.append(ChatColor.GOLD).append(ChatColor.BOLD);

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

                    currentTimer = message.toString();

                    Bukkit.getWorlds().forEach(world -> world.getPlayers().forEach(player ->
                            player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, new TextComponent(message.toString()))
                    ));

                    timeLeft--;

                } else {
                    plugin.onTimeExpired();
                    Bukkit.getScheduler().cancelTask(taskIDStart);
                    secondsLeft = 0;
                }
            }
        }, 0L, 20L); // 20 ticks = 1 second
    }

    public void pauseTimer() {
        if(secondsLeft > 0) {
            Bukkit.getScheduler().cancelTask(taskIDStart);
            taskIDPause = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
                @Override
                public void run() {
                    Bukkit.getWorlds().forEach(world -> world.getPlayers().forEach(player ->
                            player.spigot().sendMessage(net.md_5.bungee.api.ChatMessageType.ACTION_BAR, new TextComponent(currentTimer))
                    ));
                }
            }, 0L, 20L);
        }
    }

    public void stopTimer() {
        Bukkit.getScheduler().cancelTask(taskIDStart);
    }

    public void resumeTimer() {
        if(secondsLeft > 0) {
            Bukkit.getScheduler().cancelTask(taskIDPause);
            startTimer(secondsLeft);
        }
    }

    public String getCurrentTimer() {
        return currentTimer;
    }
}
