package weitma.itemHuntPlugin.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import weitma.itemHuntPlugin.ItemHuntPlugin;

public class GiveSpecialRocketCommand implements CommandExecutor {

    private final ItemHuntPlugin plugin;

    public GiveSpecialRocketCommand(ItemHuntPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player player){

            if(args.length != 1){
                sender.sendMessage("Usage: /givespecialrocket <player>");
                return false;
            }

            String playerName = args[0];

            Player playerToSendTo = Bukkit.getPlayer(playerName);

            playerToSendTo.getInventory().addItem(plugin.getSpecialRocket());

            plugin.getSpecialRocket();

        }

        return true;
    }
}
