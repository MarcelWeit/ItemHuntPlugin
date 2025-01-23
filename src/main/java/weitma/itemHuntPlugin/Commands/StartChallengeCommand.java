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
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import weitma.itemHuntPlugin.ItemHuntPlugin;

public class StartChallengeCommand implements CommandExecutor {

    private final ItemHuntPlugin plugin;

    private int taskID;

    public StartChallengeCommand(ItemHuntPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        plugin.setChallengeStarted(true);

        Bukkit.getOnlinePlayers().forEach(player -> {
            plugin.generateNewRandomMaterialToCollect(player.getUniqueId());
        });

        int seconds;

        if(args.length != 0){
            seconds = Integer.parseInt(args[0])*60;
        } else {
            seconds = 120 * 60;
        }

        plugin.startCountdown(seconds);

        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage(ChatColor.GREEN + "The challenge has started! You have "+ seconds + " seconds to collect the items!");
            plugin.showItemToCollect(player.getUniqueId());
            player.getInventory().addItem(plugin.getSkipItem(5));
        });

        return true;
    }

    public int getTaskID() {
        return taskID;
    }
}
