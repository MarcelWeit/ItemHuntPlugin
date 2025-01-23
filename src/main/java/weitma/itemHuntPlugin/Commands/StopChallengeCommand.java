package weitma.itemHuntPlugin.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import weitma.itemHuntPlugin.ItemHuntPlugin;

public class StopChallengeCommand implements CommandExecutor {

    private final ItemHuntPlugin plugin;

    public StopChallengeCommand(ItemHuntPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        plugin.stopChallenge();

        return true;
    }
}
