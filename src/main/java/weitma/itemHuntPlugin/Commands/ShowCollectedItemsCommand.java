package weitma.itemHuntPlugin.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import weitma.itemHuntPlugin.ItemHuntPlugin;

public class ShowCollectedItemsCommand implements CommandExecutor {

    private final ItemHuntPlugin plugin;

    // @todo: implement this command
    public ShowCollectedItemsCommand(ItemHuntPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {



        return true;
    }
}
