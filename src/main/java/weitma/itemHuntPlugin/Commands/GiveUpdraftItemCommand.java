package weitma.itemHuntPlugin.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import weitma.itemHuntPlugin.ItemHuntPlugin;

public class GiveUpdraftItemCommand implements CommandExecutor {

    private final ItemHuntPlugin plugin;

    public GiveUpdraftItemCommand(ItemHuntPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player player){
            if(args.length != 2){
                player.sendMessage("Usage: /giveupdraftitem <player> <amount>");
                return false;
            }

            Player target = plugin.getServer().getPlayer(args[0]);
            if(target == null){
                player.sendMessage("Player not found!");
                return false;
            }

            target.getInventory().addItem(plugin.getUpdraftItem(Integer.parseInt(args[1])));
        }

        return true;
    }
}
