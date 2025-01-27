package weitma.itemHuntPlugin.Commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ShowTeamsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player player){
            Inventory inventory = Bukkit.createInventory(player, 9, ChatColor.BLACK + "Teams");

            ItemStack teamItem1 = new ItemStack(Material.RED_WOOL);
            ItemStack teamItem2 = new ItemStack(Material.BLUE_WOOL);
            ItemStack teamItem3 = new ItemStack(Material.GREEN_WOOL);
            ItemStack teamItem4 = new ItemStack(Material.YELLOW_WOOL);

            ItemMeta teamItem1Meta = teamItem1.getItemMeta();
            ItemMeta teamItem2Meta = teamItem2.getItemMeta();
            ItemMeta teamItem3Meta = teamItem3.getItemMeta();
            ItemMeta teamItem4Meta = teamItem4.getItemMeta();

            teamItem1Meta.setDisplayName(ChatColor.RED + "Team Red");
            teamItem2Meta.setDisplayName(ChatColor.BLUE + "Team Blue");
            teamItem3Meta.setDisplayName(ChatColor.GREEN + "Team Green");
            teamItem4Meta.setDisplayName(ChatColor.YELLOW + "Team Yellow");

            teamItem1Meta.setLore(List.of("Click to join Team 1", ChatColor.WHITE + "Players:"));
            teamItem2Meta.setLore(List.of("Click to join Team 2",ChatColor.WHITE + "Players:"));
            teamItem3Meta.setLore(List.of("Click to join Team 3",ChatColor.WHITE + "Players:"));
            teamItem4Meta.setLore(List.of("Click to join Team 4",ChatColor.WHITE + "Players:"));

            teamItem1.setItemMeta(teamItem1Meta);
            teamItem2.setItemMeta(teamItem2Meta);
            teamItem3.setItemMeta(teamItem3Meta);
            teamItem4.setItemMeta(teamItem4Meta);

            inventory.setItem(0, teamItem1);
            inventory.setItem(1, teamItem2);
            inventory.setItem(2, teamItem3);
            inventory.setItem(3, teamItem4);

            player.openInventory(inventory);
        }
        return true;
    }

}
