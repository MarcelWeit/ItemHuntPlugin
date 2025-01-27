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
            if(event.getClick().isRightClick()) return;

            Player player = (Player) event.getWhoClicked();

            if(event.getCurrentItem() == null){
                Bukkit.getLogger().info("No item clicked");
                return;
            }

            switch(event.getCurrentItem().getType()){
                case RED_WOOL:
                    player.sendMessage("You joined Team Red!");
                    break;
                case BLUE_WOOL:
                    player.sendMessage("You joined Team Blue!");
                    break;
                case GREEN_WOOL:
                    player.sendMessage("You joined Team Green!");
                    break;
                case YELLOW_WOOL:
                    player.sendMessage("You joined Team Yellow!");
                    break;
                default:
                    player.sendMessage("You didn't join any team!");
                    break;
            }

            event.setCancelled(true);

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
