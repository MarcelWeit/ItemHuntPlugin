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
import weitma.itemHuntPlugin.ItemHuntPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShowTeamsCommand implements CommandExecutor {

    private final ItemHuntPlugin plugin;

    public ShowTeamsCommand(ItemHuntPlugin plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player player){
            Inventory inventory = Bukkit.createInventory(player, 9, ChatColor.BLACK + "Teams");

            ItemStack[] teamItems = {
                    new ItemStack(Material.RED_WOOL),
                    new ItemStack(Material.BLUE_WOOL),
                    new ItemStack(Material.GREEN_WOOL),
                    new ItemStack(Material.YELLOW_WOOL),
                    new ItemStack(Material.BLACK_WOOL),
                    new ItemStack(Material.PURPLE_WOOL),
                    new ItemStack(Material.ORANGE_WOOL),
                    new ItemStack(Material.PINK_WOOL),
                    new ItemStack(Material.WHITE_WOOL)
            };

            for (int i = 0; i < teamItems.length; i++) {
                ItemMeta meta = teamItems[i].getItemMeta();
                meta.setDisplayName(ItemHuntPlugin.teamNames[i]);

                List<String> lore = new ArrayList<>();
                lore.add("Click to join Team " + (i + 1));
                lore.add(ChatColor.WHITE + "Players:");

                ArrayList<UUID> playersInTeam = plugin.getPlayersInTeam(i + 1);
                for (UUID playerId : playersInTeam) {
                    Player teamPlayer = Bukkit.getPlayer(playerId);
                    if (teamPlayer != null) {
                        lore.add(ChatColor.GRAY + teamPlayer.getName() + "\n");
                    }
                }

                meta.setLore(lore);
                teamItems[i].setItemMeta(meta);
                inventory.setItem(i, teamItems[i]);
            }

            player.openInventory(inventory);
        }
        return true;
    }

}
