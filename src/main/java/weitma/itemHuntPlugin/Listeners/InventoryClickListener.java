package weitma.itemHuntPlugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import weitma.itemHuntPlugin.ItemHuntPlugin;
import weitma.itemHuntPlugin.Utils.Team;
import weitma.itemHuntPlugin.Utils.TeamManager;
import weitma.itemHuntPlugin.commands.ShowCollectedItemsCommand;
import weitma.itemHuntPlugin.commands.ShowTeamsCommand;

public class InventoryClickListener implements Listener {

    private final ItemHuntPlugin plugin;
    private ShowCollectedItemsCommand showCollectedItemsCommand;

    public InventoryClickListener(ItemHuntPlugin plugin, ShowCollectedItemsCommand command) {
        this.plugin = plugin;
        this.showCollectedItemsCommand = command;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        Inventory inventory = event.getClickedInventory();

        Player player = (Player) event.getWhoClicked();

        ItemStack currentItem = event.getCurrentItem();

        if (currentItem == null) {
            return;
        }

        if (ChatColor.stripColor(event.getView().getTitle()).equals("Teams")) {

            event.setCancelled(true);

            if (event.getClick().isRightClick()) return;

            Team team = TeamManager.getInstance().getTeamOfPlayer(player.getUniqueId());
            if (team != null) {
                team.removePlayer(player.getUniqueId());
            }

            switch (currentItem.getType()) {
                case RED_WOOL:
                    plugin.setPlayerListName(ChatColor.RED + "[Red] " + ChatColor.WHITE + player.getName(), player.getUniqueId());
                    TeamManager.getInstance().joinTeam(player.getUniqueId(), TeamManager.getInstance().getTeam(0));
                    break;
                case BLUE_WOOL:
                    plugin.setPlayerListName(ChatColor.BLUE + "[Blue] " + ChatColor.WHITE + player.getName(), player.getUniqueId());
                    TeamManager.getInstance().joinTeam(player.getUniqueId(), TeamManager.getInstance().getTeam(1));
                    break;
                case GREEN_WOOL:
                    plugin.setPlayerListName(ChatColor.GREEN + "[Green] " + ChatColor.WHITE + player.getName(), player.getUniqueId());
                    TeamManager.getInstance().joinTeam(player.getUniqueId(), TeamManager.getInstance().getTeam(2));
                    break;
                case YELLOW_WOOL:
                    plugin.setPlayerListName(ChatColor.YELLOW + "[Yellow] " + ChatColor.WHITE + player.getName(), player.getUniqueId());
                    TeamManager.getInstance().joinTeam(player.getUniqueId(), TeamManager.getInstance().getTeam(3));
                    break;
                case BLACK_WOOL:
                    plugin.setPlayerListName(ChatColor.BLACK + "[Black] " + ChatColor.WHITE + player.getName(), player.getUniqueId());
                    TeamManager.getInstance().joinTeam(player.getUniqueId(), TeamManager.getInstance().getTeam(4));
                    break;
                case PURPLE_WOOL:
                    plugin.setPlayerListName(ChatColor.DARK_PURPLE + "[Purple] " + ChatColor.WHITE + player.getName(), player.getUniqueId());
                    TeamManager.getInstance().joinTeam(player.getUniqueId(), TeamManager.getInstance().getTeam(5));
                    break;
                case ORANGE_WOOL:
                    plugin.setPlayerListName(ChatColor.GOLD + "[Orange] " + ChatColor.WHITE + player.getName(), player.getUniqueId());
                    TeamManager.getInstance().joinTeam(player.getUniqueId(), TeamManager.getInstance().getTeam(6));
                    break;
                case PINK_WOOL:
                    plugin.setPlayerListName(ChatColor.LIGHT_PURPLE + "[Pink] " + ChatColor.WHITE + player.getName(), player.getUniqueId());
                    TeamManager.getInstance().joinTeam(player.getUniqueId(), TeamManager.getInstance().getTeam(7));
                    break;
                case WHITE_WOOL:
                    plugin.setPlayerListName(ChatColor.WHITE + "[White] " + ChatColor.WHITE + player.getName(), player.getUniqueId());
                    TeamManager.getInstance().joinTeam(player.getUniqueId(), TeamManager.getInstance().getTeam(8));
                    break;
                default:
                    player.sendMessage("You didn't join any team!");
                    break;
            }
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                onlinePlayer.sendMessage(player.getName() + " joined " + TeamManager.getInstance().getTeamOfPlayer(player.getUniqueId()).getTeamName());
            }
            player.closeInventory();

        } else if (ChatColor.stripColor(event.getView().getTitle()).startsWith("Items collected")) {
            event.setCancelled(true);
            Team team = TeamManager.getInstance().getTeamOfPlayer(player.getUniqueId());
            int currentIndex = showCollectedItemsCommand.getInvIndexOfPlayer(player);

            if (currentItem.getType().equals(Material.STONE_BUTTON)) { //left
                currentIndex -= 1;
            }

            if (currentItem.getType().equals(Material.OAK_BUTTON)) { //right
                currentIndex += 1;
            }
            String command = "showcollecteditems" + " " + TeamManager.getInstance().getTeamIndex(team.getTeamName()) + " " + player.getUniqueId() + " " + currentIndex;
            Bukkit.getLogger().info(command);
            Bukkit.dispatchCommand(player, command);

        } else if (ChatColor.stripColor(event.getView().getTitle()).startsWith("Backpack")) {
            if (currentItem.getItemMeta() != null && currentItem.getItemMeta().hasCustomModelData() && currentItem.getItemMeta().getCustomModelData() == ItemHuntPlugin.BACKPACK_ID) {
                event.getWhoClicked().sendMessage(ChatColor.RED + "Cant put Backpack in itself!");
                event.setCancelled(true);
            }
        } else if (inventory != null && inventory.equals(ShowTeamsCommand.getTeamInventory())) {
            ShowTeamsCommand.updateInventory();
        }
    }

}
