package weitma.itemHuntPlugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import weitma.itemHuntPlugin.ItemHuntPlugin;

public class InventoryClickListener implements Listener {

    private final ItemHuntPlugin plugin;

    public InventoryClickListener(ItemHuntPlugin plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        if(ChatColor.stripColor(event.getView().getTitle()).equals("Teams")){

            event.setCancelled(true);

            if(event.getClick().isRightClick()) return;

            Player player = (Player) event.getWhoClicked();

            if(event.getCurrentItem() == null){
                Bukkit.getLogger().info("No item clicked");
                return;
            }

            switch(event.getCurrentItem().getType()){
                case RED_WOOL:
                    player.setPlayerListName(ChatColor.RED + "[Red] " + ChatColor.WHITE + player.getName());
                    player.sendMessage(ChatColor.RED + "You joined Team Red!");
                    plugin.joinTeam(player.getUniqueId(), 1);
                    break;
                case BLUE_WOOL:
                    player.setPlayerListName(ChatColor.BLUE + "[Blue] " + ChatColor.WHITE + player.getName());
                    player.sendMessage(ChatColor.BLUE + "You joined Team Blue!");
                    plugin.joinTeam(player.getUniqueId(), 2);
                    break;
                case GREEN_WOOL:
                    player.setPlayerListName(ChatColor.GREEN + "[Green] " + ChatColor.WHITE + player.getName());
                    player.sendMessage(ChatColor.GREEN +"You joined Team Green!");
                    plugin.joinTeam(player.getUniqueId(), 3);
                    break;
                case YELLOW_WOOL:
                    player.setPlayerListName(ChatColor.YELLOW + "[Yellow] " + ChatColor.WHITE + player.getName());
                    player.sendMessage(ChatColor.YELLOW + "You joined Team Yellow!");
                    plugin.joinTeam(player.getUniqueId(), 4);
                    break;
                case BLACK_WOOL:
                    player.setPlayerListName(ChatColor.BLACK + "[Black] " + ChatColor.WHITE + player.getName());
                    player.sendMessage(ChatColor.BLACK + "You joined Team Black!");
                    plugin.joinTeam(player.getUniqueId(), 5);
                    break;
                case PURPLE_WOOL:
                    player.setPlayerListName(ChatColor.DARK_PURPLE + "[Purple] " + ChatColor.WHITE + player.getName());
                    player.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.DARK_PURPLE + "You joined Team Purple!");
                    plugin.joinTeam(player.getUniqueId(), 6);
                    break;
                case ORANGE_WOOL:
                    player.setPlayerListName(ChatColor.GOLD + "[Orange] " + ChatColor.WHITE + player.getName());
                    player.sendMessage(ChatColor.GOLD + "You joined Team Orange!");
                    plugin.joinTeam(player.getUniqueId(), 7);
                    break;
                case PINK_WOOL:
                    player.setPlayerListName(ChatColor.LIGHT_PURPLE + "[Pink] " + ChatColor.WHITE + player.getName());
                    player.sendMessage(ChatColor.LIGHT_PURPLE + "You joined Team Pink!");
                    plugin.joinTeam(player.getUniqueId(), 8);
                    break;
                case WHITE_WOOL:
                    player.setPlayerListName(ChatColor.WHITE + "[White] " + ChatColor.WHITE + player.getName());
                    player.sendMessage(ChatColor.WHITE + "You joined Team White!");
                    plugin.joinTeam(player.getUniqueId(), 9);
                    break;
                default:
                    player.sendMessage("You didn't join any team!");
                    break;
            }
            Bukkit.dispatchCommand(player, "teams");

        } else if(ChatColor.stripColor(event.getView().getTitle()).equals("Items collected")){
            event.setCancelled(true);
        } else if(ChatColor.stripColor(event.getView().getTitle()).endsWith("Backpack")){
            ItemStack item = event.getCurrentItem();
            if (item != null && item.getItemMeta() != null && item.getItemMeta().hasCustomModelData() && item.getItemMeta().getCustomModelData() == plugin.BACKPACK_ID) {
                event.getWhoClicked().sendMessage(ChatColor.RED + "Cant put Backpack in itself!");
                event.setCancelled(true);
            }
        }
    }

}
