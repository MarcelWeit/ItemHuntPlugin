package weitma.itemHuntPlugin.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import weitma.itemHuntPlugin.Utils.TeamManager;

public class resetTeamsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        TeamManager.getInstance().resetTeams();

        return true;
    }
}
