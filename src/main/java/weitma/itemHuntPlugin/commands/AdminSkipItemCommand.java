package weitma.itemHuntPlugin.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import weitma.itemHuntPlugin.ItemHuntPlugin;
import weitma.itemHuntPlugin.Utils.TeamManager;

public class AdminSkipItemCommand implements CommandExecutor {

    private final ItemHuntPlugin plugin;

    public AdminSkipItemCommand(ItemHuntPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player player) {
            if (args.length != 1) {
                player.sendMessage("Usage: /skipitem <player>");
                return false;
            }

            Player target = plugin.getServer().getPlayer(args[0]);
            if (target == null) {
                player.sendMessage("Player not found!");
                return false;
            }

            Material itemThatShouldveBeenCollected = plugin.getItemToCollect(TeamManager.getInstance().getTeamOfPlayer(target.getUniqueId()));
            plugin.successfullPickup(target, itemThatShouldveBeenCollected, false, true);

        }

        return true;
    }
}
