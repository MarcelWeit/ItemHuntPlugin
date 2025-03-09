package weitma.itemHuntPlugin.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import weitma.itemHuntPlugin.ItemHuntPlugin;
import weitma.itemHuntPlugin.Utils.TeamManager;

public class resetChallengeCommand implements CommandExecutor {

    private final ItemHuntPlugin plugin;

    public resetChallengeCommand(ItemHuntPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        TeamManager.getInstance().resetTeams();
        plugin.resetChallenge();
        return true;
    }
}
