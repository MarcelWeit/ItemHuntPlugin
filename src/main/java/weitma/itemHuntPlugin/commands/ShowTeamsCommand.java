package weitma.itemHuntPlugin.commands;

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
import weitma.itemHuntPlugin.Utils.TeamManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class ShowTeamsCommand implements CommandExecutor {

    private static Inventory teamInventory;
    private final ItemHuntPlugin plugin;

    public ShowTeamsCommand(ItemHuntPlugin plugin) {
        this.plugin = plugin;

        teamInventory = Bukkit.createInventory(null, 9, ChatColor.BLACK + "Teams");
        updateInventory();
    }

    public static Inventory getTeamInventory() {
        return teamInventory;
    }

    public static void updateInventory() {
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

        for (int i = 0; i < TeamManager.availableTeams.length; i++) {
            ItemMeta meta = teamItems[i].getItemMeta();
            meta.setDisplayName(TeamManager.availableTeams[i]);

            List<String> lore = new ArrayList<>();
            lore.add("Click to join Team " + (i + 1));
            lore.add(ChatColor.WHITE + "Players:");

            HashSet<UUID> playersInTeam = TeamManager.getInstance().getPlayersInTeam(TeamManager.getInstance().getTeam(i));
            for (UUID playerId : playersInTeam) {
                Player teamPlayer = Bukkit.getPlayer(playerId);
                if (teamPlayer != null) {
                    lore.add(ChatColor.GRAY + teamPlayer.getName() + "\n");
                }
            }

            meta.setLore(lore);
            teamItems[i].setItemMeta(meta);
            teamInventory.setItem(i, teamItems[i]);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player player) {

            if (plugin.isChallengeStarted()) {
                player.sendMessage(ChatColor.RED + "You can't change teams while the challenge is running!");
                return false;
            }

            updateInventory();

            player.openInventory(teamInventory);
        }
        return true;
    }

}
