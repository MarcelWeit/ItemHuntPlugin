package weitma.itemHuntPlugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import weitma.itemHuntPlugin.Commands.ShowTeamsCommand;
import weitma.itemHuntPlugin.ItemHuntPlugin;
import weitma.itemHuntPlugin.Utils.Team;
import weitma.itemHuntPlugin.Utils.TeamManager;

public class InventoryClickListener implements Listener {

    private final ItemHuntPlugin plugin;

    public InventoryClickListener(ItemHuntPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        Inventory inventory = event.getClickedInventory();

        if(ChatColor.stripColor(event.getView().getTitle()).equals("Teams")){

            event.setCancelled(true);

            if(event.getClick().isRightClick()) return;

            Player player = (Player) event.getWhoClicked();

            if(event.getCurrentItem() == null){
                Bukkit.getLogger().info("No item clicked");
                return;
            }

            Team team = TeamManager.getInstance().getTeamOfPlayer(player.getUniqueId());
            if(team != null){
                team.removePlayer(player.getUniqueId());
                return;
            }

            switch(event.getCurrentItem().getType()){
                case RED_WOOL:
                    plugin.setPlayerListName(ChatColor.RED + "[Red] " + ChatColor.WHITE + player.getName(), player.getUniqueId());
                    player.sendMessage(ChatColor.RED + "You joined Team Red!");
                    TeamManager.getInstance().joinTeam(player.getUniqueId(), TeamManager.getInstance().getTeam(0));
                    break;
                case BLUE_WOOL:
                    plugin.setPlayerListName(ChatColor.BLUE + "[Blue] " + ChatColor.WHITE + player.getName(), player.getUniqueId());
                    player.sendMessage(ChatColor.BLUE + "You joined Team Blue!");
                    TeamManager.getInstance().joinTeam(player.getUniqueId(), TeamManager.getInstance().getTeam(1));
                    break;
                case GREEN_WOOL:
                    plugin.setPlayerListName(ChatColor.GREEN + "[Green] " + ChatColor.WHITE + player.getName(), player.getUniqueId());
                    player.sendMessage(ChatColor.GREEN +"You joined Team Green!");
                    TeamManager.getInstance().joinTeam(player.getUniqueId(), TeamManager.getInstance().getTeam(2));
                    break;
                case YELLOW_WOOL:
                    plugin.setPlayerListName(ChatColor.YELLOW + "[Yellow] " + ChatColor.WHITE + player.getName(), player.getUniqueId());
                    player.sendMessage(ChatColor.YELLOW + "You joined Team Yellow!");
                    TeamManager.getInstance().joinTeam(player.getUniqueId(), TeamManager.getInstance().getTeam(3));
                    break;
                case BLACK_WOOL:
                    plugin.setPlayerListName(ChatColor.BLACK + "[Black] " + ChatColor.WHITE + player.getName(), player.getUniqueId());
                    player.sendMessage(ChatColor.BLACK + "You joined Team Black!");
                    TeamManager.getInstance().joinTeam(player.getUniqueId(), TeamManager.getInstance().getTeam(4));
                    break;
                case PURPLE_WOOL:
                    plugin.setPlayerListName(ChatColor.DARK_PURPLE + "[Purple] " + ChatColor.WHITE + player.getName(), player.getUniqueId());
                    player.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.DARK_PURPLE + "You joined Team Purple!");
                    TeamManager.getInstance().joinTeam(player.getUniqueId(), TeamManager.getInstance().getTeam(5));
                    break;
                case ORANGE_WOOL:
                    plugin.setPlayerListName(ChatColor.GOLD + "[Orange] " + ChatColor.WHITE + player.getName(), player.getUniqueId());
                    player.sendMessage(ChatColor.GOLD + "You joined Team Orange!");
                    TeamManager.getInstance().joinTeam(player.getUniqueId(), TeamManager.getInstance().getTeam(6));
                    break;
                case PINK_WOOL:
                    plugin.setPlayerListName(ChatColor.LIGHT_PURPLE + "[Pink] " + ChatColor.WHITE + player.getName(), player.getUniqueId());
                    player.sendMessage(ChatColor.LIGHT_PURPLE + "You joined Team Pink!");
                    TeamManager.getInstance().joinTeam(player.getUniqueId(), TeamManager.getInstance().getTeam(7));
                    break;
                case WHITE_WOOL:
                    plugin.setPlayerListName(ChatColor.WHITE + "[White] " + ChatColor.WHITE + player.getName(), player.getUniqueId());
                    player.sendMessage(ChatColor.WHITE + "You joined Team White!");
                    TeamManager.getInstance().joinTeam(player.getUniqueId(), TeamManager.getInstance().getTeam(8));
                    break;
                default:
                    player.sendMessage("You didn't join any team!");
                    break;
            }
            Bukkit.dispatchCommand(player, "teams");

        } else if(ChatColor.stripColor(event.getView().getTitle()).equals("Items collected")){
            event.setCancelled(true);
        } else if(ChatColor.stripColor(event.getView().getTitle()).startsWith("Backpack")){
            ItemStack item = event.getCurrentItem();
            if (item != null && item.getItemMeta() != null && item.getItemMeta().hasCustomModelData() && item.getItemMeta().getCustomModelData() == ItemHuntPlugin.BACKPACK_ID) {
                event.getWhoClicked().sendMessage(ChatColor.RED + "Cant put Backpack in itself!");
                event.setCancelled(true);
            }
        } else if(inventory != null && inventory.equals(ShowTeamsCommand.getTeamInventory())){
            ShowTeamsCommand.updateInventory();
        }
    }

}
